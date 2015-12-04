package org.wildstang.wildlog.viewer.views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

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
      JScrollPane availableScrollPane = new JScrollPane(availableFields);
      JScrollPane selectedScrollPane = new JScrollPane(selectedFields);
      container.add(availableScrollPane, BorderLayout.WEST);
      container.add(selectedScrollPane, BorderLayout.EAST);
      
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
               addSelectedFields();
            }
         }
      });
      
      availableFields.addKeyListener(new KeyListener()
      {
         
         @Override
         public void keyTyped(KeyEvent p_e)
         {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public void keyReleased(KeyEvent p_e)
         {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public void keyPressed(KeyEvent p_e)
         {
            if (p_e.getKeyCode() == KeyEvent.VK_ENTER)
            {
               addSelectedFields();
            }
            
         }
      });
   }
   
   public void addSelectedFields()
   {
      ListModel<String> model = availableFields.getModel();
      int[] indices = availableFields.getSelectedIndices();
      
      for (int i = 0; i < indices.length; i++)
      {
         String key = model.getElementAt(i);
         if (!m_viewConfig.contains(key))
         {
            DataConfig dataConfig = new DataConfig(key, m_controller.getColor(m_viewConfig.getSize()));
            m_viewConfig.addConfig(dataConfig);
         }
      }
   }
   
   
   
   
}
