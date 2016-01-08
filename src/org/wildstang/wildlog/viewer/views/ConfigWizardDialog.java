package org.wildstang.wildlog.viewer.views;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
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
   JList<DataConfig> selectedFields;
   ApplicationController m_controller;
   AppViewConfig m_viewConfig;
   
   public ConfigWizardDialog(ApplicationController controller)
   {
      super(controller.frame, "Configure View");
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
      selectedFields = new JList<DataConfig>(m_viewConfig);
      selectedFields.setCellRenderer(new DataConfigCellRenderer());

      JScrollPane availableScrollPane = new JScrollPane(availableFields);
      JScrollPane selectedScrollPane = new JScrollPane(selectedFields);
      
      JButton addButton = new JButton("Add >>");
      JButton removeButton = new JButton("<< Remove");
      addButton.addActionListener(new ActionListener()
      {
         
         @Override
         public void actionPerformed(ActionEvent p_arg0)
         {
            addSelectedFields();
         }
      });
      removeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent p_arg0)
         {
            removeSelectedFields();
         }
      });

      
      container.setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      
      c.fill = GridBagConstraints.BOTH;
      c.gridx = 0;
      c.gridy = 0;
      c.gridwidth = 2;
      c.gridheight = 7;
      c.weightx = 1;
      c.weighty = 1;
      
      container.add(availableScrollPane, c);
      
      c.gridx = 3;
      c.gridy = 0;
      container.add(selectedScrollPane, c);
      
      c.gridx = 2;
      c.gridy = 3;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.weightx = 0;
      c.weighty = 0;
      
      container.add(addButton, c);
      c.gridx = 2;
      c.gridy = 4;
      container.add(removeButton, c);
      
      JButton okButton = new JButton(new SetCurrentViewAction("Update View", controller, m_viewConfig, this));
      
      c.gridx = 1;
      c.gridy = 7;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.weightx = 0.0;
      c.weighty = 0.0;
      container.add(okButton, c);
      
      
      availableFields.addMouseListener(new MouseAdapter()
      {
         public void mouseClicked(MouseEvent e)
         {
            if (e.getClickCount() == 2)
            {
               addSelectedFields();
            }
         }
      });
      
      availableFields.addKeyListener(new KeyAdapter()
      {
         @Override
         public void keyPressed(KeyEvent p_e)
         {
            if (p_e.getKeyCode() == KeyEvent.VK_ENTER)
            {
               addSelectedFields();
            }
            
         }
      });
      
      selectedFields.addMouseListener(new MouseAdapter()
      {
         public void mouseClicked(MouseEvent e)
         {
            if (e.getClickCount() == 2)
            {
               removeSelectedFields();
            }
         }
      });
      
      selectedFields.addKeyListener(new KeyAdapter()
      {
         @Override
         public void keyPressed(KeyEvent p_e)
         {
            if (p_e.getKeyCode() == KeyEvent.VK_ENTER)
            {
               removeSelectedFields();
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
         String key = model.getElementAt(indices[i]);
         if (!m_viewConfig.contains(key))
         {
            DataConfig dataConfig = new DataConfig(key, m_controller.getColor(m_viewConfig.getSize()));
            m_viewConfig.addConfig(dataConfig);
         }
      }
   }
   
   public void removeSelectedFields()
   {
      AppViewConfig model = (AppViewConfig)selectedFields.getModel();
      int[] indices = selectedFields.getSelectedIndices();
      
      for (int i = indices.length - 1; i >= 0; i--)
      {
         model.remove(indices[i]);
      }
   }
   
   
   
}
