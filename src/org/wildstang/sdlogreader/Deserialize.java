package org.wildstang.sdlogreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class Deserialize {
	static boolean isDone = false;
	public static void deserial() {
			if (Main.logFile != null) {
			File file = Main.logFile;
			Map <String, Object> e = new HashMap <String, Object>();
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
		    for (Map.Entry<String, Object> entry:e.entrySet()) {
		    	System.out.println(entry.getKey() + ": " + entry.getValue());
		    }
			}
			isDone = true;
	}
	
}
