package org.wildstang.wildlog.viewer.models;

import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;

public class LogsModel extends AbstractListModel<String>
{

   private long startTimestamp, endTimestamp;
   private Map<String, List<DataPoint>> data;
   private String[] keys;

   public LogsModel(long startTimestamp, long endTimestamp,
         Map<String, List<DataPoint>> data, String[] allKeys)
   {
      this.startTimestamp = startTimestamp;
      this.endTimestamp = endTimestamp;
      this.data = data;
      this.keys = allKeys;
   }

   public long getStartTimestamp()
   {
      return startTimestamp;
   }

   public long getEndTimestamp()
   {
      return endTimestamp;
   }

   public List<DataPoint> getDataPointsForKey(String key)
   {
      return data.get(key);
   }

   public String[] getAllKeys()
   {
      return keys;
   }

   public Class<?> getClassTypeForKey(String key)
   {
      List<DataPoint> dataForKey = data.get(key);
      return dataForKey.get(0).getObject().getClass();
   }

   @Override
   public int getSize()
   {
      return keys.length;
   }

   @Override
   public String getElementAt(int index)
   {
      return keys[index];
   }

}
