package org.wildstang.sdlogreader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScrollButtonPanel extends JPanel implements ActionListener {

	JButton rightButton;
	JButton leftButton;
	Sensor time;
	String currentTime;
	
	public ScrollButtonPanel() {
		rightButton = new JButton(">");
		leftButton = new JButton("<");
		time = new Sensor("Time: ", currentTime);
		this.add(leftButton);
		this.add(time);
		this.add(rightButton);
		rightButton.addActionListener(this);
		leftButton.addActionListener(this);
		time.data.setEditable(false);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == leftButton) {
			System.out.println("Received Button Press From Left Arrow Button");
		} else {
			System.out.println("Received Button Press From Right Arrow Button");
		}
		//Main.logPanel.drawData();
		updateTime();
	}
	public void updateTime() {
		time.data.setText(currentTime);
	}
}
