package org.wildstang.wildlog.viewer.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.models.AppViewConfig;
import org.wildstang.wildlog.viewer.views.ConfigWizardDialog;

public class SetCurrentViewAction extends AbstractAction
{

   private ApplicationController m_controller;
   private AppViewConfig m_config;
   private ConfigWizardDialog m_dialog;
   
   public SetCurrentViewAction(String name, ApplicationController controller, AppViewConfig config, ConfigWizardDialog parent)
   {
      super(name);
      m_controller = controller;
      m_config = config;
      m_dialog = parent;
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      m_controller.updateView(m_config);
      if (m_dialog != null)
      {
         m_dialog.dispose();
      }
   }

}
