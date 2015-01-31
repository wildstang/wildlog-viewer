package org.wildstang.sdlogreader;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SelectedFilePanel extends JPanel{
	Main window;
	JLabel fileLabel = new JLabel("File: ");
	JTextField fileName = new JTextField("No file Selected", 15);
	public SelectedFilePanel(Main m) {
		window = m;
		this.add(fileLabel);
		this.add(fileName);
		fileName.setEditable(false);
	}
	public void showFileName() {
		fileName.setText(Main.logFile.getName());
	}
}
