package org.wildstang.wildlog.controllers;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.wildstang.wildlog.models.LogsModel;
import org.wildstang.wildlog.views.ScrollBarPanel;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class GraphPanelController implements MouseWheelListener, AdjustmentListener {

	private static final double DELTA_ZOOM_FACTOR = 0.1;

	private ApplicationController controller;
	private ScrollBarPanel scrollPanel;
	private LogsModel model;

	// State variables
	private long currentStartTimestamp, currentWindowWidth;
	private double zoomFactor = 1.0;

	private boolean ignoreNextScrollBarPositionUpdate = false;
	private boolean dontDoNextScrollbarPositionCalculation = false;

	public GraphPanelController(ApplicationController c, ScrollBarPanel scroll) {
		controller = c;
		scrollPanel = scroll;
		scrollPanel.addAdjustmentListener(this);
	}

	private void recalculateAndUpdate() {
		if (model == null) {
			return;
		}

		int scrollMinValue = scrollPanel.getMinimum();
		int scrollMaxValue = scrollPanel.getMaximum();

		// Normalize all values
		limitValues();

		controller.updateGraphPanelZoomAndScroll(currentStartTimestamp, currentStartTimestamp + currentWindowWidth);

		int scrollbarExtent = (int) ((scrollMaxValue - scrollMinValue) * ((double) currentWindowWidth / ((double) model.getEndTimestamp() - (double) model.getStartTimestamp())));
		scrollPanel.setScrollBarExtent(scrollbarExtent);

		if (!dontDoNextScrollbarPositionCalculation) {
			int scrollbarPosition = (int) ((double) (scrollMaxValue - scrollbarExtent - scrollMinValue) * (((double) currentStartTimestamp - (double) model.getStartTimestamp()) / ((double) model
					.getEndTimestamp() - (double) model.getStartTimestamp() - (double) currentWindowWidth)));
			System.out.println("Calculated scrollbar position: " + scrollbarPosition);
			scrollPanel.scrollToValue(scrollbarPosition);
		}

		ignoreNextScrollBarPositionUpdate = false;
		dontDoNextScrollbarPositionCalculation = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (model == null) {
			return;
		}
		if (e.getModifiers() == InputEvent.CTRL_MASK) {
			// We multiply by -1 so that scrolling zooms in the right direction
			double deltaSlidingWindowWidth = currentWindowWidth * (e.getPreciseWheelRotation() * DELTA_ZOOM_FACTOR * -1);
			// Keep the sliding window centered by adding half the delta width to the start timestamp
			currentStartTimestamp = (long) (currentStartTimestamp - (deltaSlidingWindowWidth / 2));
			currentWindowWidth = (long) (currentWindowWidth - deltaSlidingWindowWidth);

			// Track the zoom factor
			zoomFactor -= (e.getPreciseWheelRotation() * DELTA_ZOOM_FACTOR);
			if (zoomFactor < 1) {
				zoomFactor = 1.0;
			}
			ignoreNextScrollBarPositionUpdate = true;
		} else {
			currentStartTimestamp -= (e.getWheelRotation() * 5);
			ignoreNextScrollBarPositionUpdate = true;
		}
		recalculateAndUpdate();
	}

	public void resetDefaultZoom() {
		zoomFactor = 1.0;
		currentStartTimestamp = model.getStartTimestamp();
		currentWindowWidth = (model.getEndTimestamp() - model.getStartTimestamp());
		ignoreNextScrollBarPositionUpdate = true;
		recalculateAndUpdate();
	}

	public void updateModel(LogsModel model) {
		this.model = model;
		// Default to zoomed all the way out
		currentStartTimestamp = model.getStartTimestamp();
		currentWindowWidth = (model.getEndTimestamp() - model.getStartTimestamp());
		ignoreNextScrollBarPositionUpdate = true;
		recalculateAndUpdate();
	}

	public void zoomAndScrollToTimestampRange(long startTimestamp, long endTimestamp) {
		currentStartTimestamp = startTimestamp;
		currentWindowWidth = endTimestamp - startTimestamp;
		ignoreNextScrollBarPositionUpdate = true;
		recalculateAndUpdate();
	}

	private void limitValues() {
		// If the current window is wider than the total window width, limit it.
		if (currentWindowWidth > (model.getEndTimestamp() - model.getStartTimestamp())) {
			currentWindowWidth = (model.getEndTimestamp() - model.getStartTimestamp());
			zoomFactor = 1.0;
		}

		// Check if the beginning of the window would be below the start timestamp.
		if (currentStartTimestamp < model.getStartTimestamp()) {
			currentStartTimestamp = model.getStartTimestamp();
		}

		if (currentStartTimestamp + currentWindowWidth > model.getEndTimestamp()) {
			currentStartTimestamp = currentStartTimestamp - ((currentStartTimestamp + currentWindowWidth) - model.getEndTimestamp());
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (ignoreNextScrollBarPositionUpdate) {
			return;
		}

		int scrollerPosition = scrollPanel.getScrollPosition();
		System.out.println("scroll position: " + scrollerPosition);

		// TODO Fix this calculation
		currentStartTimestamp = (int) (((double) model.getEndTimestamp() - (double) model.getStartTimestamp() - (double) currentWindowWidth) * ((double) scrollerPosition / ((double) scrollPanel
				.getMaximum() - (double) scrollPanel.getMinimum())));
		System.out.println("start timestamp: " + currentStartTimestamp);
		dontDoNextScrollbarPositionCalculation = true;
		recalculateAndUpdate();
	}
}
