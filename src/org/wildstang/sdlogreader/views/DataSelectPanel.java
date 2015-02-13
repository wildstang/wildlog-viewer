package org.wildstang.sdlogreader.views;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.wildstang.sdlogreader.models.LogsModel;

public class DataSelectPanel extends JPanel {

	private static final String[] DATA_TYPES = { "Select Type", "Boolean", "Double", "String" };

	JComboBox<String> keySelected;
	JComboBox<String> typeSelected;
	String[] keys = new String[1];
	String[] tempKeys;
	private static final String defaultKey = "Select key     ";
	
	
	public DataSelectPanel(Color color) {
		keys[0] = defaultKey;
		keySelected = new JComboBox<>(keys);
		add(keySelected);
		typeSelected = new JComboBox<>(DATA_TYPES);
		add(typeSelected);
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public void updateModel(LogsModel model) {

		keys = model.getAllKeys();
		tempKeys = new String[keys.length + 1];
		System.arraycopy(keys, 0, tempKeys, 1, keys.length);
		tempKeys[0] = "SelectKey";
		keys = tempKeys;
		keys[0] = "Select Key";
		keySelected.setModel(new DefaultComboBoxModel<>(keys));
	}
	public void clearAllFields() {
		keySelected.setSelectedIndex(0);
		typeSelected.setSelectedIndex(0);
	}
}
