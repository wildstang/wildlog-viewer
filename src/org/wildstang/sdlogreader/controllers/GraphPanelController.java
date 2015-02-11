package org.wildstang.sdlogreader.controllers;

import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.wildstang.sdlogreader.models.LogsModel;
import org.wildstang.sdlogreader.views.ScrollBarPanel;

public class GraphPanelController implements MouseWheelListener {

	private ApplicationController controller;
	private LogsModel model;

	private static final double deltaZoomFactor = 0.1;

	private double zoomFactor = 1.0;
	private int scrollPosition = 0;
	private int minValue, maxValue;

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

		double totalWindowWidth = model.getEndTimestamp() - model.getStartTimestamp();
		double slidingWindowWidth = (model.getEndTimestamp() - model.getStartTimestamp()) / zoomFactor;
		double positionOfBeginningOfSlidingWindow = model.getStartTimestamp() + (totalWindowWidth - slidingWindowWidth) * ((double) scrollPosition / ((double) maxValue - (double) minValue));
		int scrollbarExtent = (int) ((maxValue - minValue) * (1 / zoomFactor));
		controller.updateGraphPanelZoomAndScroll((long) positionOfBeginningOfSlidingWindow, (long) (positionOfBeginningOfSlidingWindow + slidingWindowWidth));
		controller.updateScrollBarExtent(scrollbarExtent);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getModifiers() == InputEvent.CTRL_MASK) {
			// We subtract so that scrolling zooms in the right direction
			zoomFactor -= e.getPreciseWheelRotation() * deltaZoomFactor;
			if (zoomFactor < 1) {
				zoomFactor = 1;
			}
		} else {
			controller.scrollByValue(e.getWheelRotation());
		}
		recalculateAndUpdate();
	}

	public void updateModel(LogsModel model) {
		this.model = model;
		recalculateAndUpdate();
	}

	public void scrollPositionUpdated() {
		recalculateAndUpdate();
	}

}
