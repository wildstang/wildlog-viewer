package org.wildstang.wildlog.viewer.actions;



import org.wildstang.wildlog.viewer.controllers.ApplicationController;

public class ActionManager
{
   private LaunchConfigurationAction m_configureAction;
   private ResetZoomAction m_resetZoom;
   private ResetViewAction m_clearAllFields;
   private SaveViewAction m_saveView;
   private ReadViewAction m_readView;
   private SelectFileAction m_openLog;
   
   public ActionManager(ApplicationController controller)
   {
      m_configureAction = new LaunchConfigurationAction("Configure", controller);
      m_resetZoom = new ResetZoomAction("Reset Zoom", controller);
      m_clearAllFields = new ResetViewAction("Clear All Fields", controller);
      m_saveView = new SaveViewAction("Save View", controller);
      m_readView = new ReadViewAction("Open View", controller);
      m_openLog = new SelectFileAction("Open Log File", controller);
   }

   public LaunchConfigurationAction getConfigureAction()
   {
      return m_configureAction;
   }

   public ResetZoomAction getResetZoom()
   {
      return m_resetZoom;
   }

   public ResetViewAction getClearAllFields()
   {
      return m_clearAllFields;
   }

   public SaveViewAction getSaveView()
   {
      return m_saveView;
   }

   public ReadViewAction getReadView()
   {
      return m_readView;
   }

   public SelectFileAction getOpenLog()
   {
      return m_openLog;
   }

   
   
}
