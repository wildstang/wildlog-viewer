package org.wildstang.wildlog.viewer.models;

public class IOInfo
{
   private String m_name;
   private String m_type;
   private boolean m_direction;
   
   public IOInfo(String name, String type, boolean direction)
   {
      m_name = name;
      m_type = type;
      m_direction = direction;
   }

   public String getName()
   {
      return m_name;
   }

   public String getType()
   {
      return m_type;
   }

   public boolean isInput()
   {
      return m_direction;
   }
   
   
}
