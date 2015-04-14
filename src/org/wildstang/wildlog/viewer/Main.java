package org.wildstang.wildlog.viewer;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.renderers.BooleanRenderer;
import org.wildstang.wildlog.viewer.renderers.DoubleRenderer;
import org.wildstang.wildlog.viewer.renderers.RendererFactory;
import org.wildstang.wildlog.viewer.renderers.StringRenderer;

public class Main {

	private ApplicationController appController;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		// Register LogRenderers with the RendererFactory first
		// These will be used when rendering data
		RendererFactory.registerRenderer(Boolean.class, BooleanRenderer.class);
		RendererFactory.registerRenderer(Double.class, DoubleRenderer.class);
		RendererFactory.registerRenderer(String.class, StringRenderer.class);
		
		appController = new ApplicationController();
		appController.initializeApplication();
	}
}
