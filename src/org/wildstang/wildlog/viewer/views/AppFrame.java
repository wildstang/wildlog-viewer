package org.wildstang.wildlog.viewer.views;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;

public class AppFrame extends JFrame
{
   
   private JMenuBar m_menuBar;
   
   public AppFrame(ApplicationController controller)
   {
      super("WildLog");
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setExtendedState(JFrame.MAXIMIZED_BOTH);
      
      m_menuBar = new JMenuBar();
      JMenu fileMenu = new JMenu("File");
      fileMenu.setMnemonic(KeyEvent.VK_F);
      JMenuItem openLogItem = new JMenuItem(controller.getActionManager().getOpenLog());
      openLogItem.setMnemonic(KeyEvent.VK_L);
      openLogItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
      fileMenu.add(openLogItem);
      JMenuItem openViewItem = new JMenuItem(controller.getActionManager().getReadView());
      openViewItem.setMnemonic(KeyEvent.VK_O);
      openViewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
      fileMenu.add(openViewItem);
      JMenuItem saveViewItem = new JMenuItem(controller.getActionManager().getSaveView());
      saveViewItem.setMnemonic(KeyEvent.VK_S);
      saveViewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
      fileMenu.add(saveViewItem);
      
      m_menuBar.add(fileMenu);
      
      JMenu viewMenu = new JMenu("View");
      viewMenu.setMnemonic(KeyEvent.VK_V);
      JMenuItem resetZoom = new JMenuItem(controller.getActionManager().getResetZoom());
      resetZoom.setMnemonic(KeyEvent.VK_R);
      resetZoom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
      viewMenu.add(resetZoom);
      JMenuItem clearFieldsItem = new JMenuItem(controller.getActionManager().getClearAllFields());
      clearFieldsItem.setMnemonic(KeyEvent.VK_X);
      clearFieldsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
      viewMenu.add(clearFieldsItem);
      
      m_menuBar.add(viewMenu);
      
      
      
      this.setJMenuBar(m_menuBar);
   }
   
   public JMenuBar getJMenuBar()
   {
      return m_menuBar;
   }


}
