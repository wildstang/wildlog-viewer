package org.wildstang.sdlogreader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main
{
	static JFrame frame;
	static LoggerVisualizationPanel logPanel;
	static FileChoosingPanel chooserPanel;
	static ScrollButtonPanel timeScroller;
	static SensorSelectPanel sensorPanel1;
	static SensorSelectPanel sensorPanel2;
	static SensorSelectPanel sensorPanel3;
	static SensorSelectPanel sensorPanel4;
	static SensorSelectPanel sensorPanel5;
	static SensorSelectPanel sensorPanel6;
	static SensorSelectPanel sensorPanel7;
	static SensorSelectPanel sensorPanel8;
	static JScrollBar scrollypolly = new JScrollBar();
	static File logFile;
	
	public static void main(String[] args)
	{
		new Main();
	}
	
	public Main()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setLookAndFeel();
		//initialize the JComponents
		frame = new JFrame("WildStang: SD Log Reader");
		chooserPanel = new FileChoosingPanel();
		logPanel = new LoggerVisualizationPanel();
		timeScroller = new ScrollButtonPanel();
		sensorPanel1 = new SensorSelectPanel(new Color(255, 0, 0));
		sensorPanel2 = new SensorSelectPanel(new Color(255, 127, 0));
		sensorPanel3 = new SensorSelectPanel(new Color(255, 255, 0));
		sensorPanel4 = new SensorSelectPanel(new Color(0, 255, 0));
		sensorPanel5 = new SensorSelectPanel(new Color(0, 127, 255));
		sensorPanel6 = new SensorSelectPanel(new Color(0, 0 , 255));
		sensorPanel7 = new SensorSelectPanel(new Color(127, 0, 255));
		sensorPanel8 = new SensorSelectPanel(new Color(255, 0, 255));
		JPanel blankPanel = new JPanel();
		blankPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel blankPanel1 = new JPanel();
		blankPanel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel blankPanel2 = new JPanel();
		blankPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel blankPanel3 = new JPanel();
		blankPanel3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel blankPanel4 = new JPanel();
		blankPanel4.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel blankPanel5 = new JPanel();
		blankPanel5.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel blankPanel6 = new JPanel();
		blankPanel6.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel blankPanel7 = new JPanel();
		blankPanel7.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		frame.setPreferredSize(new Dimension(1000, 695));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(BorderLayout.NORTH, chooserPanel);
		JPanel sidePart = new JPanel();
		GridLayout sideLayout = new GridLayout(8,1);
		sidePart.setLayout(sideLayout);
		sidePart.add(sensorPanel1);
		sidePart.add(sensorPanel2);
		sidePart.add(sensorPanel3);
		sidePart.add(sensorPanel4);
		sidePart.add(sensorPanel5);
		sidePart.add(sensorPanel6);
		sidePart.add(sensorPanel7);
		sidePart.add(sensorPanel8);
		frame.add(BorderLayout.LINE_START, sidePart);
		JPanel pane = new JPanel();
		pane.setLayout(sideLayout);
		pane.add(blankPanel);
		pane.add(blankPanel1);
		pane.add(blankPanel2);
		pane.add(blankPanel3);
		pane.add(blankPanel4);
		pane.add(blankPanel5);
		pane.add(blankPanel6);
		pane.add(blankPanel7);
		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
	}
	
	private static void setLookAndFeel() { 
        try {
            UIManager.setLookAndFeel(
                "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"
            );
        } catch (Exception exc){
            
        }
    }
}
	