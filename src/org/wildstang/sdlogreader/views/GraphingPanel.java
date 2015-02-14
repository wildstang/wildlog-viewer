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

public class GraphingPanel extends JPanel
{

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

	long startTimestamp;
	long endTimestamp;
	long deltaTime;

	int firstPointIndex;
	int lastPointIndex;

	double scale = 0;
	double lowest = 0;
	double highest = 0;

	public GraphingPanel(Color c)
	{
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		dotColor = c;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		graphics = g;

		if (model == null)
		{
			return;
		}

		findTimestamps();

		drawTimeLine();

		if (dataPoints == null || dataPoints.isEmpty())
		{
			return;
		}

		// Check if we have any data in the displayed range
		// If our data is outside the range, skip all the next stuff for
		// efficiency
		if ((dataPoints.get(0).getTimeStamp() > endTimestamp) || (dataPoints.get(dataPoints.size() - 1).getTimeStamp() < startTimestamp))
		{
			return;
		}

		findFirstPoint();
		findLastPoint();

		if (graphType == DOUBLE_TYPE)
		{
			ifDouble(g);
		}
		else if (graphType == BOOL_TYPE)
		{
			ifBool(g);
		}
		else if (graphType == STRING_TYPE)
		{
			ifString(g);
		}

	}

	public void drawCenteredString(String s, int x, int y, Graphics g)
	{
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, x - (fm.stringWidth(s) / 2), y);
	}

	public void updateModel(LogsModel model)
	{
		System.out.println("updated model");
		this.model = model;
		if (logKey != null)
		{
			dataPoints = model.getDataPointsForKey(logKey);
		}
		repaint();
	}

	public void setDataKey(String key)
	{
		System.out.println("updated data key");
		this.logKey = key;
		if (model != null)
		{
			dataPoints = model.getDataPointsForKey(logKey);
		}
		if (graphType != -1)
		{
			repaint();
		}
	}

	public void updateMousePosition(int posX, int posY)
	{
		mouseX = posX;
		mouseY = posY;
		repaint();
	}

	public void updateGraphView(long startTimestamp, long endTimestamp)
	{
		viewStartTimestamp = startTimestamp;
		viewEndTimestamp = endTimestamp;
		repaint();
	}

	private void clearPanel()
	{
		setBackground(UIManager.getColor("Panel.background"));
	}

	public void setType(int type)
	{
		graphType = type;
		repaint();
	}

	private void drawTimeLine()
	{
		deltaTime = endTimestamp - startTimestamp;

		graphics.drawString(Long.toString(startTimestamp), 5, getHeight() - 5);
		graphics.drawString(Long.toString(deltaTime + startTimestamp), getWidth() - 45, getHeight() - 5);
		graphics.setColor(Color.BLACK);
		graphics.drawLine(mouseX, 0, mouseX, getHeight());
	}

	private void ifDouble(Graphics g)
	{
		//finds the highest and lowest points and their difference
		highest = (Double) dataPoints.get(firstPointIndex).getObject();
		lowest = (Double) dataPoints.get(firstPointIndex).getObject();
		for (int i = 0; i < dataPoints.size(); i++)
		{
			double current = (Double) dataPoints.get(i).getObject();
			if (current > highest)
			{
				highest = current;
			}
			else if (current < lowest)
			{
				lowest = current;
			}
		}
		scale = highest - lowest;

		
		//finds the height of the nearest point and draws it
		double distance = 100000;
		int closest = 0;
		for (int i = firstPointIndex; i < lastPointIndex + 1; i++)
		{

			double newdist = Math.abs(dataPoints.get(i).getTimeStamp() - (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime));
			if (newdist < distance)
			{
				distance = newdist;
				closest = i;
			}
		}
		BigDecimal bd = new BigDecimal((Double) dataPoints.get(closest).getObject());
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		graphics.drawString(Double.toString(bd.doubleValue()), mouseX - 30, getHeight() / 2);

		
		//parses through points and draws them
		for (int i = firstPointIndex; i < lastPointIndex + 1; i++)
		{
			DataPoint point = dataPoints.get(i);
			DataPoint nextPoint = null;
			if (i < (dataPoints.size() - 1))
			{
				nextPoint = dataPoints.get(i + 1);
			}
			else
			{
				return;
			}
			if (point.getObject() instanceof Double && nextPoint.getObject() instanceof Double)
			{
				int startXVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));

				//difference from current point to lowest
				int height = (int) (((Double) point.getObject()).intValue() - lowest);
				//ratio of current point to lowest and highest point to lowest
				double yScale = (height / scale);
				//the amount of space alloted for drawing
				int space = (int) (getHeight() * .8);
				//where the point should be compared to the space alloted
				int yPos = (int) (yScale * space);
				//where the point will be drawn before being centered
				int unAdjusted = getHeight() - yPos;
				//the amount of space needed to be added above the point to draw the graph center
				int adjustment = (int) (.5 * (getHeight() - space));

				int startYVal = unAdjusted - adjustment;

				int nextXVal = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));

				height = (int) (((Double) nextPoint.getObject()).intValue() - lowest);
				yScale = (height / scale);
				yPos = (int) (yScale * space);
				unAdjusted = getHeight() - yPos;

				int nextYVal = unAdjusted - adjustment;

				g.setColor(Color.BLACK);
				g.drawLine(startXVal, startYVal, nextXVal, nextYVal);
				System.out.println("Line drawn from (" + startXVal + ", " + startYVal + ") to (" + nextXVal + ", " + nextYVal + ")");
				g.setColor(dotColor);
				g.fillRect(startXVal - 1, startYVal - 1, 3, 3);
			}
		}
	}

	private void ifString(Graphics g)
	{
		for (int i = firstPointIndex; i < lastPointIndex + 1; i++)
		{
			DataPoint point = dataPoints.get(i);
			DataPoint nextPoint = null;
			if (i < (dataPoints.size() - 1))
			{
				nextPoint = dataPoints.get(i + 1);
			}
			else
			{
				return;
			}

			if (point.getObject() instanceof String && nextPoint.getObject() instanceof String)
			{
				int startXVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));
				g.setColor(dotColor);
				g.fillRect(startXVal - 2, getHeight() / 2 - 2, 5, 5);
				System.out.println("Drawing string @ (" + startXVal + ", " + getHeight() / 2 + ")");
				g.setColor(Color.BLACK);
				drawCenteredString((String) point.getObject(), startXVal, getHeight() / 2, g);
			}
		}
	}

	private void ifBool(Graphics g)
	{
		for (int i = firstPointIndex; i < lastPointIndex + 1; i++)
		{
			DataPoint point = dataPoints.get(i);
			DataPoint nextPoint = null;
			if (i < (dataPoints.size() - 1))
			{
				nextPoint = dataPoints.get(i + 1);
			}
			else
			{
				return;
			}

			if (point.getObject() instanceof Boolean && nextPoint.getObject() instanceof Boolean)
			{
				int xVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()));
				int width = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) getWidth()) - xVal);
				if (dataPoints.get(i).getObject().equals(true))
				{
					g.setColor(Color.GREEN);
					System.out.println("Drawing green rect @ (" + xVal + ", " + 0 + ")");
				}
				else
				{
					g.setColor(Color.RED);
					System.out.println("Drawing red rect @ (" + xVal + ", " + 0 + ")");
				}
				g.fillRect(xVal, 0, width, getHeight());
			}
		}
	}

	private void findTimestamps()
	{
		if (viewStartTimestamp == -1 || viewEndTimestamp == -1)
		{
			startTimestamp = model.getStartTimestamp();
			endTimestamp = model.getEndTimestamp();
		}
		else
		{
			startTimestamp = viewStartTimestamp;
			endTimestamp = viewEndTimestamp;
		}
	}
	
	private void findFirstPoint()
	{
		// Find the index of the first data point prior to the start timestamp
		int firstPointIndex = -1, lastPointIndex = -1;
		for (int i = 0; i < dataPoints.size(); i++)
		{
			long timestamp = dataPoints.get(i).getTimeStamp();
			if (timestamp > startTimestamp)
			{
				firstPointIndex = i - 1;
				if (firstPointIndex < 0)
				{
					firstPointIndex = 0;
				}
				break;
			}
		}

		if (firstPointIndex == -1)
		{
			// Something broke.
			System.out.println("First point index is -1");
			return;
		}
	}
	
	private void findLastPoint()
	{
		// If the last timestamp we have data for is less than the end
		// timestamp,
		// use the last timestamp. Otherwise, search ahead in the data for the
		// first timestamp
		// beyond the end timestamp
		if (dataPoints.get(dataPoints.size() - 1).getTimeStamp() < endTimestamp)
		{
			lastPointIndex = dataPoints.size() - 1;
		}
		else
		{
			for (int i = firstPointIndex; i < dataPoints.size(); i++)
			{
				long timestamp = dataPoints.get(i).getTimeStamp();
				if (timestamp >= endTimestamp)
				{
					lastPointIndex = i;
					if (lastPointIndex > (dataPoints.size() - 1))
					{
						lastPointIndex = dataPoints.size() - 1;
					}
					break;
				}
			}
		}

		if (lastPointIndex == -1)
		{
			// Something broke
			System.out.println("Last point index is -1");
			return;
		}
	}
}
