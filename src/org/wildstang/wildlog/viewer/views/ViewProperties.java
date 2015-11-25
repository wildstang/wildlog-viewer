package org.wildstang.wildlog.viewer.views;

public class ViewProperties
{
   int m_dragRegionStart;
   int m_dragRegionEnd;
   boolean m_shouldShowDragRegion = false;

   int m_mouseX;
   int m_mouseY;
   private long m_viewStartTimestamp = -1;
   private long m_viewEndTimestamp = -1;

   
   public ViewProperties()
   {
      
   }


   public int getDragRegionStart()
   {
      return m_dragRegionStart;
   }


   public void setDragRegionStart(int dragRegionStart)
   {
      m_dragRegionStart = dragRegionStart;
   }


   public int getDragRegionEnd()
   {
      return m_dragRegionEnd;
   }


   public void setDragRegionEnd(int dragRegionEnd)
   {
      m_dragRegionEnd = dragRegionEnd;
   }


   public boolean isShouldShowDragRegion()
   {
      return m_shouldShowDragRegion;
   }


   public void setShouldShowDragRegion(boolean shouldShowDragRegion)
   {
      m_shouldShowDragRegion = shouldShowDragRegion;
   }


   public int getMouseX()
   {
      return m_mouseX;
   }


   public void setMouseX(int mouseX)
   {
      m_mouseX = mouseX;
   }


   public int getMouseY()
   {
      return m_mouseY;
   }


   public void setMouseY(int mouseY)
   {
      m_mouseY = mouseY;
   }


   public long getViewStartTimestamp()
   {
      return m_viewStartTimestamp;
   }


   public void setViewStartTimestamp(long viewStartTimestamp)
   {
      m_viewStartTimestamp = viewStartTimestamp;
   }


   public long getViewEndTimestamp()
   {
      return m_viewEndTimestamp;
   }


   public void setViewEndTimestamp(long viewEndTimestamp)
   {
      m_viewEndTimestamp = viewEndTimestamp;
   }
   
   
}
