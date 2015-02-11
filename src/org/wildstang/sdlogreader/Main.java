package org.wildstang.sdlogreader;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.wildstang.sdlogreader.controllers.ApplicationController;

public class Main {

	private ApplicationController appController;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		appController = new ApplicationController();
		appController.initializeApplication();
	}
}
