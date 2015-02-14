package org.wildstang.wildlog.models;

import java.util.List;
import java.util.Map;

public class LogsModel {

	private long startTimestamp, endTimestamp;
	private Map<String, List<DataPoint>> data;
	private String[] keys;

	public LogsModel(long startTimestamp, long endTimestamp, Map<String, List<DataPoint>> data, String[] allKeys) {
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.data = data;
		this.keys = allKeys;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public List<DataPoint> getDataPointsForKey(String key) {
		return data.get(key);
	}

	public String[] getAllKeys() {
		return keys;
	}

}
