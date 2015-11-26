package org.wildstang.wildlog.viewer.renderers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class RendererFactory
{

   private static Map<Class<?>, Class<? extends LogRenderer>> renderers = new HashMap<>();

   public static void registerRenderer(Class<?> clazz,
         Class<? extends LogRenderer> rendererClass)
   {
      renderers.put(clazz, rendererClass);
   }

   public static LogRenderer getRendererForClass(Class<?> clazz)
   {
      Class<? extends LogRenderer> rendererClass = (Class<? extends LogRenderer>) renderers.get(clazz);
      if (rendererClass == null)
      {
         return null;
      }
      try
      {
         Constructor<? extends LogRenderer> rendererConstructor = rendererClass.getDeclaredConstructor();
         return (LogRenderer) rendererConstructor.newInstance();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return null;
      }
   }

   public static boolean hasRendererForClass(Class<?> clazz)
   {
      return (renderers.get(clazz) != null);
   }

}
