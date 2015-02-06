package org.wildstang.sdlogreader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deserialize {
	static List<List<Sensor>> stored;
	static List<Map <String, Object>> e;
	static boolean isDone = false;
	
	public static void deserial() throws IOException, ClassNotFoundException {
		if (Main.logFile != null) {
			File file = Main.logFile;
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine()) != null)
			{
			       ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(line.getBytes()));
			       e.add((HashMap <String, Object>) in.readObject());
			       in.close();
			}
		    System.out.println("Deserialized");
			stored = new ArrayList<List<Sensor>>();
			for(int i = 0; i < e.size(); i++)
			{
				Map<String, Object> map = e.get(i);
				List<Sensor> sensors = new ArrayList<>();
				for (Map.Entry<String, Object> entry : map.entrySet())
				{
			    	sensors.add(new Sensor(entry.getKey(), entry.getValue() + ""));
			    	System.out.println(stored.size());
			    }
				stored.add(sensors);
			}
		}
		isDone = true;
	}
}
