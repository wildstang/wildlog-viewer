package org.wildstang.sdlogreader;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SensorSelectPanel extends JPanel {
	JComboBox<String> keySelected;
	JComboBox<String> typeSelected;
	String[] keys;
	
	
	public SensorSelectPanel(Color color) {
		
		String[] types = {"", "Boolean", "Double", "String"};
		getKeys();
		typeSelected = new JComboBox(types);
		add(typeSelected);
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	public void getKeys() {
		if (Main.logFile != null) {
			
			keys = (String[]) Deserialize.storedSensors.toArray(new String[0]);
			String[] tempKeys = new String[keys.length + 1];
			System.arraycopy(keys, 0, tempKeys, 1, keys.length);
			tempKeys[0] = "Select a value";
			keys = tempKeys;
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
