package org.wildstang.sdlogreader;

import javax.swing.JPanel;


public class LoggerVisualizationPanel extends JPanel {
	
	
	public LoggerVisualizationPanel() {
		if (Main.logFile != null) {
			drawData();
		}
	}
	
	public void drawData() {
		for (int i = 0; i < Deserialize.stored.size(); i++) {
			add(Deserialize.stored.get(0).get(i));
			System.out.println(i);
		}
		Main.frame.pack();
	}
}