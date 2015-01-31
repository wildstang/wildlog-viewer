package org.wildstang.sdlogreader;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Sensor extends JPanel {
	JTextField data;
	JLabel sensor;
	int sensorKey;
	
	public Sensor(String name, int size, int index) {
		sensor = new JLabel(name);
		data = new JTextField(size);
		sensorKey = index;
		this.add(sensor);
		this.add(data);
	}
}
