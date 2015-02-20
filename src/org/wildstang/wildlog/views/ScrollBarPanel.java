package org.wildstang.wildlog.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.wildstang.wildlog.controllers.ApplicationController;

public class ScrollBarPanel extends JPanel {
	JScrollBar scrollBar;

	private ApplicationController controller;

	public ScrollBarPanel(ApplicationController controller) {
		this.controller = controller;
		setLayout(new BorderLayout());
		scrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1000, 0, 1000);
		scrollBar.setMinimumSize(new Dimension(0, 80));
		add(scrollBar, BorderLayout.CENTER);
	}

	public void addAdjustmentListener(AdjustmentListener listener) {
		scrollBar.addAdjustmentListener(listener);
	}

	/**
	 * Calculates the scroller's position in the range (min, max)
	 * 
	 * Normally the position only ranges from (min, max - extent). We map it to the total range to simplify
	 * calculations.
	 * 
	 * @return mapped scroll position
	 */
	public int getScrollPosition() {
		return (int) (((double) scrollBar.getValue() / (((double) scrollBar.getMaximum() - (double) scrollBar.getMinimum()) - (double) scrollBar.getModel().getExtent())) * ((double) scrollBar
				.getMaximum() - (double) scrollBar.getMinimum()));
	}

	public int getMinimum() {
		return scrollBar.getMinimum();
	}

	public int getMaximum() {
		return scrollBar.getMaximum();
	}

	public void scrollByValue(int value) {
		scrollBar.setValue(scrollBar.getValue() + value);
	}

	public void scrollToValue(int value) {
		// should be in the range (min, max - extent)
		scrollBar.setValue(value);
	}

	public void setScrollBarExtent(int extent) {
		// Don't let the extent get bigger than the total range
		if (extent >= scrollBar.getMaximum() - scrollBar.getMinimum()) {
			extent = scrollBar.getMaximum() - scrollBar.getMinimum();
			scrollBar.setValue(0);
		}
		// If the edge of the scrollbar would be past the edge of the screen,
		// scroll to the right to keep the entire scrollbar visible
		if (scrollBar.getValue() + extent > scrollBar.getMaximum()) {
			scrollBar.setValue(scrollBar.getMaximum() - extent);
		}
		scrollBar.getModel().setExtent(extent);
	}

}
