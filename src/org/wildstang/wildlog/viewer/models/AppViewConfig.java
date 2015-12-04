package org.wildstang.wildlog.viewer.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

public class AppViewConfig extends AbstractListModel<DataConfig> implements Serializable
{
   private static final long serialVersionUID = 1L;

   private ArrayList<DataConfig> m_configList = new ArrayList<DataConfig>();
   private ArrayList<String> m_keys = new ArrayList<String>();
   public AppViewConfig()
   {
      
   }

   public List<DataConfig> getDataConfig()
   {
      return m_configList;
   }

   public void addConfig(DataConfig p_config)
   {
      m_configList.add(p_config);
      m_keys.add(p_config.getKey());
      fireIntervalAdded(this, getSize() - 1, getSize() - 1);
   }

   @Override
   public int getSize()
   {
      return m_configList.size();
   }

   @Override
   public DataConfig getElementAt(int index)
   {
      return m_configList.get(index);
   }
   
   
   public void remove(int index)
   {
      m_configList.remove(index);
      m_keys.remove(index);
      fireIntervalRemoved(this, index, index);
   }
   
   public boolean contains(String p_key)
   {
      return m_keys.contains(p_key);
   }
}
