/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import netmap.database.managers.EquipmentTypeManager;
import netmap.entities.Equipment;
import netmap.util.Util;

/**
 * Cell Render for Equipments
 * @author darlan.ullmann
 */
public class EquipmentsListCellRenderer implements ListCellRenderer<Equipment>
{

    @Override
    public Component getListCellRendererComponent( JList<? extends Equipment> list, Equipment equipment, int index, boolean isSelected, boolean cellHasFocus )
    {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel lbImg = new JLabel();
        JLabel lbDesc = new JLabel();
        JLabel lbType = new JLabel();
        
        BufferedImage icon = equipment.getImage();
        
        if (icon != null)
        {
            lbImg.setIcon(new ImageIcon(Util.resizeImage(icon, 60)));
        }
        else
        {
            lbImg.setIcon(new ImageIcon(getClass().getResource("/img/forbidden_list.png")));
        }
        lbImg.setPreferredSize(new Dimension(60, 60));
        lbDesc.setText(equipment.getDescription());
        lbType.setText(EquipmentTypeManager.getInstance().get(equipment.getEquipmentTypeId()).getDescription());
        
        if (isSelected)
        {
            panel.setBackground(list.getSelectionBackground());
            panel.setForeground(list.getSelectionForeground());
        }
        else
        {
            panel.setBackground(list.getBackground());
            panel.setForeground(list.getForeground());   
        }

        panel.add(lbImg, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 3), 0, 0));
        panel.add(lbDesc, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 3, 5), 0, 0));
        panel.add(lbDesc, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

        return panel;
    }
}
