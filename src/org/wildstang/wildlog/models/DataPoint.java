package org.wildstang.wildlog.models;

public class DataPoint {

	private Object value;
	private long timestamp;

	public DataPoint(Object o, long timestamp) {
		value = o;
		this.timestamp = timestamp;
	}

	public Object getObject() {
		return value;
	}

	public long getTimeStamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "[Timestamp=" + timestamp + "; Value=" + value + "]";
	}
}
