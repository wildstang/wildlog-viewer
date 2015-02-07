package org.wildstang.sdlogreader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main
{
	static JFrame frame;
	static FileChoosingPanel chooserPanel;
	static DataPanel dataPanel1;
	static DataPanel dataPanel2;
	static DataPanel dataPanel3;
	static DataPanel dataPanel4;
	static DataPanel dataPanel5;
	static DataPanel dataPanel6;
	static DataPanel dataPanel7;
	static DataPanel dataPanel8;
	static Scroller scrollBar;
	static File logFile;
	
	public static void main(String[] args)
	{
		new Main();
	}
	
	public Main()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		initFrameComponents();
		organizeFrameComponents();
	}
	
	private void initFrameComponents() {
		frame = new JFrame("WildStang: SD Log Reader");
		frame.setLocation(5, 5);
		chooserPanel = new FileChoosingPanel();
		dataPanel1 = new DataPanel(new Color(255, 0, 0));
		dataPanel2 = new DataPanel(new Color(255, 127, 0));
		dataPanel3 = new DataPanel(new Color(255, 255, 0));
		dataPanel4 = new DataPanel(new Color(0, 255, 0));
		dataPanel5 = new DataPanel(new Color(0, 127, 255));
		dataPanel6 = new DataPanel(new Color(0, 0 , 255));
		dataPanel7 = new DataPanel(new Color(127, 0, 255));
		dataPanel8 = new DataPanel(new Color(255, 0, 255));
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
		frame.add(dataPanel1, c);
		c.gridy = 2;
		frame.add(dataPanel2, c);
		c.gridy = 3;
		frame.add(dataPanel3, c);
		c.gridy = 4;
		frame.add(dataPanel4, c);
		c.gridy = 5;
		frame.add(dataPanel5, c);
		c.gridy = 6;
		frame.add(dataPanel6, c);
		c.gridy = 7;
		frame.add(dataPanel7, c);
		c.gridy = 8;
		frame.add(dataPanel8, c);
		c.gridy = 9;
		c.weighty = 0.0;
		frame.add(scrollBar, c);
		frame.pack();
		frame.setVisible(true);
	}
}
	