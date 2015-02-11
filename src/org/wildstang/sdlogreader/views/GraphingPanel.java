package org.wildstang.sdlogreader.views;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.wildstang.sdlogreader.models.DataPoint;
import org.wildstang.sdlogreader.models.LogsModel;

public class GraphingPanel extends JPanel {

	private LogsModel model;
	private String logKey;

	public final static int BOOL_TYPE = 1;
	public final static int DOUBLE_TYPE = 2;
	public final static int STRING_TYPE = 3;
	public final static int DEFAULT_TYPE = 0;

	List<DataPoint> dataPoints;
	private int graphType = -1;
	long firstTimestamp;
	int mouseX;
	int mouseY;
	long viewStartTimestamp = -1, viewEndTimestamp = -1;
	Graphics graphics;

	public GraphingPanel() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		graphics = g;

		if (model == null) {
			return;
		}

		long startTimestamp, endTimestamp;
		if (viewStartTimestamp == -1 || viewEndTimestamp == -1) {
			startTimestamp = model.getStartTimestamp();
			endTimestamp = model.getEndTimestamp();
		} else {
			startTimestamp = viewStartTimestamp;
			endTimestamp = viewEndTimestamp;
		}

		long deltaTime = endTimestamp - startTimestamp;

		graphics.drawString(Long.toString(startTimestamp), 5, getHeight() - 5);
		graphics.drawString(Long.toString(deltaTime + startTimestamp), getWidth() - 45, getHeight() - 5);
		graphics.setColor(Color.BLACK);
		graphics.drawLine(mouseX, 0, mouseX, getHeight());
		graphics.drawString(Long.toString((long) (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime)), mouseX - 25, getHeight() / 2);

		if (dataPoints == null || dataPoints.isEmpty()) {
			return;
		}

		// Check if we have any data in the displayed range
		// If our data is outside the range, skip all the next stuff for efficiency
		if ((dataPoints.get(0).getTimeStamp() > endTimestamp) || (dataPoints.get(dataPoints.size() - 1).getTimeStamp() < startTimestamp)) {
			return;
		}

		// Find the index of the first data point prior to the start timestamp
		int firstPointIndex = -1, lastPointIndex = -1;
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

		if (firstPointIndex == -1) {
			// Something broke.
			System.out.println("First point index is -1");
			return;
		}

		// If the last timestamp we have data for is less than the end timestamp,
		// use the last timestamp. Otherwise, search ahead in the data for the first timestamp
		// beyond the end timestamp
		if (dataPoints.get(dataPoints.size() - 1).getTimeStamp() < endTimestamp) {
			lastPointIndex = dataPoints.size() - 1;
		} else {
			for (int i = firstPointIndex; i < dataPoints.size(); i++) {
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

		if (lastPointIndex == -1) {
			// Something broke
			System.out.println("Last point index is -1");
			return;
		}

		for (int i = firstPointIndex; i < lastPointIndex + 1; i++) {
			DataPoint point = dataPoints.get(i);
			DataPoint nextPoint = null;
			if (i < (dataPoints.size() - 1)) {
				nextPoint = dataPoints.get(i + 1);
			} else {
				return;
			}
			if (graphType == DOUBLE_TYPE) {
				if (point.getObject() instanceof Double && nextPoint.getObject() instanceof Double) {
					int startXVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));
					int startYVal = getHeight() - ((Double) point.getObject()).intValue();
					int nextXVal = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));
					int nextYVal = getHeight() - ((Double) nextPoint.getObject()).intValue();
					g.setColor(Color.BLACK);
					g.drawLine(startXVal, startYVal, nextXVal, nextYVal);
					g.setColor(Color.BLACK);
					g.fillRect(startXVal - 1, startYVal - 1, 3, 3);
				}
			} else if (graphType == BOOL_TYPE) {
				if (point.getObject() instanceof Boolean && nextPoint.getObject() instanceof Boolean) {
					int xVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / getWidth()));
					int yVal;
					int width = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / getWidth()) - xVal);
					if (dataPoints.get(i).getObject().equals(true)) {
						g.setColor(Color.GREEN);
						yVal = 30;
						g.fillRect(xVal, yVal, width, 4);
					} else {
						g.setColor(Color.RED);
						yVal = 10;
						g.fillRect(xVal, yVal, width, 4);
					}
				}
			}
		}
	}

	public void updateModel(LogsModel model) {
		System.out.println("updated model");
		this.model = model;
		if (logKey != null) {
			dataPoints = model.getDataPointsForKey(logKey);
		}
		repaint();
	}

	public void setDataKey(String key) {
		System.out.println("updated data key");
		this.logKey = key;
		if (model != null) {
			dataPoints = model.getDataPointsForKey(logKey);
		}
		if (graphType != -1) {
			repaint();
		}
	}

	public void updateMousePosition(int posX, int posY) {
		mouseX = posX;
		mouseY = posY;
		repaint();
	}

	public void updateGraphView(long startTimestamp, long endTimestamp) {
		viewStartTimestamp = startTimestamp;
		viewEndTimestamp = endTimestamp;
		repaint();
	}

	private void clearPanel() {
		setBackground(UIManager.getColor("Panel.background"));
	}

	public void setType(int type) {
		graphType = type;
		repaint();
	}

}
