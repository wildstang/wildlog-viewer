package org.wildstang.wildlog.viewer.renderers;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import org.wildstang.wildlog.viewer.models.DataPoint;

public abstract class LogRenderer {
	
	protected List<DataPoint> dataPoints;
	
	protected Color accentColor;
	
	public void updateLogModel(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}
	
	public void updateAccentColor(Color color) {
		this.accentColor = color;
	}
	
	public abstract void renderLogs(Graphics g, int panelWidth, int panelHeight, long startTimestamp, long endTimestamp);
	
	public abstract void renderDecorations(Graphics g, int panelWidth, int panelHeight, long startTimestamp, long endTimestamp, int mousePosition);
	

	public static int findFirstPointBeforeTimestamp(List<DataPoint> dataPoints, long startTimestamp) {
		// Find the index of the first data point prior to the start timestamp
		int firstPointIndex = -1;
		for (int i = 0; i < dataPoints.size(); i++) {
			long timestamp = dataPoints.get(i).getTimeStamp();
			if (timestamp > startTimestamp) {
				firstPointIndex = i - 1;
				if (firstPointIndex < 0) {
					firstPointIndex = 0;
				}
				break;
			}
		}

		return firstPointIndex;
	}

	public static int findFirstPointAfterTimestamp(List<DataPoint> dataPoints, long endTimestamp) {
		/*
		 * If the last timestamp in our data set is less than the target end timestamp, use the last timestamp.
		 * Otherwise, search through the data for the first timestamp beyond the end timestamp.
		 */
		int lastPointIndex = -1;
		if (dataPoints.get(dataPoints.size() - 1).getTimeStamp() <= endTimestamp) {
			lastPointIndex = dataPoints.size() - 1;
		} else {
			for (int i = 0; i < dataPoints.size(); i++) {
				long timestamp = dataPoints.get(i).getTimeStamp();
				if (timestamp >= endTimestamp) {
					lastPointIndex = i;
					if (lastPointIndex > (dataPoints.size() - 1)) {
						lastPointIndex = dataPoints.size() - 1;
					}
					break;
				}
			}
		}

		return lastPointIndex;
	}
	
	public static int findIndexOfClosestPointToTimestamp(List<DataPoint> dataPoints, long targetTimestamp) {
		int indexOfClosest = 0;
		double distance = Math.abs(dataPoints.get(0).getTimeStamp() - targetTimestamp);
		for (int i = 0; i < dataPoints.size(); i++) {
			double newdist = Math.abs(dataPoints.get(i).getTimeStamp() - targetTimestamp);
			if (newdist < distance) {
				distance = newdist;
				indexOfClosest = i;
			}
		}
		return indexOfClosest;
	}
	
	public static long mapPointInRangeToTimestamp(long startTimestamp, long endTimestamp, long startRange, long endRange, long positionInRange) {
		return (long) (startTimestamp + ((double) (positionInRange - startRange) / (double) (endRange - startRange) * (endTimestamp - startTimestamp)));
	}

}
