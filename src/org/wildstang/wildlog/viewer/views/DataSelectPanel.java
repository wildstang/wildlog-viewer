package org.wildstang.wildlog.viewer.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.models.IOInfo;

public class DataSelectPanel extends JPanel
{

   private static final String DEFAULT_DATA_TYPE = "Data Type";

   JTextField dataKey;
   JTextField dataKeyType;
   JTextField direction;
   IOInfo m_info;

   public DataSelectPanel(Color color, IOInfo info)
   {

      m_info = info;
      JPanel leftPanel = new JPanel();
      leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

      JPanel rightPanel = new JPanel();
      rightPanel.setBackground(color);
      
      dataKey = new JTextField("", 8);
      dataKey.setEditable(false);
      dataKey.setMaximumSize(new Dimension(10000, 20));
      leftPanel.add(dataKey);

      dataKeyType = new JTextField("", 8);
      dataKeyType.setEditable(false);
      dataKeyType.setMaximumSize(new Dimension(10000, 20));
      leftPanel.add(dataKeyType);
      
      direction = new JTextField("", 8);
      direction.setEditable(false);
      direction.setMaximumSize(new Dimension(10000, 20));
      leftPanel.add(direction);
      
      setInfo(m_info);
      
      leftPanel.setBackground(color);
      
      this.setLayout(new BorderLayout());
      add(leftPanel, BorderLayout.CENTER);
      add(rightPanel, BorderLayout.EAST);
      
      setBorder(BorderFactory.createLineBorder(Color.BLACK));
      setMinimumSize(new Dimension(ApplicationController.DATA_SELECT_PANEL_WIDTH, 0));
      setMaximumSize(new Dimension(ApplicationController.DATA_SELECT_PANEL_WIDTH, 0));
   }

   public void setInfo(IOInfo info)
   {
      if (info != null)
      {
         dataKey.setText(info.getName());
         dataKeyType.setText(info.getType());
         if (info.isInput())
         {
            direction.setText("Input");
         }
         else
         {
            direction.setText("Output");
         }
      }
      else
      {
         dataKey.setText("");
         dataKeyType.setText("");
         direction.setText("");
      }
   }

   public void setKey(String key)
   {
      dataKey.setText(key);
   }

   public void clearAllFields()
   {
      setKey("");
      setInfo(null);
   }
}
