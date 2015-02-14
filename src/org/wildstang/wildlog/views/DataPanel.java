package org.wildstang.wildlog.views;

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

import org.wildstang.wildlog.controllers.ApplicationController;
import org.wildstang.wildlog.models.LogsModel;

public class DataPanel extends JPanel implements ActionListener, MouseMotionListener {

	public ApplicationController controller;

	GraphingPanel graphPanel;
	public DataSelectPanel dataSelectPanel;

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
		dataSelectPanel.keySelected.addActionListener(this);
		add(dataSelectPanel, j);

		j.gridx = 1;
		j.gridy = 0;
		j.fill = GridBagConstraints.BOTH;
		j.weightx = 1;
		j.weighty = 1.0;
		graphPanel = new GraphingPanel(c);
		add(graphPanel, j);

		graphPanel.addMouseMotionListener(this);

		MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
			private int startX;

			public void mousePressed(MouseEvent e) {
				startX = e.getXOnScreen();
				DataPanel.this.controller.updateDragRegion(startX, startX, true);
			}

			public void mouseDragged(MouseEvent e) {
				int currentX = e.getXOnScreen();

				// If we dragged to the left of the initial point, invert the points
				if (currentX < startX) {
					DataPanel.this.controller.updateDragRegion(currentX, startX, true);
				} else {
					DataPanel.this.controller.updateDragRegion(startX, currentX, true);
				}
			}

			public void mouseReleased(MouseEvent e) {
				int finalX = e.getXOnScreen();

				// If we dragged to the left of the initial point, invert the points
				if (finalX < startX) {
					DataPanel.this.controller.updateDragRegion(finalX, startX, false);
					DataPanel.this.controller.zoomToDragRegion(finalX, startX);
				} else {
					DataPanel.this.controller.updateDragRegion(startX, finalX, false);
					DataPanel.this.controller.zoomToDragRegion(startX, finalX);
				}
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
		if (e.getSource() == dataSelectPanel.keySelected) {
			String selectedKey = (String) dataSelectPanel.keySelected.getSelectedItem();
			dataKeyUpdated(selectedKey);
		}
	}
	
	public void dataKeyUpdated(String newKey) {
		graphPanel.setDataKey(newKey);
		Class<?> clazz = model.getClassTypeForKey(newKey);

		if (clazz.equals(Double.class)) {
			dataSelectPanel.setDataTypeText("Double");
			graphPanel.setType(GraphingPanel.DOUBLE_TYPE);
		} else if (clazz.equals(String.class)) {
			dataSelectPanel.setDataTypeText("String");
			graphPanel.setType(GraphingPanel.STRING_TYPE);
		} else if (clazz.equals(Boolean.class)) {
			dataSelectPanel.setDataTypeText("Boolean");
			graphPanel.setType(GraphingPanel.BOOL_TYPE);
		} else {
			dataSelectPanel.setDataTypeText("Invalid type!");
			graphPanel.setType(GraphingPanel.DEFAULT_TYPE);
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
