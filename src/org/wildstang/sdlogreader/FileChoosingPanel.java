package org.wildstang.sdlogreader;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class FileChoosingPanel extends JPanel implements ActionListener {

	SelectedFilePanel fileSelectedPanel = new SelectedFilePanel();
	JButton readFileStart = new JButton("Select Data File from SD Card");

	public FileChoosingPanel() {
		add(fileSelectedPanel);
		add(readFileStart);
		readFileStart.addActionListener(this);
		setBackground(Color.WHITE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == readFileStart) {
			chooseFile();
		}
	}

	public void chooseFile() {
		JFileChooser chooser = new JFileChooser();
		File startFile = new File(System.getProperty("user.home"));
		chooser.setCurrentDirectory(chooser.getFileSystemView()
				.getParentDirectory(startFile));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Select the Local location");
		if (chooser.showOpenDialog(Main.logPanel) == JFileChooser.APPROVE_OPTION) {
			Main.logFile = chooser.getSelectedFile();
		} else {
			Main.logFile = null;
		}
		Deserialize.deserial();
		fileSelectedPanel.showFileName();
		Main.sensorPanel1.getKeys();
		Main.sensorPanel2.getKeys();
		Main.sensorPanel3.getKeys();
		Main.sensorPanel4.getKeys();
		Main.sensorPanel5.getKeys();
		Main.sensorPanel6.getKeys();
		Main.sensorPanel7.getKeys();
		Main.sensorPanel8.getKeys();
	}
}
