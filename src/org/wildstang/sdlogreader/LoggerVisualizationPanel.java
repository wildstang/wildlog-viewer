package org.wildstang.sdlogreader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextField;


public class LoggerVisualizationPanel extends JPanel {
	
	Main window;
	Sensor sensor1;
	Sensor victor1;
	Sensor potentiometer;
	Sensor limitSwitch;
	
	public LoggerVisualizationPanel(Main m) {
		window = m;
		sensor1 = new Sensor("Sensor", 10, 1);
		victor1 = new Sensor("Victor", 10, 2);
		potentiometer = new Sensor("Pot", 10 , 3);
		limitSwitch = new Sensor("Limit Switch", 10, 4);
		this.add(sensor1);
		this.add(victor1);
		this.add(potentiometer);
		this.add(limitSwitch);
	}
	public void refreshData() {
		sensor1.data.setText("Data!");
		victor1.data.setText("true");
		potentiometer.data.setText("231");
		limitSwitch.data.setText("false");
	}
}
