package com.wildstang.sd_card_reader;

import javax.swing.JFrame;

public class ControlBase {
	JFrame f;
	
	public ControlBase() {
		f = new JFrame("How's it going?");
		f.setSize(500,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	public static void main(String[] args) {
		new ControlBase();

	}

}
