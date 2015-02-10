package org.wildstang.sdlogreader;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deserialize {
	static List<String> storedSensors = new ArrayList<String>();
	static List<Map<String, Object>> e = new ArrayList<Map<String, Object>>();
	static boolean isDone = false;
	static long startTimestamp, endTimestamp;

	public static void deserial() throws IOException, ClassNotFoundException {
		if (Main.logFile != null) {
			File file = Main.logFile;

			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
			while (true) {
				try {
					e.add((HashMap<String, Object>) oin.readObject());
				} catch (EOFException e) {
					// End of file. Break.
					break;
				} catch (ClassCastException e) {
					e.printStackTrace();
					continue;
				}
			}

			oin.close();

			for (int i = 0; i < e.size(); i++) {
				System.out.println("Map: " + i);
				for (Map.Entry<String, Object> entry : e.get(i).entrySet()) {
					System.out.println(entry.getKey() + " - " + entry.getValue());
				}
			}

			System.out.println("Deserialized");
			
			startTimestamp = (Long) e.get(0).get("Timestamp");
			endTimestamp = (Long) e.get(e.size() - 1).get("Timestamp");

			for (int i = 0; i < e.size(); i++) {
				Map<String, Object> map = e.get(i);
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if (!storedSensors.contains(entry.getKey())) {
						storedSensors.add(entry.getKey());
					}
				}
			}
			
			Collections.sort(storedSensors);
		}
		isDone = true;
	}
}
