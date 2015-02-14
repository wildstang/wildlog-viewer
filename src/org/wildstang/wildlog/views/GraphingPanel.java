package org.wildstang.wildlog.views;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.wildstang.wildlog.models.DataPoint;
import org.wildstang.wildlog.models.LogsModel;

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

		drawTimeLine(g, startTimestamp, endTimestamp);

		if (dataPoints == null || dataPoints.isEmpty()) {
			return;
		}

		// Check if we have any data in the displayed range
		// If our data is outside the range, skip all the next stuff for
		// efficiency
		if ((dataPoints.get(0).getTimeStamp() > endTimestamp) || (dataPoints.get(dataPoints.size() - 1).getTimeStamp() < startTimestamp)) {
			return;
		}

		int firstPointIndex = findFirstPoint(startTimestamp);
		int lastPointIndex = findLastPoint(endTimestamp);

		// If we're dragging to zoom, hide the labels/highlights on points
		boolean shouldDrawHighlightsAndLabels = !shouldShowDragRegion;
		if (graphType == DOUBLE_TYPE) {
			drawForDouble(g, startTimestamp, endTimestamp, firstPointIndex, lastPointIndex, shouldDrawHighlightsAndLabels);
		} else if (graphType == BOOL_TYPE) {
			drawForBoolean(g, startTimestamp, endTimestamp, firstPointIndex, lastPointIndex);
		} else if (graphType == STRING_TYPE) {
			drawForString(g, startTimestamp, endTimestamp, firstPointIndex, lastPointIndex);
		}

	}

	private void drawTimeLine(Graphics g, long startTimestamp, long endTimestamp) {
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
			g.drawString(startTimestampString, startTimestampX, getHeight());

			// Draw end line and label
			g.drawLine(localPxDragRegionEnd, 0, localPxDragRegionEnd, getHeight());
			g.drawString(endTimestampString, endTimestampX, getHeight());
		}

		g.setColor(Color.BLACK);
		g.drawLine(mouseX, 0, mouseX, getHeight());

	}

	private void drawForDouble(Graphics g, long startTimestamp, long endTimestamp, int firstPointIndex, int lastPointIndex, boolean highlightAndLabelNearestPoint) {
		// compute delta time
		long deltaTime = endTimestamp - startTimestamp;
		
		// finds the highest and lowest points and their difference
		double highest = (Double) dataPoints.get(firstPointIndex).getObject();
		double lowest = (Double) dataPoints.get(firstPointIndex).getObject();
		for (int i = 0; i < dataPoints.size(); i++) {
			double current = (Double) dataPoints.get(i).getObject();
			if (current > highest) {
				highest = current;
			} else if (current < lowest) {
				lowest = current;
			}
		}
		double range = highest - lowest;
		
		// compute constant things
		double topPadding = (double) getHeight() * 0.1;
		double bottomPadding = g.getFontMetrics().getHeight();
		double space = (double) getHeight() - topPadding - bottomPadding;
		
		// draw gridlines (just min and max)
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(40, (int) topPadding, getWidth(), (int) topPadding);
		g.drawLine(40, (int) (getHeight() - bottomPadding), getWidth(), (int) (getHeight() - bottomPadding));
		g.setColor(Color.BLACK);
		// finds the height of the nearest point and draws it
		double distance = Math.abs(dataPoints.get(0).getTimeStamp() - (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime));
		int indexOfClosest = 0;
		for (int i = firstPointIndex; i < lastPointIndex + 1; i++) {

			double newdist = Math.abs(dataPoints.get(i).getTimeStamp() - (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime));
			if (newdist < distance) {
				distance = newdist;
				indexOfClosest = i;
			}
		}
		if (highlightAndLabelNearestPoint) {
			BigDecimal bd = new BigDecimal((Double) dataPoints.get(indexOfClosest).getObject());
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			String label = Double.toString(bd.doubleValue());
			int stringWidth = SwingUtilities.computeStringWidth(g.getFontMetrics(), label);
			
			g.drawString(label, mouseX - stringWidth - 5, getHeight() - 2);
		}

		// iterates through points and draws them
		for (int i = firstPointIndex; i < lastPointIndex + 1; i++) {
			DataPoint point = dataPoints.get(i);
			DataPoint nextPoint = null;
			if (i < (dataPoints.size() - 1)) {
				nextPoint = dataPoints.get(i + 1);
			} else {
				break;
			}
			if (point.getObject() instanceof Double && nextPoint.getObject() instanceof Double) {
				int startXVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));

				/*
				 * Calculate the scaled position of this point. It will be a value between 0 and 1, with 0 corresponding
				 * with the min in the range, and 1 with the max
				 */
				double scaledPosition = (((Double) point.getObject()) - lowest) / range;
				/*
				 * "space" is the amount of vertical space we have to graph in. This is equal to the height, minus a 10%
				 * padding on the top and bottom.
				 */
				int yPos = (int) (scaledPosition * space);
				int startYVal = (int) (getHeight() - bottomPadding - yPos);

				int nextXVal = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));

				scaledPosition = (((Double) nextPoint.getObject()) - lowest) / range;
				yPos = (int) (scaledPosition * space);
				int nextYVal = (int) (getHeight() - bottomPadding - yPos);

				g.setColor(Color.BLACK);
				g.drawLine(startXVal, startYVal, nextXVal, nextYVal);

				g.setColor(dotColor);
				// Highlight the current point if we so desire
				if (highlightAndLabelNearestPoint && i == indexOfClosest) {
					g.fillRect(startXVal - 3, startYVal - 3, 7, 7);
				} else {
					g.fillRect(startXVal - 1, startYVal - 1, 3, 3);

				}
				if (highlightAndLabelNearestPoint && i + 1 == indexOfClosest) {
					g.fillRect(nextXVal - 3, nextYVal - 3, 7, 7);
				} else {
					g.fillRect(nextXVal - 1, nextYVal - 1, 3, 3);

				}
			}
		}
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.DARK_GRAY);
		BigDecimal bd = new BigDecimal(lowest);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		g.drawString(Double.toString(bd.doubleValue()), 5, getHeight() - fm.getHeight()/2);
		bd = new BigDecimal(highest);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		g.drawString(Double.toString(bd.doubleValue()), 5, fm.getHeight());
	}

	private void drawForString(Graphics g, long startTimestamp, long endTimestamp, int firstPointIndex, int lastPointIndex) {
		long deltaTime = endTimestamp - startTimestamp;
		for (int i = firstPointIndex; i < lastPointIndex + 1; i++) {
			DataPoint point = dataPoints.get(i);
			DataPoint nextPoint = null;
			if (i < (dataPoints.size() - 1)) {
				nextPoint = dataPoints.get(i + 1);
			} else {
				return;
			}

			if (point.getObject() instanceof String && nextPoint.getObject() instanceof String) {
				int startXVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));
				g.setColor(dotColor);
				g.fillRect(startXVal - 2, getHeight() / 2 - 2, 5, 5);
				if(mouseX >= startXVal - 2 && mouseX <= startXVal + 2 /*&& mouseY >= getHeight() / 2 - 2 && mouseY <= getHeight() / 2 + 2*/)
				{
					FontMetrics fm = g.getFontMetrics();
					String string = (String) point.getObject();
					int boxWidth = fm.stringWidth(string) + (int)(.33 * fm.getHeight());
					int boxHeight = (int)(1.33 * fm.getHeight());

					g.setColor(new Color(255, 255, 202));
					if(mouseX + (1.2 * fm.stringWidth(string)) > getWidth())
					{
						g.fillRect(getWidth() - boxWidth, mouseY - boxHeight, boxWidth, boxHeight);
						g.setColor(new Color(0, 0, 19));
						g.drawRect(getWidth() - boxWidth, mouseY - boxHeight, boxWidth, boxHeight);
						g.setColor(Color.BLACK);
						g.drawString(string, getWidth() - (fm.getHeight() + (int)(.33 * fm.getHeight())) + (int)(.33 * fm.getHeight()), mouseY - (int)(.33 * fm.getHeight()));
					}
					else
					{
						g.fillRect(mouseX, mouseY - boxHeight, boxWidth, boxHeight);
						g.setColor(new Color(0, 0, 19));
						g.drawRect(mouseX, mouseY - boxHeight, boxWidth, boxHeight);
						g.setColor(Color.BLACK);
						g.drawString(string, mouseX + (int)(.25 * fm.getHeight()), mouseY - (int)(.33 * fm.getHeight()));
					}
				}
			}
		}
	}

	private void drawForBoolean(Graphics g, long startTimestamp, long endTimestamp, int firstPointIndex, int lastPointIndex) {
		long deltaTime = endTimestamp - startTimestamp;
		for (int i = firstPointIndex; i < lastPointIndex + 1; i++) {
			DataPoint point = dataPoints.get(i);
			DataPoint nextPoint = null;
			if (i < (dataPoints.size() - 1)) {
				nextPoint = dataPoints.get(i + 1);
			} else {
				return;
			}

			if (point.getObject() instanceof Boolean && nextPoint.getObject() instanceof Boolean) {
				int xVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));
				int width = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()) - xVal);
				if (dataPoints.get(i).getObject().equals(true)) {
					g.setColor(Color.GREEN);
				} else {
					g.setColor(Color.RED);
				}
				g.fillRect(xVal, 0, width, getHeight());
			}
		}
	}

	private int findFirstPoint(long startTimestamp) {
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

	private int findLastPoint(long endTimestamp) {
		// If the last timestamp we have data for is less than the end
		// timestamp,
		// use the last timestamp. Otherwise, search ahead in the data for the
		// first timestamp
		// beyond the end timestamp
		int lastPointIndex = -1;
		if (dataPoints.get(dataPoints.size() - 1).getTimeStamp() < endTimestamp) {
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
