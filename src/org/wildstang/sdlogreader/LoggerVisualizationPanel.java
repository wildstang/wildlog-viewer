package org.wildstang.sdlogreader;

import javax.swing.JPanel;


public class LoggerVisualizationPanel extends JPanel {
	
	
	public LoggerVisualizationPanel() {
		if (Main.logFile != null) {
			drawData();
		}
	}
	
	public void drawData() {
		/*for (int i = 0; i < Deserialize.e.size(); i++) {
			for (int j = 0; j < Deserialize.stored.size(); j++) {
				if (Deserialize.e.get(i).containsKey("test 5")) {
					Main.timeScroller.currentTime = Deserialize.stored.get(j).get(j).data.getText();
				}
			}
		}*/
		
			for (int e = 0; e < Deserialize.stored.size(); e++) {
				add(Deserialize.stored.get(e).get(e));
				System.out.println(Deserialize.stored.get(e));	
			}	
		Main.frame.pack();
	}
}