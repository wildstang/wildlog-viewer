package org.wildstang.wildlog.viewer.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.views.ConfigWizardDialog;

public class LaunchConfigurationAction extends AbstractAction
{
   private static final long serialVersionUID = 1L;

   private ApplicationController m_controller;
   
   
   public LaunchConfigurationAction(String name, ApplicationController controller)
   {
      super(name);
      m_controller = controller;
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      if (m_controller.isFileLoaded())
      {
         ConfigWizardDialog configDialog = new ConfigWizardDialog(m_controller);
         configDialog.setSize(400, 400);
         configDialog.setVisible(true);
      }
      else
      {
         JOptionPane.showMessageDialog(null,
               "Please open a file before configuring the view.",
               "No file loaded",
               JOptionPane.ERROR_MESSAGE);      }
   }

}
