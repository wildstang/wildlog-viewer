package org.wildstang.wildlog.viewer.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.models.AppViewConfig;

public class SaveViewAction extends AbstractAction
{
   private ApplicationController m_controller;
   private JFrame m_appFrame;
   
   public SaveViewAction(String name, ApplicationController controller)
   {
      super(name);
      m_controller = controller;;
      m_appFrame = controller.frame;
      
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      File file = null;
      
      if (m_controller.getCurrentViewFile() == null)
      {
         // Show file selection dialog
         File current = m_controller.getCurrentFile();
         JFileChooser fc = null;
         if (current != null)
         {
            fc = new JFileChooser(current.getParentFile());
         }
         else
         {
            fc = new JFileChooser();
         }
         int returnVal = fc.showSaveDialog(m_appFrame);

         if (returnVal == JFileChooser.APPROVE_OPTION) {
             file = fc.getSelectedFile();
             //This is where a real application would open the file.
         } else {
         }
      }
      
      
      // Save the file
      FileOutputStream fos = null;
      
      if (file != null)
      {
         try
         {
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(m_controller.getViewConfig());
            oos.flush();
            oos.close();
         }
         catch (FileNotFoundException e1)
         {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         catch (IOException e1)
         {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
      }      
   }

}
