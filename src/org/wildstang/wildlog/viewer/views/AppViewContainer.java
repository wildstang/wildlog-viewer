package org.wildstang.wildlog.viewer.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.models.AppViewConfig;

public class AppViewContainer extends JPanel
{
   private ScrollBarPanel scrollBar;
   private TimelinePanel timeline;
   private TitleButtonPanel titlePanel;
   private final ApplicationController m_controller;
   private JPanel statusBar;
   private JLabel statusText;

   
   private ArrayList<DataPanel> m_dataPanels = new ArrayList<DataPanel>();
   
   public AppViewContainer(ApplicationController controller)
   {
      m_controller = controller;
      
//       MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
//       private int startX;
//       private int lastX;
//   
//       public void mouseMoved(MouseEvent e) {
//          m_controller.updateMousePosition(e.getXOnScreen(), e.getYOnScreen());
//       }
//   
//       public void mousePressed(MouseEvent e) {
//          startX = e.getXOnScreen();
//          m_controller.updateDragRegion(startX, startX, true);
//       }
//   
//       public void mouseDragged(MouseEvent e) {
//          int currentX = e.getXOnScreen();
//          // If we dragged to the left of the initial point, invert the points
//          if (Math.abs(currentX - startX) > 1) {
//             if (currentX < startX) {
//                m_controller.updateDragRegion(currentX, startX, true);
//             } else {
//                m_controller.updateDragRegion(startX, currentX, true);
//             }
//          }
//       }
//   
//       public void mouseReleased(MouseEvent e) {
//          int finalX = e.getXOnScreen();
//   
//          // If we dragged to the left of the initial point, invert the points
//          if (Math.abs(finalX - startX) > 1) {
//             if (finalX < startX) {
//                m_controller.updateDragRegion(finalX, startX, false);
//                m_controller.zoomToDragRegion(finalX, startX);
//             } else {
//                m_controller.updateDragRegion(startX, finalX, false);
//                m_controller.zoomToDragRegion(startX, finalX);
//             }
//          } else {
//             m_controller.updateDragRegion(0, 0, false);
//          }
//   
//       }
//    };
//   
//    addMouseMotionListener(mouseAdapter);
//    addMouseListener(mouseAdapter);
   }
   
//   int dragRegionStart, dragRegionEnd;
//   boolean shouldShowDragRegion = false;
//
//   int mouseX;
//   int mouseY;
//   private long viewStartTimestamp = -1;
//   private long viewEndTimestamp = -1;
//
//
//
//   @Override
//   protected void paintComponent(Graphics g)
//   {
//      super.paintComponent(g);
//
//      if (m_controller.getModel() == null)
//      {
//         return;
//      }
//
//      long startTimestamp, endTimestamp;
//      if (viewStartTimestamp == -1 || viewEndTimestamp == -1) {
//         startTimestamp = m_controller.getModel().getStartTimestamp();
//         endTimestamp = m_controller.getModel().getEndTimestamp();
//      } else {
//         startTimestamp = viewStartTimestamp;
//         endTimestamp = viewEndTimestamp;
//      }
//
//      drawTimeLine(g, startTimestamp, endTimestamp);
//
//   }
//
//   public void updateGraphView(long startTimestamp, long endTimestamp) {
//      viewStartTimestamp = startTimestamp;
//      viewEndTimestamp = endTimestamp;
//      repaint();
//   }
//
//   public void updateMousePosition(int posX, int posY) {
//      mouseX = posX;
//      mouseY = posY;
//      repaint();
//   }
//
//   public void updateDragRegion(int pxStart, int pxEnd, boolean shouldShowDragRegion) {
//      dragRegionStart = pxStart;
//      dragRegionEnd = pxEnd;
//      this.shouldShowDragRegion = shouldShowDragRegion;
//      repaint();
//   }
//
//   private void drawTimeLine(Graphics g, long startTimestamp, long endTimestamp)
//   {
//
//      
//      if (shouldShowDragRegion)
//      {
//         // Draw drag region, with the time scrubber positioned at the
//         // current mouse position
//
//         // Convert the drag region coordinates to local coordinates
//         // They are originally in the coordinate system of the containing
//         // DataPanel
//         int localPxDragRegionStart = dragRegionStart - getLocationOnScreen().x;
//         int localPxDragRegionEnd = dragRegionEnd - getLocationOnScreen().x;
//
//         System.out.println("localMouseX: " + localPxDragRegionStart
//               + "; dragRegionStart: " + dragRegionStart);
//
//         // Bound the drag region by the width of the box
//         if (localPxDragRegionStart < 0)
//         {
//            localPxDragRegionStart = 0;
//         }
//
//         if (localPxDragRegionEnd > getWidth())
//         {
//            localPxDragRegionEnd = getWidth();
//         }
//
//         // Draw drag region
//         Color transparentWhite = new Color(255, 255, 255, 150);
//         g.setColor(transparentWhite);
//         g.fillRect(localPxDragRegionStart, 0, localPxDragRegionEnd
//               - localPxDragRegionStart, getHeight());
//
//         // Compute where we should draw the labels (inside or outside the time
//         // line)
//         FontMetrics fMetrics = g.getFontMetrics();
//         String startTimestampString = Long.toString(mapMousePositionToTimestamp(localPxDragRegionStart, startTimestamp, endTimestamp));
//         String endTimestampString = Long.toString(mapMousePositionToTimestamp(localPxDragRegionEnd, startTimestamp, endTimestamp));
//
//         int startTimestampX;
//         int startStringWidth = SwingUtilities.computeStringWidth(fMetrics, startTimestampString);
//         if (localPxDragRegionStart - startStringWidth - 5 < 0)
//         {
//            // The label would extend past the start of the panel and we
//            // should draw it on the inside
//            startTimestampX = localPxDragRegionStart + 5;
//         }
//         else
//         {
//            // There's enough room on the outside
//            startTimestampX = localPxDragRegionStart - startStringWidth - 5;
//         }
//
//         int endTimestampX;
//         int endStringWidth = SwingUtilities.computeStringWidth(fMetrics, endTimestampString);
//         if (localPxDragRegionEnd + 5 + endStringWidth > getWidth())
//         {
//            // The label would extend past the start of the panel and we
//            // should draw it on the inside
//            endTimestampX = localPxDragRegionEnd - 5 - endStringWidth;
//         }
//         else
//         {
//            // There's enough room on the outside
//            endTimestampX = localPxDragRegionEnd + 5;
//         }
//
//         // Draw start line and label
//         g.setColor(Color.BLACK);
//         g.drawLine(localPxDragRegionStart, 0, localPxDragRegionStart, getHeight());
//         g.drawString(startTimestampString, startTimestampX, getHeight());
//
//         // Draw end line and label
//         g.drawLine(localPxDragRegionEnd, 0, localPxDragRegionEnd, getHeight());
//         g.drawString(endTimestampString, endTimestampX, getHeight());
//      }
//
//      g.setColor(Color.BLACK);
//      g.drawLine(mouseX, 0, mouseX, getHeight());
//   }
//
//   private long mapMousePositionToTimestamp(int mouseX, long startTimestamp,
//         long endTimestamp)
//   {
//      long deltaTime = endTimestamp - startTimestamp;
//      return (long) (startTimestamp + ((double) mouseX / (double) getWidth())
//            * deltaTime);
//   }
//   
   
