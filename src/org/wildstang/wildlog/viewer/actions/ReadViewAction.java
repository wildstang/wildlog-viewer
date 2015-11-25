package org.wildstang.wildlog.viewer.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;
import org.wildstang.wildlog.viewer.models.AppViewConfig;

public class ReadViewAction extends AbstractAction
{

   private ApplicationController m_controller;
   private JFrame m_appFrame;
   
   public ReadViewAction(String name, ApplicationController controller)
   {
      super(name);
      m_controller = controller;
      m_appFrame = controller.frame;
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      if (m_controller.getModel() == null)
      {
         JOptionPane.showMessageDialog(m_appFrame, "No log file is currently loaded", "No log file!", JOptionPane.ERROR_MESSAGE);
      }
      else
      {
         File currentFileLocation = m_controller.getCurrentViewFile();
         File currentDir = null;
         
         if (currentFileLocation != null)
         {
            currentDir = currentFileLocation.getParentFile();
         }
         else
         {
            currentDir = m_controller.getCurrentFile().getParentFile();
         }
         
         JFileChooser fc = null;
         
         if (currentDir != null)
         {
            fc = new JFileChooser(currentDir);
         }
         else
         {
            fc = new JFileChooser();
         }
         
         int returnVal = fc.showOpenDialog(m_appFrame);
   
         File fileToRead = null;
         
         if (returnVal == JFileChooser.APPROVE_OPTION) {
             fileToRead = fc.getSelectedFile();
             //This is where a real application would open the file.
         } else {
         }
         
         FileInputStream fis = null;
         
         try
         {
            if (fileToRead != null)
            {
               fis = new FileInputStream(fileToRead);
               ObjectInputStream ois = new ObjectInputStream(fis);
               
               AppViewConfig config = (AppViewConfig)ois.readObject();
               ois.close();
               
               m_controller.setCurrentViewFile(fileToRead);
               m_controller.updateView(config);
            }
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
         catch (ClassNotFoundException e1)
         {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
      }
   }

}
