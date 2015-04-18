package org.wildstang.wildlog.viewer.renderers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import org.wildstang.wildlog.viewer.models.DataPoint;

public class StringRenderer extends LogRenderer {

	@Override
	public void renderLogs(Graphics g, int panelWidth, int panelHeight, long startTimestamp, long endTimestamp) {

		// Do everything in decorations because we're lazy for now
	}

	@Override
	public void renderDecorations(Graphics g, int panelWidth, int panelHeight, long startTimestamp, long endTimestamp, int mousePosition) {
		int firstPointIndex = findFirstPointBeforeTimestamp(startTimestamp);
		int lastPointIndex = findFirstPointAfterTimestamp( endTimestamp);
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
				int startXVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) panelWidth));
				g.setColor(accentColor);
				g.fillRect(startXVal - 2, panelHeight / 2 - 2, 5, 5);
				if (mousePosition >= startXVal - 2 && mousePosition <= startXVal + 2) {
					FontMetrics fm = g.getFontMetrics();
					String string = (String) point.getObject();
					int boxWidth = fm.stringWidth(string) + (int) (.33 * fm.getHeight());
					int boxHeight = (int) (1.33 * fm.getHeight());

					g.setColor(new Color(255, 255, 202));
					if (mousePosition + (1.2 * fm.stringWidth(string)) > panelWidth) {
						g.fillRect(panelWidth - boxWidth, panelHeight / 2 - boxHeight, boxWidth, boxHeight);
						g.setColor(new Color(0, 0, 19));
						g.drawRect(panelWidth - boxWidth, panelHeight / 2 - boxHeight, boxWidth, boxHeight);
						g.setColor(Color.BLACK);
						g.drawString(string, panelWidth - (fm.getHeight() + (int) (.33 * fm.getHeight())) + (int) (.33 * fm.getHeight()), panelHeight / 2 - (int) (.33 * fm.getHeight()));
					} else {
						g.fillRect(mousePosition, panelHeight / 2 - boxHeight, boxWidth, boxHeight);
						g.setColor(new Color(0, 0, 19));
						g.drawRect(mousePosition, panelHeight / 2 - boxHeight, boxWidth, boxHeight);
						g.setColor(Color.BLACK);
						g.drawString(string, mousePosition + (int) (.25 * fm.getHeight()), panelHeight / 2 - (int) (.33 * fm.getHeight()));
					}
				}
			}
		}
		
	}

}
