package org.wildstang.sdlogreader.views;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.wildstang.sdlogreader.models.LogsModel;

public class DataSelectPanel extends JPanel {

	private static final String[] DATA_TYPES = { "", "Boolean", "Double", "String" };

	JComboBox<String> keySelected;
	JComboBox<String> typeSelected;
	String[] keys;

	public DataSelectPanel(Color color) {
		keySelected = new JComboBox<>();
		add(keySelected);
		typeSelected = new JComboBox<>(DATA_TYPES);
		add(typeSelected);
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public void updateModel(LogsModel model) {

		keys = model.getAllKeys();
		String[] tempKeys = new String[keys.length + 1];
		System.arraycopy(keys, 0, tempKeys, 1, keys.length);
		tempKeys[0] = "Select a value";
		keys = tempKeys;

		keySelected.setModel(new DefaultComboBoxModel<>(keys));
	}
	public void clearAllFields() {
		keySelected.setSelectedIndex(0);
		typeSelected.setSelectedIndex(0);
	}
}
