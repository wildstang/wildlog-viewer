package org.wildstang.sdlogreader;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Sensor extends JPanel {
	JTextField data;
	JLabel sensor;
	
	public Sensor(String name, String text) {
		sensor = new JLabel(name);
		data = new JTextField(text, 10);
		this.add(sensor);
		this.add(data);
	}
}