   public void init(Color[] colors)
   {
      titlePanel = new TitleButtonPanel(m_controller);

      timeline = new TimelinePanel();
      timeline.setMinimumSize(new Dimension(0, 20));

      for (int i = 0; i < 8; i++)  // initial size
      {
         m_dataPanels.add(new DataPanel(m_controller, colors[i % colors.length]));
      }

      scrollBar = new ScrollBarPanel(m_controller);
      
      statusBar = new JPanel();
      statusBar.setLayout(new BorderLayout());

      Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.gray, Color.darkGray);
      Border margin = new EmptyBorder(5, 5, 5, 5);
      statusBar.setBorder(new CompoundBorder(border, margin));
      
      statusText = new JLabel("WildLog Started");
      statusBar.add(statusText, BorderLayout.WEST);
      statusText.setForeground(Color.black);

      layoutPanel();
   }

   private void layoutPanel()
   {
      removeAll();
      setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      
      // Add the title bar with buttons
      c.gridx = 0;
      c.gridy = 0;
      c.weighty = 0.0;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      add(titlePanel, c);
      
      // Add the timeline/scale
      c.gridy = 1;
      c.weighty = 0.0;
      add(timeline, c);
      c.weighty = 1.0;

      // Add the rows of data
      int i = 0;
      for (i = 0; i < m_dataPanels.size(); i++)
      {
         c.gridy = i + 2;
         add(m_dataPanels.get(i), c);
      }

      // add the scroll bar
      c.gridy = i + 2;
      c.weighty = 0.0;
      add(scrollBar, c);
      
      // Add the status bar
      c.gridy = i + 3;
      c.weighty = 0.0;
      add(statusBar, c);
   }

   public void updateViewConfig(AppViewConfig config)
   {
      m_dataPanels.clear();
      for (int i = 0; i < config.getSize(); i++)  // initial size
      {
         m_dataPanels.add(new DataPanel(m_controller, config.getElementAt(i).getColor(), config.getElementAt(i).getKey()));
      }

      layoutPanel();
   }
   
   public void setStatus(String text)
   {
      statusText.setText(text);
   }

   public ScrollBarPanel getScrollBar()
   {
      return scrollBar;
   }

   public TimelinePanel getTimeline()
   {
      return timeline;
   }

   public ArrayList<DataPanel> getDataPanels()
   {
      return m_dataPanels;
   }
   
   
}
