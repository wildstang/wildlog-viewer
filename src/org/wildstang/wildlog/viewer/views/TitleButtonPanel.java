package org.wildstang.wildlog.viewer.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.wildstang.wildlog.viewer.controllers.ApplicationController;

public class TitleButtonPanel extends JPanel
{

   private ApplicationController controller;

   WildStangLogoPanel logoPanel = new WildStangLogoPanel();

   ApplicationControlPanel controlPanel;
   public static JButton readFileStart = null;
   
   public TitleButtonPanel(ApplicationController c)
   {
      controller = c;

      readFileStart = new JButton(controller.getActionManager().getOpenLog());

      setLayout(new BorderLayout());
      JPanel paneLeft = new JPanel();
      paneLeft.setLayout(new FlowLayout(FlowLayout.LEADING));
      paneLeft.add(readFileStart);

      JPanel paneRight = new JPanel();
      paneRight.setLayout(new FlowLayout(FlowLayout.TRAILING));

      controlPanel = new ApplicationControlPanel(controller);
      paneRight.add(controlPanel);
      add(paneLeft, BorderLayout.WEST);
      try
      {
         Image wsLogo = ImageIO.read(getClass().getResourceAsStream("/wildstang-logo.png"));
         // Scale this to 50px tall
         double scaleFactor = 50.0 / (double) wsLogo.getHeight(null);
         wsLogo = wsLogo.getScaledInstance((int) ((double) wsLogo.getWidth(null) * scaleFactor), 50, 0);
         add(new JLabel(new ImageIcon(wsLogo)), BorderLayout.CENTER);
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
      }
      add(paneRight, BorderLayout.EAST);

      JPanel paneCenter = new JPanel();
      paneCenter.setLayout(new FlowLayout(FlowLayout.TRAILING));
      add(paneCenter, BorderLayout.CENTER);
   }

}
