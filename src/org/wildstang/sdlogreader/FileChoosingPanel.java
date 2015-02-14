package org.wildstang.sdlogreader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.wildstang.sdlogreader.controllers.ApplicationController;
import org.wildstang.sdlogreader.models.Deserializer;
import org.wildstang.sdlogreader.models.LogsModel;
import org.wildstang.sdlogreader.views.PanelEditor;

public class FileChoosingPanel extends JPanel implements ActionListener {
	
	Image wsLogo;
	
	private ApplicationController controller;
	private static final int MINIMUM_FRAME_WIDTH = 575;
	SelectedFilePanel fileSelectedPanel = new SelectedFilePanel();
	JButton readFileStart = new JButton("Select Data File from SD Card");
	PanelEditor pEditor = new PanelEditor();
	private int panEditBorder;

	public FileChoosingPanel(ApplicationController c) {
		controller = c;
		setLayout(new BorderLayout());
		JPanel paneLeft = new JPanel();
		FlowLayout layoutLeft = new FlowLayout(FlowLayout.LEADING);
		paneLeft.setLayout(layoutLeft);
		paneLeft.add(fileSelectedPanel);
		paneLeft.add(readFileStart);
		JPanel paneRight = new JPanel();
		FlowLayout layoutRight = new FlowLayout(FlowLayout.TRAILING);
		paneRight.setLayout(layoutRight);
		paneRight.add(pEditor);
		//resizeBorder();
		//pEditor.setBorder(BorderFactory.createEmptyBorder(0, panEditBorder, 0, 0));
		add(BorderLayout.WEST, paneLeft);
		add(BorderLayout.EAST, paneRight);
		readFileStart.addActionListener(this);
		initWSLogo();
	}
	public void initWSLogo() {
		try {
            wsLogo = ImageIO.read(getClass().getResourceAsStream("/wslogo-small.gif"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == readFileStart) {
			chooseFile();
		} 
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (ApplicationController.frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
			g.drawImage(wsLogo, 115, 0, wsLogo.getWidth(null) / 2, wsLogo.getHeight(null) / 2, null);
		}
		System.out.println(getHeight() + ", " + getWidth());
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
	public void resizeBorder() {
		panEditBorder = (int) ((ApplicationController.frame.getWidth() - MINIMUM_FRAME_WIDTH) / 2.5);
		System.out.println(ApplicationController.frame.getWidth() + ", " + MINIMUM_FRAME_WIDTH + ", " + panEditBorder);
		pEditor.setBorder(BorderFactory.createEmptyBorder(0, panEditBorder, 0, 0));
	}
}
