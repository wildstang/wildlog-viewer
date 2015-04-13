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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class Main {

	private ApplicationController appController;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		/*
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test.json"), false));
			long startTime = System.currentTimeMillis();
			System.out.println("Beginning write");
			for (int i = 0; i < 1000000; i++) {
				JSONObject logObject = new JSONObject();
				logObject.put("time", System.currentTimeMillis());
				logObject.put("type", "int");
				if (i != 986790) {
					logObject.put("key", "this is a key");
				} else {
					logObject.put("key", "new key");
				}
				logObject.put("value", 1234563214);
				writer.write(logObject.toString());
				writer.newLine();
				if (i % 1000 == 0) {
					System.out.println("Writing object " + i);
				}
			}
			writer.flush();
			writer.close();
			System.out.println("Elapsed write time: " + (System.currentTimeMillis() - startTime));

			JsonFactory factory = new JsonFactory();
			List<String> foundTypes = new ArrayList<>();
			JsonParser parser = factory.createParser(new File("test.json"));
			startTime = System.currentTimeMillis();
			while (parser.nextToken() == JsonToken.START_OBJECT) {
				// Found new object
				while (parser.nextToken() != JsonToken.END_OBJECT) {
					if (parser.getCurrentName().equals("key")) {
						parser.nextToken();
						String key = parser.getText();
						if (!foundTypes.contains(key)) {
							foundTypes.add(key);
							System.out.println("Found new key! " + key);
						}
					}
				}
			}
			System.out.println("Elapsed find key time: " + (System.currentTimeMillis() - startTime));
			parser.close();

			BufferedReader reader = new BufferedReader(new FileReader(new File("test.json")));
			String line;
			startTime = System.currentTimeMillis();
			System.out.println("Beginning read");
			while ((line = reader.readLine()) != null) {
				JSONObject object = new JSONObject(line);
			}
			reader.close();
			System.out.println("Elapsed read time: " + (System.currentTimeMillis() - startTime));

		} catch (IOException e) {
			e.printStackTrace();
		}
*/
		appController = new ApplicationController();
		appController.initializeApplication();
	}
}
