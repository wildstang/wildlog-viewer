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
		scrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 100, 0, 1000);
		add(scrollBar, BorderLayout.CENTER);
		scrollBar.addAdjustmentListener(this);
	}

	public void adjustmentValueChanged(AdjustmentEvent event) {
		//System.out.println(scrollBar.getValue());
		scrollBarUpdated();
	}

	private void scrollBarUpdated() {
		controller.updateScrollBarPosition(getScrollPosition(), scrollBar.getMinimum(), scrollBar.getMaximum());
	}

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

	public void setScrollBarExtent(int extent) {
		System.out.println("Extent: " + extent);
		if (extent >= scrollBar.getMaximum() - scrollBar.getMinimum()) {
			//System.out.println("Extent too big. Clipping.");
			extent = scrollBar.getMaximum() - scrollBar.getMinimum();
			scrollBar.setValue(0);
		}
		if (scrollBar.getValue() + extent > scrollBar.getMaximum()) {
			scrollBar.setValue(scrollBar.getMaximum() - extent);
		}
		scrollBar.getModel().setExtent(extent);
	}

}
