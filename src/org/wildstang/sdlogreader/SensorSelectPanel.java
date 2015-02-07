package org.wildstang.sdlogreader;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SensorSelectPanel extends JPanel{
	JComboBox<String> keySelected;
	JComboBox<String> typeSelected;
	String[] keys;
	
	public SensorSelectPanel(Color color) {
		
		String[] types = {"", "Boolean", "Temp", "Voltage", "Motor"};
		getKeys();
		typeSelected = new JComboBox(types);
		add(typeSelected);
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
	}
	
	
	public void getKeys() {
		if (Main.logFile != null) {
			keys = (String[]) Deserialize.storedSensors.toArray(new String[0]);
		} else {
			keys = new String[1];
			keys[0] = "                  ";
		}
		if (keySelected == null) {
			keySelected = new JComboBox(keys);
			add(keySelected);
		}
		
		keySelected.setModel(new DefaultComboBoxModel<>(keys));
	}
}
