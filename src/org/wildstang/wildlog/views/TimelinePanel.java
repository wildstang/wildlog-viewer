package org.wildstang.wildlog.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class TimelinePanel extends JPanel {

	private long startTimestamp, endTimestamp;
	private int leftEdgePx, rightEdgePx;
	private int mouseX, mouseY;
	private int timelineLeftBound;
	private int timelineRightBound;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Convert graphing panel bounds to local coordinates
		timelineLeftBound = leftEdgePx - getLocationOnScreen().x;
		timelineRightBound = rightEdgePx - getLocationOnScreen().x;
		g.setColor(Color.GREEN);
		g.fillRect(timelineLeftBound, 0, timelineRightBound, getHeight());
		g.setColor(Color.BLACK);

		Rectangle2D stringBounds = g.getFontMetrics().getStringBounds("" + startTimestamp, g);
		g.drawString("" + startTimestamp, timelineLeftBound + 5, (int) (0 + getHeight() / 2 + stringBounds.getHeight() / 2) - 5);
		stringBounds = g.getFontMetrics().getStringBounds("" + endTimestamp, g);
		g.drawString("" + endTimestamp, (int) (timelineRightBound - stringBounds.getWidth() - 5), (int) (getHeight() / 2 + stringBounds.getHeight() / 2) - 5);

		g.setColor(Color.BLACK);
		g.drawLine(mouseX + timelineLeftBound, 0, mouseX + timelineLeftBound, getHeight());
		long deltaTime = endTimestamp - startTimestamp;
		String currentPositionLabel = Long.toString((long) (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime));
		stringBounds = g.getFontMetrics().getStringBounds(currentPositionLabel, g);
		if (timelineLeftBound + mouseX - (int) (stringBounds.getWidth()) < timelineLeftBound) {
			// String would be drawn past the start of the timeline
			// Draw to the right of the line
			g.drawString(currentPositionLabel, timelineLeftBound + mouseX + 5, (int) (stringBounds.getHeight() / 2) + 5);
		} else {
			g.drawString(currentPositionLabel, timelineLeftBound + mouseX - (int) (stringBounds.getWidth()) - 5, (int) (stringBounds.getHeight() / 2) + 5);
		}
	}

	public void updateGraphingPanelsBounds(int leftEdgePx, int rightEdgePx) {
		this.leftEdgePx = leftEdgePx;
		this.rightEdgePx = rightEdgePx;
		repaint();
	}

	public void updateGraphPanelZoomAndScroll(long startTimestamp, long endTimestamp) {
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		repaint();
	}

	public void updateMosuePosition(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		repaint();
	}
}
