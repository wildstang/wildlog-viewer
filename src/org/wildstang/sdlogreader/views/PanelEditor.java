package org.wildstang.sdlogreader.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.wildstang.sdlogreader.controllers.ApplicationController;

public class PanelEditor extends JPanel implements ActionListener{
	
	JButton resetZoom = new JButton("Reset Zoom");
	JButton clearAllFields = new JButton("Clear All Fields");
	
	public PanelEditor() {
		add(clearAllFields);
		add(resetZoom);
		clearAllFields.addActionListener(this);
		resetZoom.addActionListener(this);
	}
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == resetZoom) {
			ApplicationController.graphPanelViewController.resetDefaultZoom();
		} else if (event.getSource() == clearAllFields) {
			for (int i = 0; i < ApplicationController.dataPanels.length; i++) {
				ApplicationController.dataPanels[i].dataSelectPanel.clearAllFields();
			}
		}
	}
}
