package org.wildstang.sdlogreader;

import java.awt.*;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main
{
	static JFrame frame;
	static LoggerVisualizationPanel logPanel;
	static SelectedFilePanel fileSelectedPanel;
	static FileChoosingPanel chooserPanel;
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
		frame = new JFrame("WildStang: SD Log Reader");
		chooserPanel = new FileChoosingPanel(this);
		logPanel = new LoggerVisualizationPanel(this);
		fileSelectedPanel = new SelectedFilePanel(this);
		frame.setPreferredSize(new Dimension(500, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		frame.setLayout(layout);
		frame.add(BorderLayout.SOUTH, chooserPanel);
		frame.add(BorderLayout.NORTH, fileSelectedPanel);
		frame.add(BorderLayout.CENTER, logPanel);
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
	