package org.wildstang.wildlog.viewer.views;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;

public class ApplicationControlPanel extends JPanel
{

   JButton resetZoom;
   JButton clearAllFields;
   JButton configureButton;
   JButton saveView;
   JButton readView;

   public ApplicationControlPanel(ApplicationController controller)
   {
      configureButton = new JButton(controller.getActionManager().getConfigureAction());
      resetZoom = new JButton(controller.getActionManager().getResetZoom());
      clearAllFields = new JButton(controller.getActionManager().getClearAllFields());
      saveView = new JButton(controller.getActionManager().getSaveView());
      readView = new JButton(controller.getActionManager().getReadView());

      add(readView);
      add(saveView);
      add(configureButton);
      add(clearAllFields);
      add(resetZoom);
   }

}
