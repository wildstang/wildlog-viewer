package org.wildstang.sdlogreader;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class GraphingPanel extends JPanel{
	
	public final static int BOOL_TYPE = 1;
	public final static int DOUBLE_TYPE = 2;
	public final static int STRING_TYPE = 3;
	public final static int DEFAULT_TYPE = 0;
	public GraphingPanel() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setOpaque(true);
	}
	
	
	public void booleanPanel() {
		System.out.println("In booleanPanel");
		setBackground(Color.GREEN);
		
		/*invalidate();
		repaint();
		revalidate();
		Main.frame.pack();*/
	}
	public void doublePanel() {
		
	}
	public void stringPanel() {
		
	}
	public void clearPanel() {
		setBackground(UIManager.getColor("Panel.background"));
	}
	public static void updateGraphs() {
		
	}
	public void setType(int type){
		switch (type){
		case BOOL_TYPE:
			booleanPanel();
			break;
		case DOUBLE_TYPE:
			doublePanel();
			break;
		case STRING_TYPE:
			stringPanel();
			break;
		default:
			clearPanel();
			break;
		}
	}
}
