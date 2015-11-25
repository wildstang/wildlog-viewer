package org.wildstang.wildlog.viewer.controllers;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.wildstang.wildlog.viewer.views.AppGlassPane;
import org.wildstang.wildlog.viewer.views.TitleButtonPanel;
import org.wildstang.wildlog.viewer.views.ViewProperties;

public class GlassPaneMouseListener extends MouseAdapter
{
   private int startX;
   private int lastX;

   private ApplicationController m_controller;
   private AppGlassPane m_glassPane;
   private Container m_contentPane;
   private ViewProperties m_viewProperties;
   
   public GlassPaneMouseListener(ApplicationController controller, AppGlassPane glassPane, Container contentPane)
   {
      m_controller = controller;
      m_glassPane = glassPane;
      m_contentPane = contentPane;
      m_viewProperties = m_controller.getViewProperties();
   }
   
   public void mouseMoved(MouseEvent e)
   {
      m_controller.updateMousePosition(e.getX(), e.getY());

      m_viewProperties.setMouseX(e.getX());
      m_viewProperties.setMouseY(e.getY());
      
      redispatchEvent(e);
   }

   public void mousePressed(MouseEvent e)
   {
      startX = e.getXOnScreen();
      
      m_viewProperties.setDragRegionStart(startX);
      m_viewProperties.setDragRegionEnd(startX);
      m_viewProperties.setShouldShowDragRegion(true);

      m_glassPane.repaint();
      
      redispatchEvent(e);
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
            m_glassPane.repaint();
         }
         else
         {
            m_viewProperties.setDragRegionStart(startX);
            m_viewProperties.setDragRegionEnd(currentX);
            m_viewProperties.setShouldShowDragRegion(true);
            m_glassPane.repaint();
         }
      }

      redispatchEvent(e);
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
            m_glassPane.repaint();
         }
         else
         {
            m_viewProperties.setDragRegionStart(startX);
            m_viewProperties.setDragRegionEnd(finalX);
            m_viewProperties.setShouldShowDragRegion(false);

            m_controller.zoomToDragRegion(startX, finalX);
            m_glassPane.repaint();
         }
      }
      else
      {
         m_viewProperties.setDragRegionStart(0);
         m_viewProperties.setDragRegionEnd(0);
         m_viewProperties.setShouldShowDragRegion(false);
         m_glassPane.repaint();
      }

      redispatchEvent(e);
      
   }

   private void redispatchEvent(MouseEvent e)
   {
      // Redispatch the event so buttons can receive it
      // TODO: Should check region the mouse is in
      Point glassPanePoint = e.getPoint();
      Container container = m_contentPane;
      container.dispatchEvent(e);
      Point containerPoint = SwingUtilities.convertPoint(
                                      m_glassPane,
                                      glassPanePoint,
                                      m_contentPane);
      if (containerPoint.y >= 0)
      {
          //The mouse event is probably over the content pane.
          //Find out exactly which component it's over.  
          Component component = 
              SwingUtilities.getDeepestComponentAt(
                                      container,
                                      containerPoint.x,
                                      containerPoint.y);

//          if (component instanceof JButton)
//          {
              //Forward events over the check box.
              Point componentPoint = SwingUtilities.convertPoint(
                                          m_glassPane,
                                          glassPanePoint,
                                          component);
              component.dispatchEvent(new MouseEvent(component,
                                                   e.getID(),
                                                   e.getWhen(),
                                                   e.getModifiers(),
                                                   componentPoint.x,
                                                   componentPoint.y,
                                                   e.getClickCount(),
                                                   e.isPopupTrigger()));
//          }
      }
//      else
//      {
//         Point menuPoint = SwingUtilities.convertPoint(
//               m_glassPane,
//               glassPanePoint,
//               m_controller.frame.getJMenuBar());
//         Component menuComponent = 
//               SwingUtilities.getDeepestComponentAt(
//                     m_controller.frame.getJMenuBar(),
//                                       menuPoint.x,
//                                       menuPoint.y);
//         menuComponent.dispatchEvent(e);
//      }
      
       
      //Update the glass pane if requested.
       m_glassPane.repaint();
   }

   @Override
   public void mouseClicked(MouseEvent e)
   {
      redispatchEvent(e);
   }

   @Override
   public void mouseEntered(MouseEvent e)
   {
      redispatchEvent(e);
   }

   @Override
   public void mouseExited(MouseEvent e)
   {
      redispatchEvent(e);
   }

   @Override
   public void mouseWheelMoved(MouseWheelEvent e)
   {
      redispatchEvent(e);
   }
   
   

}
