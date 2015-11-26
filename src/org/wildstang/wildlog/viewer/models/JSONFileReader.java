package org.wildstang.wildlog.viewer.models;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONFileReader {

	@SuppressWarnings("unchecked")
	public static LogsModel loadLogsModelFromFile(File file) throws IOException 
	{
		// Get the first and last timestamps
		long startTimestamp = 0;
		long endTimestamp = 0;

		if (file != null) 
		{

			try {

				FileReader reader = new FileReader(file);
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
				
				JSONArray ioInfo = (JSONArray)jsonObject.get("ioinfo");
				
				JSONArray state = (JSONArray)jsonObject.get("state");
				
				// Extract list of all keys from the data
				Iterator<JSONObject> ioIter = ioInfo.iterator();
				List<String> keys = new ArrayList<>();
				LinkedHashMap<String, IOInfo> ioInfoList = new LinkedHashMap<String, IOInfo>();
				
				while (ioIter.hasNext())
				{
					JSONObject currentInfo = ioIter.next();
					String name = (String)currentInfo.get("name");
					keys.add(name);
					String dirStr = (String)currentInfo.get("direction");
					boolean dir = false;
					if (dirStr.equalsIgnoreCase("input"))
					{
					   dir = true;
					}
					ioInfoList.put(name, new IOInfo((String)currentInfo.get("name"), (String)currentInfo.get("type"), dir));
				}

				// Sort them nicely
				Collections.sort(keys);

				// Convert data to DataPoints
				// for each timestamp key, get the object name and value
				// lookup by name as it does here, and create a new datapoint with the timestamp and value
				HashMap<String, List<DataPoint>> dataPoints = new HashMap<>();
				Iterator<JSONObject> stateIter = state.iterator();
				boolean first = true;
				
				while (stateIter.hasNext())
				{
					JSONObject currentState = stateIter.next();
					JSONArray values = (JSONArray)currentState.get("values");
					
					long timestamp = Long.parseLong((String)currentState.get("timestamp"));
					
					if (first)
					{
						startTimestamp = timestamp;
						first = false;
					}
					
					for (int i = 0; i < values.size(); i++)
					{
						JSONObject iostate = (JSONObject)values.get(i);
						String name = (String)iostate.get("name");
						String value = (String)iostate.get("value");

						if (dataPoints.get(name) == null)
						{
							dataPoints.put(name, new ArrayList<DataPoint>());
						}
						if (value.equalsIgnoreCase("true"))
						{
							dataPoints.get(name).add(new DataPoint(true, timestamp - startTimestamp));
						}
						else if (value.equalsIgnoreCase("false"))
						{
							dataPoints.get(name).add(new DataPoint(false, timestamp - startTimestamp));
						}
						else
						{
							double doubleValue = Double.parseDouble(value);
							dataPoints.get(name).add(new DataPoint(doubleValue, timestamp - startTimestamp));
						}
					}
					
					if (!stateIter.hasNext())
					{
						endTimestamp = timestamp;
					}
				}

				return new LogsModel(startTimestamp - startTimestamp, endTimestamp - startTimestamp, dataPoints, ioInfoList);
			}
			catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
