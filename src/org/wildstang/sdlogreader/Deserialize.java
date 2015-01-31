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
	static List<Sensor> stored;
	static Map <String, Object> e;
	static boolean isDone = false;
	public static void deserial() {
			if (Main.logFile != null) {
			File file = Main.logFile;
			e = new HashMap <String, Object>();
		    try
		    {
		       FileInputStream fileIn = new FileInputStream(file);
		       ObjectInputStream in = new ObjectInputStream(fileIn);
		       e = (HashMap <String, Object>) in.readObject();
		       in.close();
		       fileIn.close();
		    }catch(IOException i)
		    {
		       i.printStackTrace();
		       return;
		    }catch(ClassNotFoundException c)
		    {
		       System.out.println("Employee class not found");
		       c.printStackTrace();
		       return;
		    }
		    System.out.println("Deserialized");
		    thisForLoop();
			}
			isDone = true;
	}
	public static void thisForLoop() {
		stored = new ArrayList<Sensor>();
		for (Map.Entry<String, Object> entry:e.entrySet()) {
	    	stored.add(new Sensor(entry.getKey(), entry.getValue() + ""));
	    	System.out.println(stored.size());
	    }
	}
}
