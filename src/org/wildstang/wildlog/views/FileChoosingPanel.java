package org.wildstang.wildlog.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.wildstang.wildlog.controllers.ApplicationController;
import org.wildstang.wildlog.models.Deserializer;
import org.wildstang.wildlog.models.LogsModel;

public class FileChoosingPanel extends JPanel implements ActionListener {

	private ApplicationController controller;

	WildStangLogoPanel logoPanel = new WildStangLogoPanel();
	JLabel fileLabel = new JLabel("File: ");
	JTextField fileName = new JTextField("No file Selected", 15);
	JButton readFileStart = new JButton("Select Data File from SD Card");

	PanelEditor pEditor = new PanelEditor();

	public FileChoosingPanel(ApplicationController c) {
		controller = c;
		fileName.setEditable(false);
		setLayout(new BorderLayout());
		JPanel paneLeft = new JPanel();
		paneLeft.setLayout(new FlowLayout(FlowLayout.LEADING));
		paneLeft.add(fileLabel);
		paneLeft.add(fileName);
		paneLeft.add(readFileStart);
		JPanel paneRight = new JPanel();
		paneRight.setLayout(new FlowLayout(FlowLayout.TRAILING));
		paneRight.add(pEditor);
		add(paneLeft, BorderLayout.WEST);
		try {
			Image wsLogo = ImageIO.read(getClass().getResourceAsStream("/wildstang-logo.png"));
			// Scale this to 50px tall
			double scaleFactor = 50.0 / (double) wsLogo.getHeight(null);
			wsLogo = wsLogo.getScaledInstance((int) ((double) wsLogo.getWidth(null) * scaleFactor), 50, 0);
			add(new JLabel(new ImageIcon(wsLogo)), BorderLayout.CENTER);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		add(paneRight, BorderLayout.EAST);
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
			fileName.setText(file.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
