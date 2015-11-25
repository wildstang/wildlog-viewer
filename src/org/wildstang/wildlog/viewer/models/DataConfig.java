package org.wildstang.wildlog.viewer.models;

import java.awt.Color;
import java.io.Serializable;

public class DataConfig implements Serializable
{
   private static final long serialVersionUID = 1L;

   private String m_key;
   
   private Color m_color;
   
   public DataConfig()
   {
      
   }
   
   public DataConfig(String p_key, Color p_color)
   {
      m_key = p_key;
      m_color = p_color;
   }

   public String getKey()
   {
      return m_key;
   }

   public void setKey(String key)
   {
      m_key = key;
   }

   public Color getColor()
   {
      return m_color;
   }

   public void setColor(Color color)
   {
      m_color = color;
   }
   
   
}
