package org.wildstang.wildlog.viewer.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

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
   }
   
   public void init(Color[] colors)
   {
      titlePanel = new TitleButtonPanel(m_controller);

      timeline = new TimelinePanel(m_controller);
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
