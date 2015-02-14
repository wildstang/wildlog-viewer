package org.wildstang.wildlog.controllers;

import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.wildstang.wildlog.models.LogsModel;
import org.wildstang.wildlog.views.ScrollBarPanel;

public class GraphPanelController implements MouseWheelListener {

	private ApplicationController controller;
	private LogsModel model;

	private static final double deltaZoomFactor = 0.1;

	public double zoomFactor = 1.0;
	private int scrollPosition = 0;
	private int minValue, maxValue, deltaPos;

	private long desiredStartTimestamp, desiredEndTimestamp;
	private boolean shouldScrollToDesiredTimestamps;

	public GraphPanelController(ApplicationController c) {
		controller = c;
	}

	private void recalculateAndUpdate() {
		if (model == null) {
			return;
		}

		ScrollBarPanel s = controller.getScroller();
		scrollPosition = s.getScrollPosition();
		minValue = s.getMinimum();
		maxValue = s.getMaximum();

		if (!shouldScrollToDesiredTimestamps) {
			double totalWindowWidth = model.getEndTimestamp() - model.getStartTimestamp();
			double slidingWindowWidth = (model.getEndTimestamp() - model.getStartTimestamp()) / zoomFactor;
			System.out.println("sliding window width (calc): " + slidingWindowWidth);
			double positionOfBeginningOfSlidingWindow = model.getStartTimestamp() + (totalWindowWidth - slidingWindowWidth) * ((double) scrollPosition / ((double) maxValue - (double) minValue));
			int scrollbarExtent = (int) ((maxValue - minValue) * (1 / zoomFactor));
			controller.updateGraphPanelZoomAndScroll((long) positionOfBeginningOfSlidingWindow, (long) (positionOfBeginningOfSlidingWindow + slidingWindowWidth));
			controller.updateScrollBarExtent(scrollbarExtent);
		} else {
			// Calculate zoom factor
			double totalWindowWidth = model.getEndTimestamp() - model.getStartTimestamp();
			double slidingWindowWidth = desiredEndTimestamp - desiredStartTimestamp;
			double positionOfBeginningOfSlidingWindow = desiredStartTimestamp;
			zoomFactor = totalWindowWidth / slidingWindowWidth;
			// Calculate scrollbar extent
			int scrollbarExtent = (int) ((maxValue - minValue) * (1 / zoomFactor));
			// Calculate scrollbar position
			int scrollbarPosition = (int) ((positionOfBeginningOfSlidingWindow / ((model.getEndTimestamp() - slidingWindowWidth) - model.getStartTimestamp())) * ((double) maxValue - scrollbarExtent - (double) minValue));
			System.out.println("scrollbar position (timestamps): " + scrollbarPosition);
			controller.updateGraphPanelZoomAndScroll((long) desiredStartTimestamp, (long) (desiredEndTimestamp));
			controller.scrollToValue(scrollbarPosition);
			controller.updateScrollBarExtent(scrollbarExtent);

			shouldScrollToDesiredTimestamps = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getModifiers() == InputEvent.CTRL_MASK) {
			// We subtract so that scrolling zooms in the right direction
			zoomFactor -= e.getPreciseWheelRotation() * deltaZoomFactor;
			if (zoomFactor < 1) {
				zoomFactor = 1.0;
			}
		} else {
			controller.scrollByValue(e.getWheelRotation());
		}
		recalculateAndUpdate();
	}

	public void resetDefaultZoom() {
		zoomFactor = 1.0;
		recalculateAndUpdate();
	}

	public void updateModel(LogsModel model) {
		this.model = model;
		recalculateAndUpdate();
	}

	public void scrollPositionUpdated() {
		recalculateAndUpdate();
	}

	public void zoomAndScrollToTimestampRange(long startTimestamp, long endTimestamp) {
		desiredStartTimestamp = startTimestamp;
		desiredEndTimestamp = endTimestamp;
		shouldScrollToDesiredTimestamps = true;
		recalculateAndUpdate();
	}
}
