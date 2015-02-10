package org.wildstang.sdlogreader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class DataPanel extends JPanel implements ActionListener, MouseMotionListener {
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
		sPane.keySelected.addActionListener(this);
		add(sPane, j);

		j.gridx = 1;
		j.gridy = 0;
		j.fill = GridBagConstraints.BOTH;
		j.weightx = 1;
		j.weighty = 1.0;
		gPane = new GraphingPanel();
		add(gPane, j);
		
		gPane.addMouseMotionListener(this);
	}
	public void update(Graphics g) {
		System.out.println("in Data Panel update method");
		g.setColor(Color.BLACK);
		g.drawLine(gPane.mouseX, 0, gPane.mouseX, gPane.getHeight() * 8);
		g.drawString(Long.toString(gPane.firstTimestamp + gPane.mouseX), gPane.mouseX - 25, getHeight() / 2);
	}
	public void actionPerformed(ActionEvent e) {

		System.out.println("actionPerformed");
		if (e.getSource() == sPane.typeSelected) {
			switch (sPane.typeSelected.getSelectedIndex()) {
			case 0:
				gPane.setType(GraphingPanel.DEFAULT_TYPE);
				break;
			case 1:
				System.out.println("case1");
				gPane.setType(GraphingPanel.BOOL_TYPE);
				break;
			case 2:
				gPane.setType(GraphingPanel.DOUBLE_TYPE);
				break;
			case 3:
				gPane.setType(GraphingPanel.STRING_TYPE);
				break;
			}
		} else if (e.getSource() == sPane.keySelected) {
			gPane.setDataKey((String) sPane.keySelected.getSelectedItem());
		}

	}
	

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Main.updateMousePosition(e.getX(), e.getY());
	}
	
	public void updateMousePosition(int posX, int posY) {
		gPane.setMousePosition(posX, posY);
	}
	
	public void updateGraphView(long startTimestamp, long endTimestamp) {
		gPane.updateGraphView(startTimestamp, endTimestamp);
	}
}
