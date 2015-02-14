package org.wildstang.wildlog.models;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wildstang.wildlog.controllers.ApplicationController;

public class Deserializer {

	private static List<String> keys;
	private static List<Object> values;
	
	@SuppressWarnings("unchecked")
	public static LogsModel loadLogsModelFromFile(File file) throws IOException, ClassNotFoundException {
		if (file != null) {

			List<Map<String, Object>> logsList = new ArrayList<Map<String, Object>>();

			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
			while (true) {
				try {
					Object o = oin.readObject();
					if (o instanceof HashMap) {
						logsList.add((HashMap<String, Object>) o);
					}
				} catch (EOFException e) {
					// End of file. Break.
					break;
				} catch (ClassCastException e) {
					e.printStackTrace();
					continue;
				}
			}

			oin.close();

			// Extract list of all keys from the data
			keys = new ArrayList<>();
			values = new ArrayList<>();
			for (Map<String, Object> map : logsList) {
				for (String key : map.keySet()) {
					if (!keys.contains(key) && !key.equals("Timestamp")) {
						keys.add(key);
					}
				}
			}

			// Sort them nicely
			Collections.sort(keys);

			// Sort the list of logs by timestamp. They should come in order, but we do this to avoid weird behavior.
			Collections.sort(logsList, new DataPointsListComparator());

			// Convert data to DataPoints
			HashMap<String, List<DataPoint>> dataPoints = new HashMap<>();
			for (String key : keys) {
				boolean getFirst = false;
				for (Map<String, Object> map : logsList) {
					if (map.containsKey(key)) {
						long timestamp = (Long) map.get("Timestamp");
						Object value = map.get(key);
						if (dataPoints.get(key) == null) {
							dataPoints.put(key, new ArrayList<DataPoint>());
						}
						//System.out.println(key + new DataPoint(value, timestamp));
						dataPoints.get(key).add(new DataPoint(value, timestamp));
						if (!getFirst) {
							System.out.println(value);
							values.add(value);
							getFirst = true;
						}
					}
					
				}
			}

			// Get the first and last timestamps
			long startTimestamp = (Long) logsList.get(0).get("Timestamp");
			long endTimestamp = (Long) logsList.get(logsList.size() - 1).get("Timestamp");

			System.out.println("Deserialized");

			return new LogsModel(startTimestamp, endTimestamp, dataPoints, keys.toArray(new String[0]));
		}
		return null;
	}

	private static class DataPointsListComparator implements Comparator<Map<String, Object>> {

		@Override
		public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
			Long timestamp1 = (Long) arg0.get("Timestamp");
			Long timestamp2 = (Long) arg1.get("Timestamp");
			return timestamp1.compareTo(timestamp2);
		}

	}
	public static List<String> getKeyData() {
		return keys;
	}
	public static List<Object> getTypeData() {
		return values;
	}
}
