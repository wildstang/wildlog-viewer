package org.wildstang.wildlog.viewer.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;

public class TimelinePanel extends JPanel
{

   private long startTimestamp, endTimestamp;

   private ViewProperties m_viewProperties;

   public TimelinePanel(ApplicationController controller)
   {
      m_viewProperties = controller.getViewProperties();
   }

   @Override
   protected void paintComponent(Graphics g)
   {
      super.paintComponent(g);

      int mouseX = m_viewProperties.getMouseX() - ApplicationController.DATA_SELECT_PANEL_WIDTH;

      // Convert graphing panel bounds to local coordinates
      int timelineLeftBound = ApplicationController.DATA_SELECT_PANEL_WIDTH;
      int timelineRightBound = getWidth();
      g.setColor(Color.GREEN);
      g.fillRect(timelineLeftBound, 0, timelineRightBound, getHeight());
      g.setColor(Color.BLACK);

      Rectangle2D stringBounds = g.getFontMetrics().getStringBounds("" + startTimestamp, g);
      g.drawString("" + startTimestamp, timelineLeftBound + 5, (int) (0 + getHeight() / 2 + stringBounds.getHeight() / 2) - 5);
      stringBounds = g.getFontMetrics().getStringBounds(endTimestamp + "ms", g);
      g.drawString(endTimestamp + "ms", (int) (timelineRightBound - stringBounds.getWidth() - 5), (int) (getHeight() / 2 + stringBounds.getHeight() / 2) - 5);

      if (mouseX >= 0)
      {
         long deltaTime = endTimestamp - startTimestamp;
         String currentPositionLabel = Long.toString((long) (startTimestamp + ((double) mouseX / (double) getWidth()) * deltaTime)) + "ms";
         stringBounds = g.getFontMetrics().getStringBounds(currentPositionLabel, g);
         if (timelineLeftBound + mouseX - (int) (stringBounds.getWidth()) < timelineLeftBound)
         {
            // String would be drawn past the start of the timeline
            // Draw to the right of the line
            g.drawString(currentPositionLabel, timelineLeftBound + mouseX + 5, (int) (stringBounds.getHeight() / 2) + 5);
         }
         else
         {
            g.drawString(currentPositionLabel, timelineLeftBound + mouseX - (int) (stringBounds.getWidth()) - 5, (int) (stringBounds.getHeight() / 2) + 5);
         }
      }
      // TODO: Draw scale

   }

   public void updateGraphPanelZoomAndScroll(long startTimestamp, long endTimestamp)
   {
      this.startTimestamp = startTimestamp;
      this.endTimestamp = endTimestamp;
      repaint();
   }

}
