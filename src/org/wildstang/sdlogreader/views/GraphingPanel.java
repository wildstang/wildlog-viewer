package org.wildstang.sdlogreader.views;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
	Color dotColor;

	public GraphingPanel(Color c) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		dotColor = c;
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
		// graphics.drawString(Long.toString((long) (startTimestamp + ((double)
		// mouseX / (double) getWidth()) * deltaTime)), mouseX - 25, getHeight()
		// / 2);

		if (dataPoints == null || dataPoints.isEmpty()) {
			return;
		}

		// Check if we have any data in the displayed range
		// If our data is outside the range, skip all the next stuff for
		// efficiency
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

		// If the last timestamp we have data for is less than the end
		// timestamp,
		// use the last timestamp. Otherwise, search ahead in the data for the
		// first timestamp
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

		double scale = 0;
		double lowest = 0;
		double highest = 0;
		if (graphType == DOUBLE_TYPE) {
			double distance = 100000;
			int closest = 0;
			highest = (Double) dataPoints.get(firstPointIndex).getObject();
			lowest = (Double) dataPoints.get(firstPointIndex).getObject();

			for (int i = 0; i < dataPoints.size(); i++) {
				double current = (Double) dataPoints.get(i).getObject();
				if (current > highest) {
					highest = current;
				} else if (current < lowest) {
					lowest = current;
				}
			}

			for (int i = firstPointIndex; i < lastPointIndex + 1; i++) {

				double newdist = Math.abs(dataPoints.get(i).getTimeStamp() - (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime));
				if (newdist < distance) {
					distance = newdist;
					closest = i;
				}
			}
			scale = highest - lowest;

			BigDecimal bd = new BigDecimal((Double) dataPoints.get(closest).getObject());
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			graphics.drawString(Double.toString(bd.doubleValue()), mouseX - 30, getHeight() / 2);
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

					int height = (int) (((Double) point.getObject()).intValue() - lowest);
					double yScale = (height / scale);
					int space = (int) (getHeight() * .8);
					int yPos = (int) (yScale * space);
					int unAdjusted = getHeight() - yPos;
					int adjustment = (int) (.5 * (getHeight() - space));

					// int startYVal = getHeight() - ((Double)
					// point.getObject()).intValue();
					int startYVal = unAdjusted - adjustment;

					int nextXVal = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));

					height = (int) (((Double) nextPoint.getObject()).intValue() - lowest);
					yScale = (height / scale);
					yPos = (int) (yScale * space);
					unAdjusted = getHeight() - yPos;

					// int nextYVal = getHeight() - ((Double)
					// nextPoint.getObject()).intValue();
					int nextYVal = unAdjusted - adjustment;

					g.setColor(Color.BLACK);
					g.drawLine(startXVal, startYVal, nextXVal, nextYVal);
					System.out.println("Line drawn from (" + startXVal + ", " + startYVal + ") to (" + nextXVal + ", " + nextYVal + ")");
					g.setColor(dotColor);
					g.fillRect(startXVal - 1, startYVal - 1, 3, 3);
				}
			} else if (graphType == BOOL_TYPE) {
				if (point.getObject() instanceof Boolean && nextPoint.getObject() instanceof Boolean) {
					int xVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));
					int width = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()) - xVal);
					if (dataPoints.get(i).getObject().equals(true)) {
						g.setColor(Color.GREEN);
						System.out.println("Drawing green rect @ (" + xVal + ", " + 0 + ")");
					} else {
						g.setColor(Color.RED);
						System.out.println("Drawing red rect @ (" + xVal + ", " + 0 + ")");
					}
					g.fillRect(xVal, 0, width, getHeight());
				}
			} else if (graphType == STRING_TYPE) {
				if (point.getObject() instanceof String && nextPoint.getObject() instanceof String) {
					int startXVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));
					g.setColor(dotColor);
					g.fillRect(startXVal - 2, getHeight() / 2 - 2, 5, 5);
					System.out.println("Drawing string @ (" + startXVal + ", " + getHeight() / 2 + ")");
					g.setColor(Color.BLACK);
					drawCenteredString((String) point.getObject(), startXVal, getHeight() / 2, g);
				}
			} else {
				System.out.println("Not a Type");
			}
		}
	}

	public void drawCenteredString(String s, int x, int y, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, x - (fm.stringWidth(s) / 2), y);
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
