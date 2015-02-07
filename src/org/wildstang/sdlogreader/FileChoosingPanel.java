package org.wildstang.sdlogreader;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
		if (chooser.showOpenDialog(Main.chooserPanel) == JFileChooser.APPROVE_OPTION) {
			Main.logFile = chooser.getSelectedFile();
		} else {
			Main.logFile = null;
		}
		try {
			Deserialize.deserial();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileSelectedPanel.showFileName();
		Main.dataPanel1.sPane.getKeys();
		Main.dataPanel2.sPane.getKeys();
		Main.dataPanel3.sPane.getKeys();
		Main.dataPanel4.sPane.getKeys();
		Main.dataPanel5.sPane.getKeys();
		Main.dataPanel6.sPane.getKeys();
		Main.dataPanel7.sPane.getKeys();
		Main.dataPanel8.sPane.getKeys();
		
		fileSelectedPanel.showFileName();
	}
}
