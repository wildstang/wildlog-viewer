package org.wildstang.sdlogreader.models;

public class DataPoint {

	private Object storedObject;
	private long timestamp;

	public DataPoint(Object o, long timestamp) {
		storedObject = o;
		this.timestamp = timestamp;
	}

	public Object getObject() {
		return storedObject;
	}

	public long getTimeStamp() {
		return timestamp;
	}
}
