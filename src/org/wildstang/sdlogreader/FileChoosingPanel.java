package org.wildstang.sdlogreader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class FileChoosingPanel extends JPanel implements ActionListener{
	
	JButton readFileStart = new JButton("Select Data File from SD Card");
	
	public FileChoosingPanel() {
		add(readFileStart);
		readFileStart.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == readFileStart)
		{
			chooseFile();
		}
	}
	
	public void chooseFile() {
		JFileChooser chooser = new JFileChooser();
		File startFile = new File(System.getProperty("user.home"));
		chooser.setCurrentDirectory(chooser.getFileSystemView().getParentDirectory(startFile));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Select the Local location");
		if (chooser.showOpenDialog(Main.logPanel) == JFileChooser.APPROVE_OPTION) {
			Main.logFile = chooser.getSelectedFile();
		} else {
			Main.logFile = null;
		}
		try {
			Deserialize.deserial();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Main.logPanel.drawData();
		Main.fileSelectedPanel.showFileName();
	}
}
