package org.wildstang.wildlog.viewer.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.wildstang.wildlog.viewer.views.ViewProperties;

public class GraphViewMouseListener extends MouseAdapter
{
   private int startX;

   private ApplicationController m_controller;
   private ViewProperties m_viewProperties;
   
   public GraphViewMouseListener(ApplicationController controller)
   {
      m_controller = controller;
      m_viewProperties = m_controller.getViewProperties();
   }
   
   public void mouseMoved(MouseEvent e)
   {
      System.out.println("e.getX(): " + e.getX() + "\t\te.getXOnScreen(): " + e.getXOnScreen());
      m_viewProperties.setMouseX(e.getXOnScreen());
      m_viewProperties.setMouseY(e.getYOnScreen());
      m_controller.frame.repaint();
      
   }

   public void mousePressed(MouseEvent e)
   {
      startX = e.getXOnScreen();
      
      m_viewProperties.setDragRegionStart(startX);
      m_viewProperties.setDragRegionEnd(startX);
      m_viewProperties.setShouldShowDragRegion(true);
      m_controller.frame.repaint();
   }

   public void mouseDragged(MouseEvent e)
   {
      int currentX = e.getXOnScreen();
      // If we dragged to the left of the initial point, invert the points
      if (Math.abs(currentX - startX) > 1)
      {
         if (currentX < startX)
         {
            m_viewProperties.setDragRegionStart(currentX);
            m_viewProperties.setDragRegionEnd(startX);
            m_viewProperties.setShouldShowDragRegion(true);
         }
         else
         {
            m_viewProperties.setDragRegionStart(startX);
            m_viewProperties.setDragRegionEnd(currentX);
            m_viewProperties.setShouldShowDragRegion(true);
         }
      }

      m_controller.frame.repaint();
   }

   public void mouseReleased(MouseEvent e)
   {
      int finalX = e.getXOnScreen();

      // If we dragged to the left of the initial point, invert the points
      if (Math.abs(finalX - startX) > 1)
      {
         if (finalX < startX)
         {
            m_viewProperties.setDragRegionStart(finalX);
            m_viewProperties.setDragRegionEnd(startX);
            m_viewProperties.setShouldShowDragRegion(false);

            m_controller.zoomToDragRegion(finalX, startX);
         }
         else
         {
            m_viewProperties.setDragRegionStart(startX);
            m_viewProperties.setDragRegionEnd(finalX);
            m_viewProperties.setShouldShowDragRegion(false);

            m_controller.zoomToDragRegion(startX, finalX);
         }
      }
      else
      {
         m_viewProperties.setDragRegionStart(0);
         m_viewProperties.setDragRegionEnd(0);
         m_viewProperties.setShouldShowDragRegion(false);
      }

      m_controller.frame.repaint();
   }

}
