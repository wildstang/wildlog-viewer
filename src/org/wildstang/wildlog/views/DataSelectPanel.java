package org.wildstang.wildlog.views;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.wildstang.wildlog.models.LogsModel;

public class DataSelectPanel extends JPanel {
	
	private static final String DEFAULT_KEY = "Select Key";
	private static final String DEFAULT_DATA_TYPE = "Data Type";

	JComboBox<String> keySelected;
	JTextField typeOfKey;
	String[] keys = new String[1];        
	String[] tempKeys;
	

	public DataSelectPanel(Color color) {
		keys[0] = DEFAULT_KEY;
		keySelected = new JComboBox<>(keys);
		add(keySelected);
		typeOfKey = new JTextField(DEFAULT_DATA_TYPE, 8);
		typeOfKey.setEditable(false);
		add(typeOfKey);
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public void updateModel(LogsModel model) {
		keys = model.getAllKeys();
		tempKeys = new String[keys.length + 1];
		System.arraycopy(keys, 0, tempKeys, 1, keys.length);
		tempKeys[0] = "SelectKey";
		keys = tempKeys;
		keys[0] = DEFAULT_KEY;
		keySelected.setModel(new DefaultComboBoxModel<>(keys));
	}
	
	public void setDataTypeText(String text) {
		typeOfKey.setText(text);
	}
	
	public void clearAllFields() {
		keySelected.setSelectedIndex(0);
		setDataTypeText(DEFAULT_DATA_TYPE);
	}
}
