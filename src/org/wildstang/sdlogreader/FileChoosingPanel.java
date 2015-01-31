package org.wildstang.sdlogreader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class FileChoosingPanel extends JPanel implements ActionListener{
	
	Main window;
	String buttonText = "Select Data File from SD Card";
	
	JButton readFileStart = new JButton(buttonText);
	
	public FileChoosingPanel(Main m) {
		window = m;
		this.add(readFileStart);
		readFileStart.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command == buttonText) {
			chooseFile();
		}
	}
	public void chooseFile() {
		JFileChooser chooser = new JFileChooser();
		File startFile = new File(System.getProperty("user.home"));
		chooser.setCurrentDirectory(chooser.getFileSystemView().getParentDirectory(startFile));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Select the Local location");
		if (chooser.showOpenDialog(window.logPanel) == JFileChooser.APPROVE_OPTION) {
			window.logFile = chooser.getSelectedFile();
		} else {
			window.logFile = null;
		}
		Deserialize.deserial();
		Main.logPanel.refreshData();
		Main.fileSelectedPanel.showFileName();
	}
}
