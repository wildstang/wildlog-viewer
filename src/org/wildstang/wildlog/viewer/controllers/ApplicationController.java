package org.wildstang.wildlog.viewer.controllers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import org.wildstang.wildlog.viewer.actions.ActionManager;
import org.wildstang.wildlog.viewer.models.AppViewConfig;
import org.wildstang.wildlog.viewer.models.DataConfig;
import org.wildstang.wildlog.viewer.models.JSONFileReader;
import org.wildstang.wildlog.viewer.models.LogsModel;
import org.wildstang.wildlog.viewer.views.AppFrame;
import org.wildstang.wildlog.viewer.views.AppGlassPane;
//import org.wildstang.wildlog.viewer.views.AppGlassPane;
import org.wildstang.wildlog.viewer.views.AppViewContainer;
import org.wildstang.wildlog.viewer.views.DataPanel;
import org.wildstang.wildlog.viewer.views.ViewProperties;

public class ApplicationController
{

   public static final int DATA_SELECT_PANEL_WIDTH = 200;
   private int NUM_COLOURS = 8;

   public static AppFrame frame;
   private AppViewContainer m_viewContainer;
   private AppViewConfig m_viewConfig;
   private ActionManager m_actionManager;
   private ViewProperties m_viewProperties = new ViewProperties();

   private LogsModel m_model = null;
   private File m_currentFile;
   private File m_currentViewFile;
   
   public Color[] theWSRainbow = new Color[NUM_COLOURS];

   // Controllers
   public static GraphPanelController graphPanelViewController;

   public ApplicationController()
   {
      m_actionManager = new ActionManager(this);
   }
   
   public void initializeApplication()
   {
      try
      {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception exc)
      {

      }
      
      theWSRainbow[0] = new Color(255, 0, 0);
      theWSRainbow[1] = new Color(255, 127, 0);
      theWSRainbow[2] = new Color(255, 255, 0);
      theWSRainbow[3] = new Color(0, 255, 0);
      theWSRainbow[4] = new Color(0, 127, 255);
      theWSRainbow[5] = new Color(0, 0, 255);
      theWSRainbow[6] = new Color(127, 0, 255);
      theWSRainbow[7] = new Color(255, 0, 255);


      initFrameComponents();

      // Init controllers
      graphPanelViewController = new GraphPanelController(this, m_viewContainer.getScrollBar());
      frame.addMouseWheelListener(graphPanelViewController);
   }

   private void initFrameComponents()
   {
      frame = new AppFrame(this);
      m_viewContainer = new AppViewContainer(this);
      frame.setContentPane(m_viewContainer);
      m_viewContainer.init(theWSRainbow);

      AppGlassPane glassPane = new AppGlassPane(this);
      GlassPaneMouseListener listener = new GlassPaneMouseListener(this, glassPane, m_viewContainer);
      glassPane.addMouseListener(listener);
      glassPane.addMouseMotionListener(listener);
      frame.setGlassPane(glassPane);
      frame.getGlassPane().setVisible(true);
      glassPane.setOpaque(false);
      
      frame.pack();
      frame.setVisible(true);
   }


   public AppViewConfig getViewConfig()
   {
      return m_viewConfig;
   }
   
   public File getCurrentViewFile()
   {
      return m_currentViewFile;
   }
   
   public void setCurrentViewFile(File file)
   {
      m_currentViewFile = file;
   }
   
   public void updateView(AppViewConfig config)
   {
      m_viewConfig = config;

      m_viewContainer.updateViewConfig(m_viewConfig);
      
      List<DataConfig> panelConfigList = config.getDataConfig();

      updateLogsModel(m_model);
      
      for (int i = 0; i < panelConfigList.size(); i++)
      {
         m_viewContainer.getDataPanels().get(i).dataKeyUpdated(panelConfigList.get(i).getKey());
      }
      
      m_viewContainer.repaint();
   }
   
