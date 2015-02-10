package org.wildstang.sdlogreader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class GraphingPanel extends JPanel {

	public final static int BOOL_TYPE = 1;
	public final static int DOUBLE_TYPE = 2;
	public final static int STRING_TYPE = 3;
	public final static int DEFAULT_TYPE = 0;

	List<DataPoint> dataPoints = new ArrayList<>();
	private int graphType = -1;
	int buttonClicked;
	long firstTimestamp;
	int mouseX;
	int mouseY;
	long viewStartTimestamp, viewEndTimestamp;
	Graphics graphics;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		graphics = g;
		if (dataPoints.isEmpty()) {
			return;
		}
		firstTimestamp = dataPoints.get(0).timestamp;
		long deltaTime = dataPoints.get(dataPoints.size() - 1).timestamp - firstTimestamp;
		graphics.drawString(Long.toString(firstTimestamp), 5, getHeight()-5);
		graphics.drawString(Long.toString(deltaTime + firstTimestamp), getWidth() - 45, getHeight()-5);
		graphics.setColor(Color.BLACK);
		graphics.drawLine(mouseX, 0, mouseX, getHeight());
		graphics.drawString(Long.toString((long) (firstTimestamp + ((double)mouseX / (double) getWidth()) * deltaTime)), mouseX - 25, getHeight() / 2);

		for (int i = 0; i < dataPoints.size(); i++) {
			DataPoint point = dataPoints.get(i);
			DataPoint nextPoint = null;
			if (i < (dataPoints.size() - 1)) {
				nextPoint = dataPoints.get(i + 1);
			}
			if (graphType == DOUBLE_TYPE) {
				if (point.storedObject instanceof Double && nextPoint.storedObject instanceof Double) {
					int startXVal = (int) ((point.timestamp - firstTimestamp) / (deltaTime / getWidth()));
					int startYVal = getHeight() - ((Double) point.storedObject).intValue();
					int nextXVal = (int) ((nextPoint.timestamp - firstTimestamp) / (deltaTime / getWidth()));
					int nextYVal = getHeight() - ((Double) nextPoint.storedObject).intValue();
					g.setColor(Color.BLACK);
					g.drawLine(startXVal, startYVal, nextXVal, nextYVal);
					g.setColor(Color.YELLOW);
					g.fillRect(startXVal, startYVal- 1, 1, 3);
					//System.out.println(point.timestamp + ", " + point.storedObject);
				}
				if (graphType == BOOL_TYPE) {
					if (point.storedObject instanceof Boolean && nextPoint.storedObject instanceof Boolean) {
						int xVal = (int) ((point.timestamp - firstTimestamp) / (deltaTime / getWidth()));
						int yVal;
						int width = (int) ((nextPoint.timestamp - firstTimestamp) / (deltaTime / getWidth()) - xVal);
						if (dataPoints.get(i).storedObject.equals(true)) {
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
	}

	public void setDataKey(String key) {
		dataPoints.clear();
		System.out.println("data key set");
		for (Map<String, Object> map : Deserialize.e) {
			long timestamp = (Long) map.get("Timestamp");
			Object value = map.get(key);
			dataPoints.add(new DataPoint(value, timestamp));
		}
		if (graphType != -1) {
			repaint();
		}
	}
	
	public void setMousePosition(int posX, int posY) {
		mouseX = posX;
		mouseY = posY;
		repaint();
	}
	
	public void updateGraphView(long startTimestamp, long endTimestamp) {
		viewStartTimestamp = startTimestamp;
		viewEndTimestamp = endTimestamp;
		repaint();
	}
	
	public GraphingPanel() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public void booleanPanel() {

	}

	public void doublePanel() {

	}

	public void stringPanel() {

	}

	public void clearPanel() {
		setBackground(UIManager.getColor("Panel.background"));
	}

	public static void updateGraphs() {

	}

	public void setType(int type) {
		graphType = type;
		switch (type) {
		case BOOL_TYPE:
			booleanPanel();
			break;
		case DOUBLE_TYPE:
			doublePanel();
			break;
		case STRING_TYPE:
			stringPanel();
			break;
		default:
			clearPanel();
			break;
		}
	}

	
}
