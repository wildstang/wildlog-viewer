package org.wildstang.wildlog.viewer;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;

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
