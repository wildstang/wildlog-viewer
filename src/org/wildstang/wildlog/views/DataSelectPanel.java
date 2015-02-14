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

	//private static final String[] DATA_TYPES = { "Select Type", "Boolean", "Double", "String" };

	JComboBox<String> keySelected;
	//JComboBox<String> typeSelected;
	JTextField typeOfKey;
	String[] keys = new String[1];
	String[] tempKeys;
	private static final String defaultKey = "Select Key               ";
	private static final String defaultType = "Type of Key";

	public DataSelectPanel(Color color) {
		keys[0] = defaultKey;
		keySelected = new JComboBox<>(keys);
		add(keySelected);
		typeOfKey = new JTextField(defaultType, 8);
		typeOfKey.setEditable(false);
		add(typeOfKey);
		//typeSelected = new JComboBox<>(DATA_TYPES);
		//add(typeSelected);
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public void updateModel(LogsModel model) {

		keys = model.getAllKeys();
		tempKeys = new String[keys.length + 1];
		System.arraycopy(keys, 0, tempKeys, 1, keys.length);
		tempKeys[0] = "SelectKey";
		keys = tempKeys;
		keys[0] = defaultKey;
		keySelected.setModel(new DefaultComboBoxModel<>(keys));
	}
	public void settingText(String text) {
		typeOfKey.setText(text);
	}
	public void clearAllFields() {
		keySelected.setSelectedIndex(0);
		typeOfKey.setText(defaultType);
		//typeSelected.setSelectedIndex(0);
	}
}
