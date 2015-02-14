package org.wildstang.sdlogreader.views;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
	int dragRegionStart, dragRegionEnd;
	boolean shouldShowDragRegion = false;
	Color dotColor;

	public GraphingPanel(Color c) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		dotColor = c;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

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

		if (shouldShowDragRegion) {
			// Draw drag region, with the time scrubber positioned at the current mouse position

			int localPxDragRegionStart = dragRegionStart - getLocationOnScreen().x;
			int localPxDragRegionEnd = dragRegionEnd - getLocationOnScreen().x;

			// Bound the drag region by the width of the box
			if (localPxDragRegionStart < 0) {
				localPxDragRegionStart = 0;
			}

			if (localPxDragRegionEnd > getWidth()) {
				localPxDragRegionEnd = getWidth();
			}

			// Draw drag region
			g.setColor(Color.WHITE);
			g.fillRect(localPxDragRegionStart, 0, localPxDragRegionEnd - localPxDragRegionStart, getHeight());

			// Compute where we should draw the labels (inside or outside the time line)
			FontMetrics fMetrics = g.getFontMetrics();
			String startTimestampString = Long.toString(mapMousePositionToTimestamp(localPxDragRegionStart, startTimestamp, endTimestamp));
			String endTimestampString = Long.toString(mapMousePositionToTimestamp(localPxDragRegionEnd, startTimestamp, endTimestamp));

			int startTimestampX;
			int startStringWidth = SwingUtilities.computeStringWidth(fMetrics, startTimestampString);
			if (localPxDragRegionStart - startStringWidth - 5 < 0) {
				// The label would extend past the start of the panel and we should draw it on the inside
				startTimestampX = localPxDragRegionStart + 5;
			} else {
				// There's enough room on the outside
				startTimestampX = localPxDragRegionStart - startStringWidth - 5;
			}

			int endTimestampX;
			int endStringWidth = SwingUtilities.computeStringWidth(fMetrics, endTimestampString);
			if (localPxDragRegionEnd + 5 + endStringWidth > getWidth()) {
				// The label would extend past the start of the panel and we should draw it on the inside
				endTimestampX = localPxDragRegionEnd - 5 - endStringWidth;
			} else {
				// There's enough room on the outside
				endTimestampX = localPxDragRegionEnd + 5;
			}

			// Draw start line and label
			g.setColor(Color.BLACK);
			g.drawLine(localPxDragRegionStart, 0, localPxDragRegionStart, getHeight());
			g.drawString(startTimestampString, startTimestampX, getHeight() / 2);

			// Draw end line and label
			g.drawLine(localPxDragRegionEnd, 0, localPxDragRegionEnd, getHeight());
			g.drawString(endTimestampString, endTimestampX, getHeight() / 2);
		} else {
			// Draw the time scrubber without any drag region
			g.setColor(Color.BLACK);
			g.drawLine(mouseX, 0, mouseX, getHeight());
			g.drawString(Long.toString(mapMousePositionToTimestamp(mouseX, startTimestamp, endTimestamp)), mouseX - 25, getHeight() / 2);
		}

		// Draw the beginning and end timestamps
		g.drawString(Long.toString(startTimestamp), 5, getHeight() - 5);
		g.drawString(Long.toString(startTimestamp + deltaTime), getWidth() - 45, getHeight() - 5);
		g.setColor(Color.BLACK);
		g.drawLine(mouseX, 0, mouseX, getHeight());

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

		double range = 0;
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

				double newdist = Math.abs(dataPoints.get(i).getTimeStamp() - (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime));
				if (newdist < distance) {
					distance = newdist;
					closest = i;
				}
			}
			range = highest - lowest;

			BigDecimal bd = new BigDecimal((Double) dataPoints.get(closest).getObject());
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			g.drawString(Double.toString(bd.doubleValue()), mouseX - 30, getHeight() / 2);
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

					/*
					 * Calculate the scaled position of this point. It will be a value between 0 and 1, with 0
					 * corresponding with the min in the range, and 1 with the max
					 */
					double scaledPosition = (((Double) point.getObject()) - lowest) / range;
					System.out.println("Actual value: " + point.getObject() + "; scaled position: " + scaledPosition);
					/*
					 * "space" is the amount of vertical space we have to graph in. This is equal to the height,
					 * minus a 10% padding on the top and bottom.
					 */
					double padding = (double) getHeight() * 0.1;
					double space = (double) getHeight() - (padding * 2);
					int yPos = (int) (scaledPosition * space);
					int startYVal = (int) (getHeight() - padding - yPos);

					int nextXVal = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));

					scaledPosition = (((Double) nextPoint.getObject()) - lowest) / range;
					yPos = (int) (scaledPosition * space);
					int nextYVal = (int) (getHeight() - padding - yPos);

					g.setColor(Color.BLACK);
					g.drawLine(startXVal, startYVal, nextXVal, nextYVal);
					System.out.println("Line drawn from (" + startXVal + ", " + startYVal + ") to (" + nextXVal + ", " + nextYVal + ")");
					g.setColor(dotColor);
					g.fillRect(startXVal - 1, startYVal - 1, 3, 3);
					g.fillRect(nextXVal - 1, nextYVal - 1, 3, 3);
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
			} else if (graphType == STRING_TYPE) {

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

	public void updateDragRegion(int pxStart, int pxEnd, boolean shouldShowDragRegion) {
		dragRegionStart = pxStart;
		dragRegionEnd = pxEnd;
		this.shouldShowDragRegion = shouldShowDragRegion;
		repaint();
	}

	private void clearPanel() {
		setBackground(UIManager.getColor("Panel.background"));
	}

	public void setType(int type) {
		graphType = type;
		repaint();
	}

	private long mapMousePositionToTimestamp(int mouseX, long startTimestamp, long endTimestamp) {
		long deltaTime = endTimestamp - startTimestamp;
		return (long) (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime);
	}

	public long mapAbsoluteMousePositionToTimestamp(int mouseX) {
		long startTimestamp, endTimestamp;
		if (viewStartTimestamp == -1 || viewEndTimestamp == -1) {
			startTimestamp = model.getStartTimestamp();
			endTimestamp = model.getEndTimestamp();
		} else {
			startTimestamp = viewStartTimestamp;
			endTimestamp = viewEndTimestamp;
		}

		int localX = mouseX - getLocationOnScreen().x;
		long deltaTime = endTimestamp - startTimestamp;
		return (long) (startTimestamp + ((double) localX / (double) getWidth()) * deltaTime);
	}

}
