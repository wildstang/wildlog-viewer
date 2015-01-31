package org.wildstang.sdlogreader;

import java.util.List;
import javax.swing.JPanel;


public class LoggerVisualizationPanel extends JPanel {
	
	Main window;
	
	public LoggerVisualizationPanel(Main m) {
		window = m;
		if (Main.logFile != null) {
			drawData();
		}
	}
	public void drawData() {
		for (int i = 0; i < Deserialize.stored.size(); i++) {
			add(Deserialize.stored.get(i));
			System.out.println(i);
		}
		Main.frame.pack();
	}
}