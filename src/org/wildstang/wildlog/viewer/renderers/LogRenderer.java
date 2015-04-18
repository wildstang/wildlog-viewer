package org.wildstang.wildlog.viewer.renderers;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

import org.wildstang.wildlog.viewer.models.DataPoint;

public abstract class LogRenderer {

	protected List<DataPoint> dataPoints;
	protected DataPoint[] dataPointsArray;

	protected Color accentColor;

	public void updateLogModel(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
		// Cache the data points as an array for searching
		this.dataPointsArray = dataPoints.toArray(new DataPoint[dataPoints.size()]);
	}

	public void updateAccentColor(Color color) {
		this.accentColor = color;
	}

	public abstract void renderLogs(Graphics g, int panelWidth, int panelHeight, long startTimestamp, long endTimestamp);

	public abstract void renderDecorations(Graphics g, int panelWidth, int panelHeight, long startTimestamp, long endTimestamp, int mousePosition);

	protected int findFirstPointBeforeTimestamp(long startTimestamp) {
		// If the timestamp of the first element in the array is greater than the target, return 0
		if (dataPointsArray[0].getTimeStamp() >= startTimestamp) {
			return 0;
		}

		// Use binary search algoritm for efficiency
		int low = 0;
		int high = dataPointsArray.length;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			long midVal = dataPointsArray[mid].getTimeStamp();

			if (midVal < startTimestamp)
				low = mid + 1;
			else if (midVal > startTimestamp)
				high = mid - 1;
			else
				// target timestamp found
				// return the position one below this
				return (mid - 1 < 0 ? 0 : mid - 1);
		}

		// Don't return a negative index
		return (low - 1 < 0 ? 0 : low - 1);
	}

	protected int findFirstPointAfterTimestamp(long endTimestamp) {
		// If the timestamp of the last element in the array is less than the target, return the last index
		if (dataPointsArray[dataPoints.size() - 1].getTimeStamp() <= endTimestamp) {
			return dataPoints.size() - 1;
		}
		// Use binary search algoritm for efficiency
		int low = 0;
		int high = dataPointsArray.length;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			long midVal = dataPointsArray[mid].getTimeStamp();

			if (midVal < endTimestamp)
				low = mid + 1;
			else if (midVal > endTimestamp)
				high = mid - 1;
			else
				// target timestamp found
				// return the position one below this
				return (mid + 1 < 0 ? 0 : mid - 1);
		}

		// Don't return an index greater than the max index of the array
		return (high + 1 > (dataPointsArray.length - 1) ? dataPointsArray.length - 1 : high + 1);
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
