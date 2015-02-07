package org.wildstang.sdlogreader;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class LoggerVisualizationPanel extends JPanel {
	
	
	public LoggerVisualizationPanel() {
		if (Main.logFile != null) {
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
	}
	
}