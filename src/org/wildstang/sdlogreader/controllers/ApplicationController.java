package org.wildstang.sdlogreader.controllers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.wildstang.sdlogreader.models.LogsModel;
import org.wildstang.sdlogreader.views.DataPanel;
import org.wildstang.sdlogreader.views.FileChoosingPanel;
import org.wildstang.sdlogreader.views.ScrollBarPanel;
import org.wildstang.sdlogreader.views.TimelinePanel;

public class ApplicationController implements ComponentListener{

	private static final int NUM_DATA_PANELS = 8;
	public static JFrame frame;
	private FileChoosingPanel chooserPanel;

	public static DataPanel[] dataPanels = new DataPanel[NUM_DATA_PANELS];
	public static Color[] theWSRainbow = new Color[NUM_DATA_PANELS];
	public static TimelinePanel timeline;
	private ScrollBarPanel scrollBar;

	// Controllers
	public static GraphPanelController graphPanelViewController;

	public void initializeApplication() {
		try {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exc){

		}
		initFrameComponents();
		organizeFrameComponents();

		// Init controllers
		graphPanelViewController = new GraphPanelController(this);
		frame.addMouseWheelListener(graphPanelViewController);
	}

	private void initFrameComponents() {
		frame = new JFrame("WildStang: SD Log Reader");
		chooserPanel = new FileChoosingPanel(this);
		
		timeline = new TimelinePanel();
		timeline.setMinimumSize(new Dimension(0, 20));
		
		theWSRainbow[0] = new Color(255, 0, 0);
		theWSRainbow[1] = new Color(255, 127, 0);
		theWSRainbow[2] = new Color(255, 255, 0);
		theWSRainbow[3] = new Color(0, 255, 0);
		theWSRainbow[4] = new Color(0, 127, 255);
		theWSRainbow[5] = new Color(0, 0, 255);
		theWSRainbow[6] = new Color(127, 0, 255);
		theWSRainbow[7] = new Color(255, 0, 255);
		
		for (int i = 0; i < dataPanels.length; i++) {
			dataPanels[i] = new DataPanel(this, theWSRainbow[i]);
		}

		dataPanels[0].addComponentListener(this);
		scrollBar = new ScrollBarPanel(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private void organizeFrameComponents() {
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		frame.add(chooserPanel, c);
		c.gridy = 1;
		c.weighty = 0.0;
		frame.add(timeline, c);
		c.gridy = 2;
		c.weighty = 1.0;
		frame.add(dataPanels[0], c);
		c.gridy = 3;
		frame.add(dataPanels[1], c);
		c.gridy = 4;
		frame.add(dataPanels[2], c);
		c.gridy = 5;
		frame.add(dataPanels[3], c);
		c.gridy = 6;
		frame.add(dataPanels[4], c);
		c.gridy = 7;
		frame.add(dataPanels[5], c);
		c.gridy = 8;
		frame.add(dataPanels[6], c);
		c.gridy = 9;
		frame.add(dataPanels[7], c);
		c.gridy = 10;
		c.weighty = 0.0;
		frame.add(scrollBar, c);
		frame.pack();
		frame.setVisible(true);
	}

	public void updateMousePosition(int x, int y) {
		for (int i = 0; i < dataPanels.length; i++) {
			dataPanels[i].updateMousePosition(x, y);
		}
		timeline.updateMosuePosition(x, y);
	}

	public void updateLogsModel(LogsModel model) {
		for (int i = 0; i < dataPanels.length; i++) {
			dataPanels[i].updateModel(model);
		}
		graphPanelViewController.updateModel(model);
	}

	public void updateGraphPanelZoomAndScroll(long startTimestamp, long endTimestamp) {
		for (int i = 0; i < dataPanels.length; i++) {
			dataPanels[i].updateGraphPanelZoomAndScroll(startTimestamp, endTimestamp);
		}
		timeline.updateGraphPanelZoomAndScroll(startTimestamp, endTimestamp);
	}

	public void scrollByValue(int value) {
		scrollBar.scrollByValue(value);
	}
	
	public void scrollToValue(int value) {
		scrollBar.scrollToValue(value);
	}

	public void updateScrollBarPosition(int position, int min, int max) {
		position = 100;
		graphPanelViewController.scrollPositionUpdated();
		System.out.println(position +", " + min + ", "+max);
	}

	public void updateScrollBarExtent(int scrollbarExtent) {
		scrollBar.setScrollBarExtent(scrollbarExtent);
	}

	public ScrollBarPanel getScroller() {
		return scrollBar;
	}

	public void updateDataPanelBounds(int leftEdgePx, int rightEdgePx) {
		timeline.updateGraphingPanelsBounds(leftEdgePx, rightEdgePx);
	}
	
	public void updateDragRegion(int pxStart, int pxEnd, boolean shouldShowDragRegion) {
		for (int i = 0; i < dataPanels.length; i++) {
			dataPanels[i].updateDragRegion(pxStart, pxEnd, shouldShowDragRegion);
		}
	}
	
	public void zoomToDragRegion(int pxStart, int pxEnd) {
		// Map pixels to timestamps
		long startTimestamp = dataPanels[0].getGraphingPanel().mapAbsoluteMousePositionToTimestamp(pxStart);
		long endTimestamp = dataPanels[0].getGraphingPanel().mapAbsoluteMousePositionToTimestamp(pxEnd);
		graphPanelViewController.zoomAndScrollToTimestampRange(startTimestamp, endTimestamp);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		//System.out.println("Component event!");

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		//System.out.println("Component event!");
		if (e.getSource() == dataPanels[0]) {
			Point pos = dataPanels[0].getGraphingPanel().getLocationOnScreen();
			Rectangle bounds = dataPanels[0].getGraphingPanel().getBounds();
			updateDataPanelBounds(pos.x, pos.x + bounds.width);
		}

	}

	@Override
	public void componentResized(ComponentEvent e) {
		//System.out.println("Component event!");
		if (e.getSource() == dataPanels[0]) {
			Point pos = dataPanels[0].getGraphingPanel().getLocationOnScreen();
			Rectangle bounds = dataPanels[0].getGraphingPanel().getBounds();
			updateDataPanelBounds(pos.x, pos.x + bounds.width);
			//chooserPanel.resizeBorder();
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
		//System.out.println("Component event!");

	}
}
