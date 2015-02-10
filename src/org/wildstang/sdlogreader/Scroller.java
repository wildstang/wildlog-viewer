package org.wildstang.sdlogreader;

import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class Scroller extends JPanel implements AdjustmentListener {
	static JScrollBar scrollyPolly;
	
	public Scroller() {
		setLayout(new BorderLayout());
		scrollyPolly = new JScrollBar(JScrollBar.HORIZONTAL, 0, 10, 0, 110);
		add(scrollyPolly, BorderLayout.CENTER);
		scrollyPolly.addAdjustmentListener(this);
	}
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
		System.out.println(scrollyPolly.getValue());
		GraphingPanel.updateGraphs();
		
	}
	public static void scroll(double wheelMovement) {
		System.out.println(wheelMovement);
	}

}
