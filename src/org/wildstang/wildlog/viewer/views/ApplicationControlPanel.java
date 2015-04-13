package org.wildstang.wildlog.viewer.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;

public class ApplicationControlPanel extends JPanel implements ActionListener {

	private ApplicationController controller;

	JButton resetZoom = new JButton("Reset Zoom");
	JButton clearAllFields = new JButton("Clear All Fields");

	public ApplicationControlPanel(ApplicationController controller) {
		this.controller = controller;
		add(clearAllFields);
		add(resetZoom);
		clearAllFields.addActionListener(this);
		resetZoom.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == resetZoom) {
			controller.resetZoomToDefault();
		} else if (event.getSource() == clearAllFields) {
			controller.clearAllFields();
		}
	}
}
