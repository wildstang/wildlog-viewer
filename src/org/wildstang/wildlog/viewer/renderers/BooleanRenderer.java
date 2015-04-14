package org.wildstang.wildlog.viewer.renderers;

import java.awt.Color;
import java.awt.Graphics;

import org.wildstang.wildlog.viewer.models.DataPoint;

public class BooleanRenderer extends LogRenderer {

	@Override
	public void renderLogs(Graphics g, int panelWidth, int panelHeight, long startTimestamp, long endTimestamp) {
		long deltaTime = endTimestamp - startTimestamp;
		int firstPointIndex = LogRenderer.findFirstPointBeforeTimestamp(dataPoints, startTimestamp);
		int lastPointIndex = LogRenderer.findFirstPointAfterTimestamp(dataPoints, endTimestamp);
		for (int i = firstPointIndex; i < lastPointIndex + 1; i++) {
			DataPoint point = dataPoints.get(i);
			DataPoint nextPoint = null;
			if (i < (dataPoints.size() - 1)) {
				nextPoint = dataPoints.get(i + 1);
			} else {
				return;
			}

			if (point.getObject() instanceof Boolean && nextPoint.getObject() instanceof Boolean) {
				int xVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) panelWidth));
				int width = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) panelWidth) - xVal);
				if (dataPoints.get(i).getObject().equals(true)) {
					g.setColor(Color.GREEN);
				} else {
					g.setColor(Color.RED);
				}
				g.fillRect(xVal, 0, width, panelHeight);
			}
		}
	}

	@Override
	public void renderDecorations(Graphics g, int panelWidth, int panelHeight, long startTimestamp, long endTimestamp, int mousePosition) {
		// TODO Auto-generated method stub
		
	}

}
