package org.wildstang.sdlogreader;

import java.util.List;

import javax.swing.JPanel;

public class LoggerVisualizationPanel extends JPanel {

	public LoggerVisualizationPanel() {
		if (Main.logFile != null) {
			drawData();
		}
	}

	public void drawData() {
		for (int i = 0; i < Deserialize.stored.size(); i++) {
			List<Sensor> currentList = Deserialize.stored.get(i);
			System.out.println(i);
			for (int j = 0; j < currentList.size(); j++) {
				add(currentList.get(j));
				System.out.println(j);
			}
		}
		Main.frame.pack();
	}
}