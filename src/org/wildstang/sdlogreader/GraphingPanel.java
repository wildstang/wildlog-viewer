package org.wildstang.sdlogreader;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GraphingPanel extends JPanel{
	public GraphingPanel(int type) {
		switch (type){
			case 1:
				booleanPanel();
				break;
			case 2:
				doublePanel();
				break;
			case 3:
				stringPanel();
				break;
			default:
				break;
		}
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	public void booleanPanel() {
		System.out.println("In booleanPanel");
		this.setBackground(Color.GREEN);
	}
	public void doublePanel() {
		
	}
	public void stringPanel() {
		
	}
}
