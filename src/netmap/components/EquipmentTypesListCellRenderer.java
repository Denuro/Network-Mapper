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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import netmap.entities.EquipmentType;

/**
 * Cell Render for Equipments
 *
 * @author darlan.ullmann
 */
public class EquipmentTypesListCellRenderer implements ListCellRenderer<EquipmentType>
{
    private int size;

    public EquipmentTypesListCellRenderer(int size)
    {
        this.size = size;
    }

    public EquipmentTypesListCellRenderer()
    {
        this(50);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends EquipmentType> list, EquipmentType equipment, int index, boolean isSelected, boolean cellHasFocus)
    {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel lbDesc = new JLabel();
        lbDesc.setText(equipment.getDescription());
        lbDesc.setMinimumSize(new Dimension(0, size));
        lbDesc.setPreferredSize(new Dimension(0, size));

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

        panel.add(lbDesc, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

        return panel;
    }
}
