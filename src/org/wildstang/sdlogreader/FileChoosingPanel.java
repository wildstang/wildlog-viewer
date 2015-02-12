package org.wildstang.sdlogreader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.wildstang.sdlogreader.controllers.ApplicationController;
import org.wildstang.sdlogreader.models.Deserializer;
import org.wildstang.sdlogreader.models.LogsModel;

public class FileChoosingPanel extends JPanel implements ActionListener {

	private ApplicationController controller;

	SelectedFilePanel fileSelectedPanel = new SelectedFilePanel();
	JButton readFileStart = new JButton("Select Data File from SD Card");
	JButton resetZoom = new JButton("Reset Zoom");
	JButton clearAllFields = new JButton("Clear All Fields");

	public FileChoosingPanel(ApplicationController c) {
		controller = c;
		add(fileSelectedPanel);
		add(readFileStart);
		add(clearAllFields);
		add(resetZoom);
		readFileStart.addActionListener(this);
		clearAllFields.addActionListener(this);
		resetZoom.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		System.out.println("In actionPerformed of FileChoosing");
		if (event.getSource() == readFileStart) {
			chooseFile();
		} else if (event.getSource() == resetZoom) {
			ApplicationController.graphPanelViewController.resetDefaultZoom();
		} else if (event.getSource() == clearAllFields) {
			for (int i = 0; i < ApplicationController.dataPanels.length; i++) {
				ApplicationController.dataPanels[i].dataSelectPanel.clearAllFields();
			}
		}
	}

	public void chooseFile() {
		JFileChooser chooser = new JFileChooser();
		File startFile = new File(System.getProperty("user.home"));
		chooser.setCurrentDirectory(chooser.getFileSystemView().getParentDirectory(startFile));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Select the Local location");
		File file;
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
		} else {
			file = null;
		}
		LogsModel model;
		try {
			model = Deserializer.loadLogsModelFromFile(file);
			controller.updateLogsModel(model);
			fileSelectedPanel.showFileName(file.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
