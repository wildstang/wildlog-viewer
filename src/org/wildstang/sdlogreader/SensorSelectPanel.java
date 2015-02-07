package org.wildstang.sdlogreader;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SensorSelectPanel extends JPanel{
	JComboBox keySelected;
	JComboBox typeSelected;
	String[] keys = {"", "TestKeys", "LeftMotor", "Blahblah"};
	
	public SensorSelectPanel() {
		
		String[] types = {"", "Boolean", "Temp", "Voltage", "Motor"};
		typeSelected = new JComboBox(types);
		keySelected = new JComboBox(keys);
		add(keySelected);
		add(typeSelected);
		setBorder(BorderFactory.createLineBorder(Color.black));
		
	}
	
	
	public void getKeys() {
		List<String> keyList = new ArrayList<String>();
		for (int i = 1; i < Deserialize.stored.size(); i++ ) {
			keyList.add(Deserialize.stored.get(i).toString());
		} 
		keySelected = new JComboBox(keyList.toArray(keys));
		add(keySelected);
	}
}
