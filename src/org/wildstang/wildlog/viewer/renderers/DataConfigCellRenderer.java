package org.wildstang.wildlog.viewer.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.wildstang.wildlog.viewer.models.DataConfig;

public class DataConfigCellRenderer implements ListCellRenderer<DataConfig>
{

   @Override
   public Component getListCellRendererComponent(JList<? extends DataConfig> list, DataConfig value, int index, boolean isSelected, boolean cellHasFocus)
   {
      JPanel cell = new JPanel();

      cell.setLayout(new BorderLayout());
      
      JLabel label = new JLabel(value.getKey());
      label.setOpaque(true);
      if (isSelected)
      {
         label.setBackground(new Color(51, 153, 255));
         label.setForeground(Color.white);
      }
      else
      {
         label.setBackground(Color.white);
         label.setForeground(Color.black);
      }
      
      // Spacer
//      JPanel spacer = new JPanel();
//      spacer.setPreferredSize(new Dimension(7, 20));
      
//      cell.add(spacer, BorderLayout.CENTER);
      cell.add(label, BorderLayout.CENTER);
      
      JPanel swatch = new JPanel();
      swatch.setBackground(value.getColor());
      swatch.setPreferredSize(new Dimension(20, 20));
      
      cell.add(swatch, BorderLayout.WEST);
      cell.setSize(200, 20);

      return cell;
   }

}
