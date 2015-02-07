package org.wildstang.sdlogreader;

public class DataPoint {
	
	Object storedObject;
	long timestamp;
	
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
