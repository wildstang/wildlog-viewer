package org.wildstang.wildlog.viewer.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;

public class ResetZoomAction extends AbstractAction
{
   private static final long serialVersionUID = 1L;
   
   private ApplicationController m_controller;
   
   public ResetZoomAction(String name, ApplicationController controller)
   {
      super(name);
      m_controller = controller;
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      m_controller.resetZoomToDefault();
   }

}
