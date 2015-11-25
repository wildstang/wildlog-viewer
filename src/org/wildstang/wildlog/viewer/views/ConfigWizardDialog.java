package org.wildstang.wildlog.viewer.views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;

import org.wildstang.wildlog.viewer.actions.SetCurrentViewAction;
import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.models.AppViewConfig;
import org.wildstang.wildlog.viewer.models.DataConfig;
import org.wildstang.wildlog.viewer.renderers.DataConfigCellRenderer;

public class ConfigWizardDialog extends JDialog
{
   
   JList availableFields;
   ApplicationController m_controller;
   AppViewConfig m_viewConfig;
   
   public ConfigWizardDialog(ApplicationController controller)
   {
      m_controller = controller;
      Container container = getContentPane();
      if (m_controller.getViewConfig() != null)
      {
         m_viewConfig = m_controller.getViewConfig();
      }
      else
      {
         m_viewConfig = new AppViewConfig();
      }
      
      availableFields = new JList<>(controller.getModel());
      JList<DataConfig> selectedFields = new JList<DataConfig>(m_viewConfig);
      selectedFields.setCellRenderer(new DataConfigCellRenderer());
      
      container.setLayout(new BorderLayout());
      container.add(availableFields, BorderLayout.WEST);
      container.add(selectedFields, BorderLayout.EAST);
      
      JButton okButton = new JButton(new SetCurrentViewAction("Update View", controller, m_viewConfig, this));
      container.add(okButton, BorderLayout.SOUTH);
      
      
      availableFields.addMouseListener(new MouseListener()
      {
         
         @Override
         public void mouseReleased(MouseEvent e)
         {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public void mousePressed(MouseEvent e)
         {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public void mouseExited(MouseEvent e)
         {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public void mouseEntered(MouseEvent e)
         {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public void mouseClicked(MouseEvent e)
         {
            if (e.getClickCount() == 2)
            {
               String key = (String)availableFields.getModel().getElementAt(availableFields.getSelectedIndex());
               DataConfig dataConfig = new DataConfig(key, m_controller.getColor(m_viewConfig.getSize()));
               m_viewConfig.addConfig(dataConfig);
            }
         }
      });
   }
   
   
   
   
}
