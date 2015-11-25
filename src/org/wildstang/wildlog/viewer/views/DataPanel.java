package org.wildstang.wildlog.viewer.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.models.LogsModel;

public class DataPanel extends JPanel
{

   public ApplicationController controller;

   GraphingPanel graphPanel;
   DataSelectPanel dataSelectPanel;

   private LogsModel model;
   private String m_key = "";

   public DataPanel(final ApplicationController controller, Color c, String key)
   {
      this(controller, c);
      m_key = key;
   }

   public DataPanel(final ApplicationController controller, Color c)
   {
      this.controller = controller;

      setLayout(new GridBagLayout());
      GridBagConstraints j = new GridBagConstraints();
      j.gridx = 0;
      j.gridy = 0;
      j.fill = GridBagConstraints.VERTICAL;
      j.anchor = GridBagConstraints.LINE_START;
      j.weightx = 0.0;
      j.weighty = 1.0;
      dataSelectPanel = new DataSelectPanel(c);
      dataSelectPanel.setPreferredSize(new Dimension(200, 500));
      add(dataSelectPanel, j);

      j.gridx = 1;
      j.gridy = 0;
      j.fill = GridBagConstraints.BOTH;
      j.weightx = 1;
      j.weighty = 1.0;
      graphPanel = new GraphingPanel(c);
      add(graphPanel, j);

   }

   public DataSelectPanel getDataSelectPanel()
   {
      return dataSelectPanel;
   }

   public GraphingPanel getGraphingPanel()
   {
      return graphPanel;
   }

   public void updateModel(LogsModel model)
   {
      this.model = model;
      graphPanel.updateModel(model);
      dataKeyUpdated(m_key);
   }

   public void updateGraphPanelZoomAndScroll(long startTimestamp,
         long endTimestamp)
   {
      graphPanel.updateGraphView(startTimestamp, endTimestamp);
   }

   public void dataKeyUpdated(String newKey)
   {
      if (newKey != null && !newKey.equals(""))
      {
         Class<?> clazz = model.getClassTypeForKey(newKey);
         graphPanel.updateSelectedDataKey(newKey, clazz);
         dataSelectPanel.setKey(newKey);
         dataSelectPanel.setDataTypeText(clazz.getName());
      }
   }

   public void updateMousePosition(int posX, int posY)
   {
      graphPanel.updateMousePosition(posX, posY);
   }

   public void updateGraphView(long startTimestamp, long endTimestamp)
   {
      graphPanel.updateGraphView(startTimestamp, endTimestamp);
   }
}
