package org.wildstang.wildlog;

import org.wildstang.wildlog.controllers.ApplicationController;

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
