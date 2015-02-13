package org.wildstang.sdlogreader.views;

import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.wildstang.sdlogreader.controllers.ApplicationController;

public class ScrollBarPanel extends JPanel implements AdjustmentListener {
	JScrollBar scrollBar;

	private ApplicationController controller;

	public ScrollBarPanel(ApplicationController controller) {
		this.controller = controller;
		setLayout(new BorderLayout());
		scrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1000, 0, 1000);
		add(scrollBar, BorderLayout.CENTER);
		scrollBar.addAdjustmentListener(this);
	}

	public void adjustmentValueChanged(AdjustmentEvent event) {
		System.out.println(scrollBar.getValue());
		scrollBarUpdated();
	}

	private void scrollBarUpdated() {
		controller.updateScrollBarPosition(getScrollPosition(), scrollBar.getMinimum(), scrollBar.getMaximum());
	}

	/**
	 * Calculates the scroller's position in the range (min, max)
	 * 
	 * Normally the position only ranges from (min, max - extent). We map it to the total range to simplify calculations.
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
		System.out.println("Setting scroll value to " + value);
		scrollBar.setValue(value);
		System.out.println("REad scroll value: " + scrollBar.getValue());
	}

	public void setScrollBarExtent(int extent) {
		// Don't let the extent get bigger than the total range
		if (extent >= scrollBar.getMaximum() - scrollBar.getMinimum()) {
			System.out.println("Extent too big. Clipping.");
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
