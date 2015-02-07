package org.wildstang.sdlogreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deserialize {
	static List<List<Sensor>> stored;
	static List<String> storedSensors;
	static List<String> storedValues;
	static List<Map<String, Object>> e;
	static boolean isDone = false;

	public static void deserial() {
		if (Main.logFile != null) {
			File file = Main.logFile;
			e = new ArrayList<>();
			try {
				FileInputStream fileIn = new FileInputStream(file);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				e.add((HashMap<String, Object>) in.readObject());
				in.close();
				fileIn.close();
			} catch (IOException i) {
				i.printStackTrace();
				return;
			} catch (ClassNotFoundException c) {
				System.out.println("Log file not found!");
				c.printStackTrace();
				return;
			}
			System.out.println("Deserialized");

			stored = new ArrayList<List<Sensor>>();
			storedSensors = new ArrayList<String>();
			storedValues = new ArrayList<String>();
			for (int i = 0; i < e.size(); i++) {
				Map<String, Object> map = e.get(i);
				List<Sensor> sensors = new ArrayList<>();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					storedSensors.add(entry.getKey());
					storedValues.add(entry.getValue() + "");
					sensors.add(new Sensor(storedSensors.get(i), storedValues
							.get(i)));
					System.out.println(stored.size());
				}
				stored.add(sensors);
			}
		}
		isDone = true;
	}
}
