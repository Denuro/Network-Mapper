/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.components;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import netmap.util.Util;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import netmap.entities.Brand;

/**
 * List cell render for Brands
 * @author Darlan
 */
public class BrandsListCellRenderer implements ListCellRenderer<Brand>
{
    private final int iconSize;

    /**
     * Create a new BrandListCellRendered with icon size of 60
     */
    public BrandsListCellRenderer()
    {
        this(60);
    }

    /**
     * Create a new BrandListCellRendered
     * @param iconSize 
     */
    public BrandsListCellRenderer(int iconSize)
    {
        this.iconSize = iconSize;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Brand> list, Brand brand, int index, boolean isSelected, boolean cellHasFocus)
    {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel lbImg = new JLabel();
        JLabel lbDesc = new JLabel();
        
        if (brand != null)
        {
            BufferedImage icon = brand.getImage();

            if (icon != null)
            {
                lbImg.setIcon(new ImageIcon(Util.resizeImage(icon, iconSize)));
            }
            else
            {
                try
                {
                    BufferedImage noIcon = ImageIO.read(getClass().getResource("/img/forbidden_list.png"));
                    lbImg.setIcon(new ImageIcon(Util.resizeImage(noIcon, iconSize)));
                }
                catch (Exception e)
                {
                    Util.handleException(e);
                }
            }
            lbImg.setPreferredSize(new Dimension(iconSize, iconSize));
            lbDesc.setText(brand.getDescription());
        }
        
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

        panel.add(lbImg, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 3), 0, 0));
        panel.add(lbDesc, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

        return panel;
    }
}
