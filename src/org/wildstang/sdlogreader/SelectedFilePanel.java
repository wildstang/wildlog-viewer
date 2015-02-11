package org.wildstang.sdlogreader;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SelectedFilePanel extends JPanel {

	JLabel fileLabel = new JLabel("File: ");
	JTextField fileName = new JTextField("No file Selected", 15);

	public SelectedFilePanel() {
		this.add(fileLabel);
		this.add(fileName);
		fileName.setEditable(false);
	}

	public void showFileName(String name) {
		fileName.setText(name);
	}
}
