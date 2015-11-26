package org.wildstang.wildlog.viewer.renderers;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.wildstang.wildlog.viewer.models.DataConfig;

public class DataConfigCellRenderer implements ListCellRenderer<DataConfig>
{

   @Override
   public Component getListCellRendererComponent(
         JList<? extends DataConfig> list, DataConfig value, int index,
         boolean isSelected, boolean cellHasFocus)
   {
      JPanel cell = new JPanel();

      cell.setLayout(new BorderLayout());
      JLabel label = new JLabel(value.getKey());
      label.setForeground(value.getColor());
      cell.add(label, BorderLayout.WEST);
      // JLabel swatch = new JLabel();
      // swatch.setBackground(value.getColor());
      // cell.add(swatch, BorderLayout.CENTER);
      cell.setSize(200, 30);

      return cell;
   }

}
