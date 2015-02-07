package org.wildstang.sdlogreader;

import java.io.EOFException;
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
	static List<Map<String, Object>> e = new ArrayList<Map<String,Object>>();
	static boolean isDone = false;

	public static void deserial() throws IOException, ClassNotFoundException {
		if (Main.logFile != null) {
			File file = Main.logFile;
			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
			while (true) {
				try {
					e.add((HashMap<String, Object>) oin.readObject());
				} catch (EOFException e) {
					e.printStackTrace();
					break;
				} catch (ClassCastException e) {
					e.printStackTrace();
					continue;
				}
			}
			oin.close();
			for(int i = 0; i < e.size(); i++)
			{
				System.out.println("Map: " + i);
				for (Map.Entry<String, Object> entry : e.get(i).entrySet())
				{
					System.out.println(entry.getKey() + " - " + entry.getValue());
				}
			}
			System.out.println("Deserialized");
			stored = new ArrayList<List<Sensor>>();
			for (int i = 0; i < e.size(); i++) {
				Map<String, Object> map = e.get(i);
				List<Sensor> sensors = new ArrayList<>();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					sensors.add(new Sensor(entry.getKey(), "" + entry.getValue()));
					System.out.println(stored.size());
				}
				stored.add(sensors);
			}
		}
		isDone = true;
	}
}
