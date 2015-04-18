package org.wildstang.wildlog.viewer.views;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.models.LogsModel;

public class DataSelectPanel extends JPanel {

	private static final String DEFAULT_KEY = "Select Key";
	private static final String DEFAULT_DATA_TYPE = "Data Type";

	JComboBox<String> dataKeySelector;
	JTextField dataKeyType;
	String[] keys = new String[1];
	String[] tempKeys;

	public DataSelectPanel(Color color) {
		keys[0] = DEFAULT_KEY;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		dataKeySelector = new JComboBox<>(keys);
		dataKeySelector.setMaximumSize(new Dimension(10000, 20));
		add(dataKeySelector);
		dataKeyType = new JTextField(DEFAULT_DATA_TYPE, 8);
		dataKeyType.setEditable(false);
		dataKeyType.setMaximumSize(new Dimension(10000, 20));
		add(dataKeyType);
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setMinimumSize(new Dimension(ApplicationController.DATA_SELECT_PANEL_WIDTH, 0));
		setMaximumSize(new Dimension(ApplicationController.DATA_SELECT_PANEL_WIDTH, 0));
	}

	public void updateModel(LogsModel model) {
		keys = model.getAllKeys();
		tempKeys = new String[keys.length + 1];
		System.arraycopy(keys, 0, tempKeys, 1, keys.length);
		tempKeys[0] = "SelectKey";
		keys = tempKeys;
		keys[0] = DEFAULT_KEY;
		dataKeySelector.setModel(new DefaultComboBoxModel<>(keys));
	}

	public void setDataTypeText(String text) {
		dataKeyType.setText(text);
	}

	public void clearAllFields() {
		dataKeySelector.setSelectedIndex(0);
		setDataTypeText(DEFAULT_DATA_TYPE);
	}
}
