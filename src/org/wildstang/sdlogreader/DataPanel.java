package org.wildstang.sdlogreader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DataPanel extends JPanel implements ActionListener {
	GraphingPanel gPane;
	SensorSelectPanel sPane;

	public DataPanel(Color c) {
		setLayout(new GridBagLayout());
		GridBagConstraints j = new GridBagConstraints();
		j.gridx = 0;
		j.gridy = 0;
		j.fill = GridBagConstraints.VERTICAL;
		j.anchor = GridBagConstraints.LINE_START;
		j.weightx = 0.0;
		j.weighty = 1.0;
		sPane = new SensorSelectPanel(c);
		sPane.setPreferredSize(new Dimension(200, 500));
		sPane.typeSelected.addActionListener(this);
		add(sPane, j);

		j.gridx = 1;
		j.gridy = 0;
		j.fill = GridBagConstraints.BOTH;
		j.weightx = 1;
		j.weighty = 1.0;
		gPane = new GraphingPanel(0);
		add(gPane, j);

	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed");
		switch (sPane.typeSelected.getSelectedIndex()) {
		case 1:
			System.out.println("case1");
			gPane = new GraphingPanel(1);
			break;
		case 2:
			gPane = new GraphingPanel(2);
			break;
		case 3:
			gPane = new GraphingPanel(3);
			break;
		}
	}
}
