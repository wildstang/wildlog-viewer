package org.wildstang.sdlogreader.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import org.wildstang.sdlogreader.controllers.ApplicationController;
import org.wildstang.sdlogreader.models.LogsModel;

public class DataPanel extends JPanel implements ActionListener, MouseMotionListener {

	public ApplicationController controller;

	GraphingPanel graphPanel;
	DataSelectPanel dataSelectPanel;

	private LogsModel model;

	public DataPanel(ApplicationController controller, Color c) {
		this.controller = controller;

		setLayout(new GridBagLayout());
		GridBagConstraints j = new GridBagConstraints();
		j.gridx = 0;
		j.gridy = 0;
		j.fill = GridBagConstraints.VERTICAL;
		j.anchor = GridBagConstraints.LINE_START;
		j.weightx = 0.0;
		j.weighty = 1.0;
		dataSelectPanel = new DataSelectPanel(c);
		dataSelectPanel.setPreferredSize(new Dimension(200, 500));
		dataSelectPanel.typeSelected.addActionListener(this);
		dataSelectPanel.keySelected.addActionListener(this);
		add(dataSelectPanel, j);

		j.gridx = 1;
		j.gridy = 0;
		j.fill = GridBagConstraints.BOTH;
		j.weightx = 1;
		j.weighty = 1.0;
		graphPanel = new GraphingPanel();
		add(graphPanel, j);

		graphPanel.addMouseMotionListener(this);

		MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
			private int startX;

			public void mousePressed(MouseEvent e) {
				startX = e.getXOnScreen();
				controller.updateDragRegion(startX, startX, true);
				System.out.println("Mouse pressed: " + e);
			}

			public void mouseDragged(MouseEvent e) {
				int currentX = e.getXOnScreen();

				// If we dragged to the left of the initial point, invert the points
				if (currentX < startX) {
					controller.updateDragRegion(currentX, startX, true);
				} else {
					controller.updateDragRegion(startX, currentX, true);
				}
				System.out.println("Mouse dragged: " + e);
			}

			public void mouseReleased(MouseEvent e) {
				int finalX = e.getXOnScreen();
				
				// If we dragged to the left of the initial point, invert the points
				if (finalX < startX) {
					controller.updateDragRegion(finalX, startX, false);
					controller.zoomToDragRegion(finalX, startX);
				} else {
					controller.updateDragRegion(startX, finalX, false);
					controller.zoomToDragRegion(startX, finalX);
				}
				
				System.out.println("Mouse released: " + e);
			}
		};

		graphPanel.addMouseMotionListener(mouseAdapter);
		graphPanel.addMouseListener(mouseAdapter);
	}

	public GraphingPanel getGraphingPanel() {
		return graphPanel;
	}

	public void updateModel(LogsModel model) {
		this.model = model;
		dataSelectPanel.updateModel(model);
		graphPanel.updateModel(model);
	}

	public void updateGraphPanelZoomAndScroll(long startTimestamp, long endTimestamp) {
		graphPanel.updateGraphView(startTimestamp, endTimestamp);
	}
	
	public void updateDragRegion(int pxStart, int pxEnd, boolean shouldShowDragRegion) {
		graphPanel.updateDragRegion(pxStart, pxEnd, shouldShowDragRegion);
	}

	public void actionPerformed(ActionEvent e) {

		System.out.println("actionPerformed");
		if (e.getSource() == dataSelectPanel.typeSelected) {
			switch (dataSelectPanel.typeSelected.getSelectedIndex()) {
			case 0:
				graphPanel.setType(GraphingPanel.DEFAULT_TYPE);
				break;
			case 1:
				System.out.println("case1");
				graphPanel.setType(GraphingPanel.BOOL_TYPE);
				break;
			case 2:
				graphPanel.setType(GraphingPanel.DOUBLE_TYPE);
				break;
			case 3:
				graphPanel.setType(GraphingPanel.STRING_TYPE);
				break;
			}
		} else if (e.getSource() == dataSelectPanel.keySelected) {
			graphPanel.setDataKey((String) dataSelectPanel.keySelected.getSelectedItem());
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		controller.updateMousePosition(e.getX(), e.getY());
	}

	public void updateMousePosition(int posX, int posY) {
		graphPanel.updateMousePosition(posX, posY);
	}

	public void updateGraphView(long startTimestamp, long endTimestamp) {
		graphPanel.updateGraphView(startTimestamp, endTimestamp);
	}
}
