package org.wildstang.wildlog.viewer.views;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.wildstang.wildlog.viewer.models.DataPoint;
import org.wildstang.wildlog.viewer.models.LogsModel;
import org.wildstang.wildlog.viewer.renderers.LogRenderer;
import org.wildstang.wildlog.viewer.renderers.RendererFactory;

public class GraphingPanel extends JPanel {

	private LogsModel model;
	private String logKey;
	List<DataPoint> dataPoints;
	long firstTimestamp;
	int mouseX;
	int mouseY;
	long viewStartTimestamp = -1, viewEndTimestamp = -1;
	int dragRegionStart, dragRegionEnd;
	boolean shouldShowDragRegion = false;
	Color dotColor;

	private LogRenderer renderer;

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

		if (dataPoints != null && !dataPoints.isEmpty()) {
			/*
			 * Check if we have any data in the displayed range. If our data is outside the range, skip all the next
			 * stuff for efficiency.
			 */
			if ((dataPoints.get(0).getTimeStamp() > endTimestamp) || (dataPoints.get(dataPoints.size() - 1).getTimeStamp() < startTimestamp)) {
				return;
			}

			if (renderer != null) {
				renderer.renderLogs(g, getWidth(), getHeight(), startTimestamp, endTimestamp);
				if (!shouldShowDragRegion) {
					renderer.renderDecorations(g, getWidth(), getHeight(), startTimestamp, endTimestamp, mouseX);
				}
			} else {
				String noRendererFound = "No renderer found for the selected data type.";
				int stringWidth = SwingUtilities.computeStringWidth(g.getFontMetrics(), noRendererFound);

				g.drawString(noRendererFound, (getWidth() / 2) - (stringWidth / 2), getHeight() / 2);
			}
		}

		drawTimeLine(g, startTimestamp, endTimestamp);
	}

	public void clearData() {
		dataPoints = null;
		repaint();
	}

	private void drawTimeLine(Graphics g, long startTimestamp, long endTimestamp) {

		if (shouldShowDragRegion) {
			// Draw drag region, with the time scrubber positioned at the
			// current mouse position

			// Convert the drag region coordinates to local coordinates
			// They are originally in the coordinate system of the containing DataPanel
			int localPxDragRegionStart = dragRegionStart - getLocationOnScreen().x;
			int localPxDragRegionEnd = dragRegionEnd - getLocationOnScreen().x;

			System.out.println("localMouseX: " + localPxDragRegionStart + "; dragRegionStart: " + dragRegionStart);

			// Bound the drag region by the width of the box
			if (localPxDragRegionStart < 0) {
				localPxDragRegionStart = 0;
			}

			if (localPxDragRegionEnd > getWidth()) {
				localPxDragRegionEnd = getWidth();
			}

			// Draw drag region
			Color transparentWhite = new Color(255, 255, 255, 150);
			g.setColor(transparentWhite);
			g.fillRect(localPxDragRegionStart, 0, localPxDragRegionEnd - localPxDragRegionStart, getHeight());

			// Compute where we should draw the labels (inside or outside the time line)
			FontMetrics fMetrics = g.getFontMetrics();
			String startTimestampString = Long.toString(mapMousePositionToTimestamp(localPxDragRegionStart, startTimestamp, endTimestamp));
			String endTimestampString = Long.toString(mapMousePositionToTimestamp(localPxDragRegionEnd, startTimestamp, endTimestamp));

			int startTimestampX;
			int startStringWidth = SwingUtilities.computeStringWidth(fMetrics, startTimestampString);
			if (localPxDragRegionStart - startStringWidth - 5 < 0) {
				// The label would extend past the start of the panel and we
				// should draw it on the inside
				startTimestampX = localPxDragRegionStart + 5;
			} else {
				// There's enough room on the outside
				startTimestampX = localPxDragRegionStart - startStringWidth - 5;
			}

			int endTimestampX;
			int endStringWidth = SwingUtilities.computeStringWidth(fMetrics, endTimestampString);
			if (localPxDragRegionEnd + 5 + endStringWidth > getWidth()) {
				// The label would extend past the start of the panel and we
				// should draw it on the inside
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

	public void updateModel(LogsModel model) {
		this.model = model;
		if (logKey != null) {
			dataPoints = model.getDataPointsForKey(logKey);
		}
		repaint();
	}

	public void updateSelectedDataKey(String key, Class<?> dataClass) {
		this.logKey = key;
		if (model != null) {
			dataPoints = model.getDataPointsForKey(logKey);
		}
		renderer = RendererFactory.getRendererForClass(dataClass);
		if (renderer != null) {
			renderer.updateLogModel(dataPoints);
			renderer.updateAccentColor(dotColor);
		}
		repaint();
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
