package org.wildstang.wildlog.viewer.controllers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.wildstang.wildlog.viewer.models.LogsModel;
import org.wildstang.wildlog.viewer.views.DataPanel;
import org.wildstang.wildlog.viewer.views.FileChoosingPanel;
import org.wildstang.wildlog.viewer.views.ScrollBarPanel;
import org.wildstang.wildlog.viewer.views.TimelinePanel;

public class ApplicationController {

	public static final int DATA_SELECT_PANEL_WIDTH = 150;
	private static final int NUM_DATA_PANELS = 8;
	public static JFrame frame;
	public FileChoosingPanel chooserPanel;

	public DataPanel[] dataPanels = new DataPanel[NUM_DATA_PANELS];
	public Color[] theWSRainbow = new Color[NUM_DATA_PANELS];
	public TimelinePanel timeline;
	private ScrollBarPanel scrollBar;

	// Controllers
	public static GraphPanelController graphPanelViewController;

	public void initializeApplication() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exc) {

		}
		initFrameComponents();
		organizeFrameComponents();

		// Init controllers
		graphPanelViewController = new GraphPanelController(this, scrollBar);
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
		timeline.updateMousePosition(x, y);
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

	public void updateDragRegion(int pxStart, int pxEnd, boolean shouldShowDragRegion) {
		for (int i = 0; i < dataPanels.length; i++) {
			dataPanels[i].updateDragRegion(pxStart, pxEnd, shouldShowDragRegion);
		}
	}

	public void zoomToDragRegion(int pxStart, int pxEnd) {
		// Map pixels to timestamps
		long startTimestamp = dataPanels[0].getGraphingPanel().mapAbsoluteMousePositionToTimestamp(pxStart);
		long endTimestamp = dataPanels[0].getGraphingPanel().mapAbsoluteMousePositionToTimestamp(pxEnd);
		if (startTimestamp == endTimestamp) {
			// Don't drag to a region 0 time wide! Abort! Abort!
			return;
		}
		graphPanelViewController.zoomAndScrollToTimestampRange(startTimestamp, endTimestamp);
	}

	public void clearAllFields() {
		for (int i = 0; i < dataPanels.length; i++) {
			dataPanels[i].dataSelectPanel.clearAllFields();
		}
	}

	public void resetZoomToDefault() {
		graphPanelViewController.resetDefaultZoom();
	}

	public void errorReadingFile() {
		JOptionPane.showMessageDialog(frame, "Error reading the selected file. Please try another file.", "Error", JOptionPane.ERROR_MESSAGE);
	}
}
