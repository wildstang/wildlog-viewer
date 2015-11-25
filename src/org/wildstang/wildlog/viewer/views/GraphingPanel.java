package org.wildstang.wildlog.viewer.views;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
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

	}

	public void clearData() {
		dataPoints = null;
		repaint();
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
		mouseX = posX - ApplicationController.DATA_SELECT_PANEL_WIDTH;
//		mouseY = posY;
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
