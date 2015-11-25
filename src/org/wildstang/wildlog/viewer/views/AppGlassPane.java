package org.wildstang.wildlog.viewer.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;

public class AppGlassPane extends JComponent
{
   private ApplicationController m_controller;
   private ViewProperties m_viewProperties;
   
//   int dragRegionStart, dragRegionEnd;
//   boolean shouldShowDragRegion = false;
//
//   int mouseX;
//   int mouseY;
//   private long viewStartTimestamp = -1;
//   private long viewEndTimestamp = -1;

   public AppGlassPane(ApplicationController controller)
   {
      m_controller = controller;
      m_viewProperties = m_controller.getViewProperties();
   }

   @Override
   protected void paintComponent(Graphics g)
   {
      // TODO Auto-generated method stub
//      super.paintComponent(g);

      if (m_controller.getModel() == null)
      {
         return;
      }

      long startTimestamp, endTimestamp;
      if (m_viewProperties.getViewStartTimestamp() == -1 || m_viewProperties.getViewEndTimestamp() == -1) {
         startTimestamp = m_controller.getModel().getStartTimestamp();
         endTimestamp = m_controller.getModel().getEndTimestamp();
      } else {
         startTimestamp = m_viewProperties.getViewStartTimestamp();
         endTimestamp = m_viewProperties.getViewEndTimestamp();
      }

      drawTimeLine(g, startTimestamp, endTimestamp);

   }

   public void updateGraphView(long startTimestamp, long endTimestamp) {
      m_viewProperties.setViewStartTimestamp(startTimestamp);
      m_viewProperties.setViewEndTimestamp(endTimestamp);
      repaint();
   }

//   public void updateMousePosition(int posX, int posY) {
//      mouseX = posX;
//      mouseY = posY;
//      repaint();
//   }

//   public void updateDragRegion(int pxStart, int pxEnd, boolean shouldShowDragRegion) {
//      dragRegionStart = pxStart;
//      dragRegionEnd = pxEnd;
//      this.shouldShowDragRegion = shouldShowDragRegion;
//      repaint();
//   }

   private void drawTimeLine(Graphics g, long startTimestamp, long endTimestamp)
   {

      if (m_viewProperties.isShouldShowDragRegion())
      {
         // Draw drag region, with the time scrubber positioned at the
         // current mouse position

         // Convert the drag region coordinates to local coordinates
         // They are originally in the coordinate system of the containing
         // DataPanel
         int localPxDragRegionStart = m_viewProperties.getDragRegionStart() - getLocationOnScreen().x;
         int localPxDragRegionEnd = m_viewProperties.getDragRegionEnd() - getLocationOnScreen().x;

//         System.out.println("localMouseX: " + localPxDragRegionStart
//               + "; dragRegionStart: " + dragRegionStart);

         // Bound the drag region by the width of the box
         if (localPxDragRegionStart < 0)
         {
            localPxDragRegionStart = 0;
         }

         if (localPxDragRegionEnd > getWidth())
         {
            localPxDragRegionEnd = getWidth();
         }

         // Draw drag region
         Color transparentWhite = new Color(255, 255, 255, 150);
         g.setColor(transparentWhite);
         g.fillRect(localPxDragRegionStart, 0, localPxDragRegionEnd
               - localPxDragRegionStart, getHeight());

         // Compute where we should draw the labels (inside or outside the time
         // line)
         FontMetrics fMetrics = g.getFontMetrics();
         String startTimestampString = Long.toString(mapMousePositionToTimestamp(localPxDragRegionStart, startTimestamp, endTimestamp));
         String endTimestampString = Long.toString(mapMousePositionToTimestamp(localPxDragRegionEnd, startTimestamp, endTimestamp));

         int startTimestampX;
         int startStringWidth = SwingUtilities.computeStringWidth(fMetrics, startTimestampString);
         if (localPxDragRegionStart - startStringWidth - 5 < 0)
         {
            // The label would extend past the start of the panel and we
            // should draw it on the inside
            startTimestampX = localPxDragRegionStart + 5;
         }
         else
         {
            // There's enough room on the outside
            startTimestampX = localPxDragRegionStart - startStringWidth - 5;
         }

         int endTimestampX;
         int endStringWidth = SwingUtilities.computeStringWidth(fMetrics, endTimestampString);
         if (localPxDragRegionEnd + 5 + endStringWidth > getWidth())
         {
            // The label would extend past the start of the panel and we
            // should draw it on the inside
            endTimestampX = localPxDragRegionEnd - 5 - endStringWidth;
         }
         else
         {
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
      g.drawLine(m_viewProperties.getMouseX(), 0, m_viewProperties.getMouseX(), getHeight());
   }

   private long mapMousePositionToTimestamp(int mouseX, long startTimestamp,
         long endTimestamp)
   {
      long deltaTime = endTimestamp - startTimestamp;
      return (long) (startTimestamp + ((double) mouseX / (double) getWidth())
            * deltaTime);
   }

}
