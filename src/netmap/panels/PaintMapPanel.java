/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import netmap.database.managers.EquipmentManager;
import netmap.database.managers.PositionManager;
import netmap.database.managers.ScreenEquipmentManager;
import netmap.database.managers.ScreenPortManager;
import netmap.entities.Port;
import netmap.entities.Position;
import netmap.entities.ScreenItem;
import netmap.entities.ScreenPort;
import netmap.entities.ScreenEquipment;
import netmap.util.Util;

/**
 *
 * @author darlan.ullmann
 */
public class PaintMapPanel extends JPanel
{
    private final BufferedImage etherPort = Util.readImage(getClass().getResource("/img/port-ether.png"));
    private final BufferedImage fiberPort = Util.readImage(getClass().getResource("/img/port-fiber.png"));

    private final List<ScreenItem> displayItems;
    private int zoom;

    public PaintMapPanel()
    {
        displayItems = new ArrayList<>();
        for (ScreenEquipment screenEquipment : ScreenEquipmentManager.getInstance().getAll())
        {
            displayItems.add(screenEquipment);
        }
        zoom = 1;
    }

    public void addDisplay(ScreenItem display)
    {
        displayItems.add(display);
        repaint();
    }

    public void setZoom(int zoom)
    {
        this.zoom = zoom;
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setFont(new Font(Font.SERIF, Font.PLAIN, 12 * zoom));
        FontMetrics fm = g2d.getFontMetrics();

        for (ScreenItem screenItems : displayItems)
        {
            if (screenItems instanceof ScreenEquipment)
            {
                ScreenEquipment screenEquipment = (ScreenEquipment) screenItems;
                Position position = PositionManager.getInstance().get(screenEquipment.getPositionId());
                
                BufferedImage equipmentImage = Util.resizeImageWidth(
                        EquipmentManager.getInstance().get(screenEquipment.getEquipmentId()).getImage(), 150 * zoom
                );
                g2d.drawImage(equipmentImage, position.getX(), position.getY(), this);

                String title = screenEquipment.getName();
                g2d.setColor(Color.black);
                g2d.drawString(title,
                        position.getX() + (equipmentImage.getWidth() / 2) - fm.stringWidth(title) / 2,
                        position.getY() + fm.getHeight()
                );

                //calculate ammount of ports per line
                List<ScreenPort> screenPorts = ScreenPortManager.getInstance().getAllByEquipment(screenEquipment.getId());
                int portsAmmount = screenPorts.size();
                int totalLines = new BigDecimal(portsAmmount)
                        .divide(new BigDecimal(12), RoundingMode.UP)
                        .intValue();
                int[] lines = new int[totalLines];
                int currentTotal = 0;
                for (int i = 0; i < totalLines; i++)
                {
                    lines[i] = portsAmmount / totalLines;
                    currentTotal += portsAmmount / totalLines;
                }
                int i = 0;
                while (currentTotal < portsAmmount)
                {
                    lines[i++]++;
                    currentTotal++;
                }

                // print ports
                int currentLine = 0;
                int currentPort = 0;
                for (ScreenPort port : screenPorts)
                {
                    BufferedImage portImg;
                    switch (port.getType())
                    {
                        case Port.TYPE_ETHERNET:
                            portImg = etherPort;
                            break;
                        default:
                            portImg = fiberPort;
                    }
                    int width = lines[currentLine] * 12;
                    g2d.drawImage(portImg,
                            position.getX() + ((equipmentImage.getWidth() - width) / 2) + ((currentPort) * 12),
                            position.getY() + equipmentImage.getHeight() + ((currentLine) * 12),
                            this
                    );
                    currentPort++;
                    if (lines[currentLine] == currentPort)
                    {
                        currentLine++;
                        currentPort = 0;
                    }
                }
            }
        }
    }
}
