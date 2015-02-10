package org.wildstang.sdlogreader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main implements MouseWheelListener{
	private static final int NUM_DATA_PANELS = 8;
	static JFrame frame;
	static FileChoosingPanel chooserPanel;
	 
	static DataPanel[] dataPanels = new DataPanel[NUM_DATA_PANELS];
	static Scroller scrollBar;
	static File logFile;
	
	// Controllers
	GraphPanelViewController gpViewController;
	
	private static Main instance;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		initFrameComponents();
		organizeFrameComponents();
		gpViewController = new GraphPanelViewController(dataPanels);
		frame.addMouseWheelListener(gpViewController);
	}

	private void initFrameComponents() {
		frame = new JFrame("WildStang: SD Log Reader");
		frame.setLocation(5, 5);
		chooserPanel = new FileChoosingPanel();
		dataPanels[0] = new DataPanel(new Color(255, 0, 0));
		dataPanels[1] = new DataPanel(new Color(255, 127, 0));
		dataPanels[2] = new DataPanel(new Color(255, 255, 0));
		dataPanels[3] = new DataPanel(new Color(0, 255, 0));
		dataPanels[4] = new DataPanel(new Color(0, 127, 255));
		dataPanels[5] = new DataPanel(new Color(0, 0, 255));
		dataPanels[6] = new DataPanel(new Color(127, 0, 255));
		dataPanels[7] = new DataPanel(new Color(255, 0, 255));
		scrollBar = new Scroller();
		scrollBar.setPreferredSize(new Dimension(1000, 40));

		frame.setPreferredSize(new Dimension(1000, 702));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void organizeFrameComponents() {
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		frame.add(chooserPanel, c);
		c.gridy = 1;
		c.weighty = 1.0;
		frame.add(dataPanels[0], c);
		c.gridy = 2;
		frame.add(dataPanels[1], c);
		c.gridy = 3;
		frame.add(dataPanels[2], c);
		c.gridy = 4;
		frame.add(dataPanels[3], c);
		c.gridy = 5;
		frame.add(dataPanels[4], c);
		c.gridy = 6;
		frame.add(dataPanels[5], c);
		c.gridy = 7;
		frame.add(dataPanels[6], c);
		c.gridy = 8;
		frame.add(dataPanels[7], c);
		c.gridy = 9;
		c.weighty = 0.0;
		frame.add(scrollBar, c);
		frame.pack();
		frame.setVisible(true);
	}

	public static void updateMousePosition(int x, int y) {
		System.out.println("updateMousePosition");
		for (int i = 0; i < dataPanels.length; i++) {
			dataPanels[i].updateMousePosition(x, y);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		e.getPreciseWheelRotation();
	}
}
