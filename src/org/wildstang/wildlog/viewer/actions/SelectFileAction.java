package org.wildstang.wildlog.viewer.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;

public class SelectFileAction extends AbstractAction
{
   private JFrame m_parent;
   private ApplicationController m_controller;

   public SelectFileAction(String name, ApplicationController controller)
   {
      super(name);
      m_controller = controller;
      m_parent = controller.frame;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      JFileChooser chooser = new JFileChooser();
      File startFile = null;
      
      if (m_controller.getCurrentFile() == null)
      {
         startFile = new File(System.getProperty("user.home"));
      }
      else
      {
         startFile = m_controller.getCurrentFile();
      }
      
      chooser.setCurrentDirectory(chooser.getFileSystemView().getParentDirectory(startFile));
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setDialogTitle("Select the log file");

      File file;
      if (chooser.showOpenDialog(m_parent) == JFileChooser.APPROVE_OPTION)
      {
         file = chooser.getSelectedFile();
      }
      else
      {
         file = null;
      }
      if (file != null)
      {
         m_controller.attempLoadDataFromFile(file);
      }
   }

}
