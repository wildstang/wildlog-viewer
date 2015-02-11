package org.wildstang.sdlogreader.views;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TimelinePanel extends JPanel {

	private long startTimestamp, endTimestamp;
	private int leftEdgePx, rightEdgePx;
	private int mouseX, mouseY;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Convert graphing panel bounds to local coordinates
		int timelineLeftBound = leftEdgePx - getLocationOnScreen().x;
		int timelineRightBound = rightEdgePx - getLocationOnScreen().x;
		g.setColor(Color.GREEN);
		g.fillRect(timelineLeftBound, 0, timelineRightBound, getHeight());
		g.setColor(Color.BLACK);
		g.drawString("" + startTimestamp, timelineLeftBound, getHeight());
		int textWidth = SwingUtilities.computeStringWidth(g.getFontMetrics(), "" + endTimestamp);
		g.drawString("" + endTimestamp, timelineRightBound - textWidth, getHeight());

		g.setColor(Color.BLACK);
		g.drawLine(mouseX + timelineLeftBound, 0, mouseX + timelineLeftBound, getHeight());
		long deltaTime = endTimestamp - startTimestamp;
		g.drawString(Long.toString((long) (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime)), mouseX + timelineLeftBound - 25, getHeight());
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