   public void updateMousePosition(int x, int y)
   {
      for (int i = 0; i < m_viewContainer.getDataPanels().size(); i++)
      {
         m_viewContainer.getDataPanels().get(i).updateMousePosition(x, y);
      }
      m_viewContainer.getTimeline().updateMousePosition(x, y);
   }

   public void updateLogsModel(LogsModel model)
   {
      m_model = model;
      for (int i = 0; i < m_viewContainer.getDataPanels().size(); i++)
      {
         m_viewContainer.getDataPanels().get(i).updateModel(model);
      }
      graphPanelViewController.updateModel(model);
   }

   public void updateGraphPanelZoomAndScroll(long startTimestamp,
         long endTimestamp)
   {
      for (int i = 0; i < m_viewContainer.getDataPanels().size(); i++)
      {
         m_viewContainer.getDataPanels().get(i).updateGraphPanelZoomAndScroll(startTimestamp, endTimestamp);
      }
      m_viewContainer.getTimeline().updateGraphPanelZoomAndScroll(startTimestamp, endTimestamp);

      frame.getAppGlassPane().updateGraphView(startTimestamp, endTimestamp);
   }

   public void zoomToDragRegion(int pxStart, int pxEnd)
   {
      // Map pixels to timestamps
      long startTimestamp = m_viewContainer.getDataPanels().get(0).getGraphingPanel().mapAbsoluteMousePositionToTimestamp(pxStart);
      long endTimestamp = m_viewContainer.getDataPanels().get(0).getGraphingPanel().mapAbsoluteMousePositionToTimestamp(pxEnd);
      if (startTimestamp == endTimestamp)
      {
         // Don't drag to a region 0 time wide! Abort! Abort!
         return;
      }
      graphPanelViewController.zoomAndScrollToTimestampRange(startTimestamp, endTimestamp);
   }

   public void clearAllFields()
   {
      for (int i = 0; i < m_viewContainer.getDataPanels().size(); i++)
      {
         m_viewContainer.getDataPanels().get(i).getDataSelectPanel().clearAllFields();
      }
   }

   public void resetZoomToDefault()
   {
      graphPanelViewController.resetDefaultZoom();
   }

   public void errorReadingFile()
   {
      JOptionPane.showMessageDialog(frame, "Error reading the selected file. Please try another file.", "Error", JOptionPane.ERROR_MESSAGE);
   }

   public File getCurrentFile()
   {
      return m_currentFile;
   }
   
   public void setCurrentFile(File file)
   {
      m_currentFile = file;
   }
   
   public void attempLoadDataFromFile(final File file)
   {
      setCurrentFile(file);

      final JDialog dlg = new JDialog(frame, "Loading Log File", true);
      JProgressBar dpb = new JProgressBar(0, 500);
      dpb.setIndeterminate(true);
      dlg.add(BorderLayout.CENTER, dpb);
      dlg.add(BorderLayout.NORTH, new JLabel("Loading..."));
      dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
      dlg.setSize(300, 75);
      dlg.setLocationRelativeTo(frame);

      Thread t = new Thread(new Runnable()
      {

         @Override
         public void run()
         {
            try
            {
               // LogsModel model = Deserializer.loadLogsModelFromFile(file);
               LogsModel model = JSONFileReader.loadLogsModelFromFile(file);
               ApplicationController.this.updateLogsModel(model);
               m_viewContainer.setStatus(file.getCanonicalPath());
            }
            catch (Exception e)
            {
               e.printStackTrace();
               ApplicationController.this.errorReadingFile();
            }
            dlg.setVisible(false);
            System.out.println("Load complete");
         }
      });
      t.start();
      // Showing the dialog suspends this thread. Fire off the background task
      // before doing anything.
      dlg.setVisible(true);
   }

   public LogsModel getModel()
   {
      return m_model;
   }
   
   public Color getColor(int index)
   {
      return theWSRainbow[index % NUM_COLOURS];
   }

   public boolean isFileLoaded()
   {
      return m_model != null;
   }

   public ActionManager getActionManager()
   {
      return m_actionManager;
   }

   public ViewProperties getViewProperties()
   {
      return m_viewProperties;
   }

   
   
}
