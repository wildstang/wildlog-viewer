package org.wildstang.wildlog.viewer.renderers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.SwingUtilities;

import org.wildstang.wildlog.viewer.models.DataPoint;

public class DoubleRenderer extends LogRenderer
{

   private double maxValue, minValue;

   @Override
   public void updateLogModel(List<DataPoint> dataPoints)
   {
      super.updateLogModel(dataPoints);

      // finds the max and min values
      maxValue = minValue = 0;
      for (int i = 0; i < dataPoints.size(); i++)
      {
         double current = (Double) dataPoints.get(i).getObject();
         if (current > maxValue)
         {
            maxValue = current;
         }
         else if (current < minValue)
         {
            minValue = current;
         }
      }
   }

   @Override
   public void renderLogs(Graphics g, int panelWidth, int panelHeight, long startTimestamp, long endTimestamp)
   {

      int firstPointIndex = findFirstPointBeforeTimestamp(startTimestamp);
      int lastPointIndex = findFirstPointAfterTimestamp(endTimestamp);
      // compute delta time
      long deltaTime = endTimestamp - startTimestamp;

      double range = maxValue - minValue;

      // compute constant things
      double topPadding = panelHeight * 0.1;
      double bottomPadding = g.getFontMetrics().getHeight();
      double space = panelHeight - topPadding - bottomPadding;

      // draw gridlines (just min and max)
      g.setColor(Color.LIGHT_GRAY);
      g.drawLine(40, (int) topPadding, panelWidth, (int) topPadding);
      g.drawLine(40, (int) (panelHeight - bottomPadding), panelWidth, (int) (panelHeight - bottomPadding));

      // iterates through points and draws them
      g.setColor(accentColor);
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
            break;
         }
         if (point.getObject() instanceof Double && nextPoint.getObject() instanceof Double)
         {
            int startXVal = (int) ((point.getTimeStamp() - startTimestamp) / (deltaTime / (double) panelWidth));
            int nextXVal = (int) ((nextPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) panelWidth));

            /*
             * Calculate the scaled position of this point. It will be a value
             * between 0 and 1, with 0 corresponding with the min in the range,
             * and 1 with the max
             */
            double scaledPosition = (((Double) point.getObject()) - minValue) / range;
            /*
             * "space" is the amount of vertical space we have to graph in. This
             * is equal to the height, minus a 10% padding on the top and
             * bottom.
             */
            int yPos = (int) (scaledPosition * space);
            int startYVal = (int) (panelHeight - bottomPadding - yPos);

            scaledPosition = (((Double) nextPoint.getObject()) - minValue) / range;
            yPos = (int) (scaledPosition * space);
            int nextYVal = (int) (panelHeight - bottomPadding - yPos);

            g.setColor(Color.BLACK);
            g.drawLine(startXVal, startYVal, nextXVal, nextYVal);

            g.setColor(accentColor);
            g.fillRect(startXVal - 1, startYVal - 1, 3, 3);
            g.fillRect(nextXVal - 1, nextYVal - 1, 3, 3);

         }

      }
      FontMetrics fm = g.getFontMetrics();
      g.setColor(Color.DARK_GRAY);
      BigDecimal bd = new BigDecimal(minValue);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      g.drawString(Double.toString(bd.doubleValue()), 5, panelHeight - fm.getHeight() / 2);
      bd = new BigDecimal(maxValue);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      g.drawString(Double.toString(bd.doubleValue()), 5, fm.getHeight());
   }

   @Override
   public void renderDecorations(Graphics g, int panelWidth, int panelHeight,
         long startTimestamp, long endTimestamp, int mousePosition)
   {
      int firstPointIndex = findFirstPointBeforeTimestamp(startTimestamp);
      int lastPointIndex = findFirstPointAfterTimestamp(endTimestamp);
      long deltaTime = endTimestamp - startTimestamp;

      long targetTimestamp = LogRenderer.mapPointInRangeToTimestamp(startTimestamp, endTimestamp, 0, panelWidth, mousePosition);
//      System.out.println("target timestamp: " + targetTimestamp);
      int indexOfClosest = findIndexOfClosestPointToTimestamp(dataPoints, targetTimestamp);
//      System.out.println("index of closest: " + indexOfClosest);

      BigDecimal bd = new BigDecimal((Double) dataPoints.get(indexOfClosest).getObject());
      bd = bd.setScale(8, RoundingMode.HALF_UP);
      String label = Double.toString(bd.doubleValue());
      int stringWidth = SwingUtilities.computeStringWidth(g.getFontMetrics(), label);

      g.drawString(label, mousePosition - stringWidth - 5, panelHeight - 2);

      // Highlight the nearest point
      DataPoint nearestPoint = dataPoints.get(indexOfClosest);
      double range = maxValue - minValue;
      double scaledPosition = (((Double) nearestPoint.getObject()) - minValue) / range;
      int x = (int) ((nearestPoint.getTimeStamp() - startTimestamp) / (deltaTime / (double) panelWidth));

      double topPadding = panelHeight * 0.1;
      double bottomPadding = g.getFontMetrics().getHeight();
      double space = panelHeight - topPadding - bottomPadding;

      int yPos = (int) (scaledPosition * space);
      int y = (int) (panelHeight - bottomPadding - yPos);

      g.setColor(accentColor);
      g.fillRect(x - 3, y - 3, 7, 7);

   }

}
