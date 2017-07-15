/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import netmap.database.managers.EquipmentManager;
import netmap.database.managers.PositionManager;
import netmap.database.managers.ScreenCableManager;
import netmap.database.managers.ScreenEquipmentManager;
import netmap.database.managers.ScreenPortManager;
import netmap.entities.Port;
import netmap.entities.Position;
import netmap.entities.ScreenCable;
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

    public enum ZOOM
    {
        z1(0.25), z2(0.5), z3(1), z4(2), z5(4);

        private final double zoom;

        private ZOOM(double zoom)
        {
            this.zoom = zoom;
        }

        public double getZoom()
        {
            return zoom;
        }
    };

    private final BufferedImage etherPort = Util.readImage(getClass().getResource("/img/port-ether.png"));
    private final BufferedImage fiberPort = Util.readImage(getClass().getResource("/img/port-fiber.png"));

    private final List<ScreenItem> displayItems;
    private final HashMap<Shape, ScreenItem> itemShapes;
    private ZOOM zoom;

    public PaintMapPanel()
    {
        displayItems = new ArrayList<>();
        for (ScreenEquipment screenEquipment : ScreenEquipmentManager.getInstance().getAll())
        {
            displayItems.add(screenEquipment);
        }
        for (ScreenCable screenCable : ScreenCableManager.getInstance().getAll())
        {
            displayItems.add(screenCable);
        }
        itemShapes = new HashMap<>();
        zoom = ZOOM.z3;
    }

    public void addDisplay(ScreenItem display)
    {
        displayItems.add(display);
        repaint();
    }

    public void removeDisplay(ScreenItem display)
    {
        displayItems.remove(display);
        repaint();
    }

    public void setZoom(ZOOM zoom)
    {
        this.zoom = zoom;
        itemShapes.clear();
        repaint();
    }

    public ZOOM getZoom()
    {
        return zoom;
    }

    public ScreenItem getClicked(int x, int y)
    {
        ScreenItem screenItem = null;
        for (Shape shape : itemShapes.keySet())
        {
            if (shape.contains(x, y) || shape.intersects(x - 4, y - 4, 8, 8))
            {
                screenItem = itemShapes.get(shape);
            }
        }
        
        return screenItem;
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setFont(new Font(Font.SERIF, Font.PLAIN, (int) (12 * zoom.getZoom())));
        FontMetrics fm = g2d.getFontMetrics();

        for (ScreenItem screenItem : displayItems)
        {
            if (screenItem instanceof ScreenCable)
            {
                ScreenCable screenCable = (ScreenCable)screenItem;
                
                Position startPosition = screenCable.getStartPosition();
                Position endPosition;
                if (screenCable.getEndPosition() != null)
                {
                    endPosition = screenCable.getEndPosition();
                }
                else
                {
                    endPosition = new Position(getMousePosition().x, getMousePosition().y);
                }
                
                Shape line = new Line2D.Double(startPosition.getX(), startPosition.getY(), endPosition.getX(), endPosition.getY());
                if (!itemShapes.containsValue(screenItem))
                {
                    itemShapes.put(line, screenItem);
                }
                g2d.setColor(new Color(66, 200, 66));
                g2d.setStroke(new BasicStroke((float)(5*zoom.getZoom())));
                g2d.draw(line);
            }
            else if (screenItem instanceof ScreenEquipment)
            {
                ScreenEquipment screenEquipment = (ScreenEquipment) screenItem;
                Position position = PositionManager.getInstance().get(screenEquipment.getPositionId());
                
                Point upperLeft = new Point();
                Point downRight = new Point();

                BufferedImage equipmentImage = Util.resizeImageWidth(
                        EquipmentManager.getInstance().get(screenEquipment.getEquipmentId()).getImage(), (int) (150 * zoom.getZoom())
                );
                g2d.drawImage(equipmentImage, position.getX(), position.getY(), this);

                String title = screenEquipment.getName();
                g2d.setColor(Color.black);
                g2d.drawString(title,
                        position.getX() + (equipmentImage.getWidth() / 2) - fm.stringWidth(title) / 2,
                        position.getY() - fm.getHeight()
                );
                upperLeft.x = position.getX();
                upperLeft.y = position.getY() - fm.getHeight() - fm.getHeight();
                downRight.x = position.getX() + equipmentImage.getWidth();

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
                    portImg = Util.resizeImageWidth(
                            portImg, (int) (10 * zoom.getZoom())
                    );
                    int portImageWidth = (int)(portImg.getWidth()+portImg.getWidth()*0.2);
                    int width = lines[currentLine] * portImageWidth;
                    int y = position.getY() + equipmentImage.getHeight() + ((currentLine) * portImageWidth);
                    g2d.drawImage(portImg,
                            position.getX() + ((equipmentImage.getWidth() - width) / 2) + ((currentPort) * portImageWidth),
                            position.getY() + equipmentImage.getHeight() + ((currentLine) * portImageWidth),
                            this
                    );
                    currentPort++;
                    if (downRight.y < y + portImg.getHeight())
                    {
                        downRight.y = y + portImg.getHeight();
                    }
                    if (lines[currentLine] == currentPort)
                    {
                        currentLine++;
                        currentPort = 0;
                    }
                }
                
                Shape shape = new Rectangle2D.Double(upperLeft.x, upperLeft.y, downRight.x - upperLeft.x, downRight.y - upperLeft.y);
                itemShapes.put(shape, screenItem);
                //g2d.setColor(Color.red);
                //g2d.draw(shape);
            }
        }
    }
}
