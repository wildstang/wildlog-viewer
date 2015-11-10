package org.wildstang.wildlog.viewer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.renderers.BooleanRenderer;
import org.wildstang.wildlog.viewer.renderers.DoubleRenderer;
import org.wildstang.wildlog.viewer.renderers.RendererFactory;
import org.wildstang.wildlog.viewer.renderers.StringRenderer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

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
