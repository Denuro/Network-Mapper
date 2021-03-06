/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.panels;

import netmap.components.PaintMapPanel;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import netmap.components.EquipmentsListCellRenderer;
import netmap.database.managers.EquipmentManager;
import netmap.database.managers.PositionManager;
import netmap.database.managers.ScreenCableManager;
import netmap.database.managers.ScreenEquipmentManager;
import netmap.database.managers.ScreenPortManager;
import netmap.entities.Equipment;
import netmap.entities.Position;
import netmap.entities.ScreenCable;
import netmap.entities.ScreenEquipment;
import netmap.entities.ScreenItem;
import netmap.entities.ScreenPort;
import netmap.util.Application;
import netmap.util.Properties;
import netmap.util.Util;

/**
 *
 * @author darlan.ullmann
 */
public class MapPanel extends javax.swing.JPanel
{

    public static final int STATUS_VIEW = 0;
    public static final int STATUS_EDIT = 1;

    public static final int TOOL_CURSOR = 0;
    public static final int TOOL_EQUIPMENT = 1;
    public static final int TOOL_CABLE = 2;
    public static final int TOOL_VLAN = 3;

    private int status;
    private int selectedTool;
    private int dividerLocation;

    private boolean searchEnabled = false;

    final PaintMapPanel paintMap;
    private ScreenCable currentCable;

    /**
     * Creates new form MapPanel
     */
    public MapPanel()
    {
        initComponents();

        paintMap = new PaintMapPanel();
        scrollPaint.setViewportView(paintMap);

        listEquipments.setCellRenderer(new EquipmentsListCellRenderer());

        searchEnabled = Properties.getInstance().getBoolean("lastSearchEnabled", false);
        updateSearch();

        dividerLocation = Properties.getInstance().getInt("mapDividerLocation", 240);
        splitPane.setDividerLocation(dividerLocation);
        setSelectedTool(Properties.getInstance().getInt("lastSelectedTool", TOOL_CURSOR));

        paintMap.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                switch (selectedTool)
                {
                    case TOOL_CURSOR:
                        if (e.getClickCount() > 1)
                        {
                            ScreenItem clicked = paintMap.getClicked(e.getX(), e.getY());
                            if (clicked != null)
                            {
                                if (clicked instanceof ScreenEquipment)
                                {
                                    ScreenEquipment screenEquipment = (ScreenEquipment) clicked;
                                    ScreenEquipmentPanel screenEquipmentPanel = new ScreenEquipmentPanel(screenEquipment);

                                    screenEquipmentPanel.setSize(600, 500);
                                    screenEquipmentPanel.setLocationRelativeTo(Application.getInstance().getMainFrame());
                                    screenEquipmentPanel.setVisible(true);

                                    int code = screenEquipmentPanel.getCode();
                                    if (code == JOptionPane.OK_OPTION)
                                    {
                                        screenEquipment = screenEquipmentPanel.getScreenEquipment();

                                        ScreenEquipmentManager.getInstance().save(screenEquipment);
                                        paintMap.addDisplay(screenEquipment);

                                        for (ScreenPort screenPort : screenEquipmentPanel.getScreenPorts())
                                        {
                                            ScreenPortManager.getInstance().save(screenPort);
                                        }
                                    }
                                    else if (code == 4)
                                    {
                                        paintMap.removeDisplay(screenEquipment);

                                        for (ScreenPort screenPort : screenEquipmentPanel.getScreenPorts())
                                        {
                                            ScreenPortManager.getInstance().delete(screenPort);
                                        }
                                        ScreenEquipmentManager.getInstance().delete(screenEquipment);
                                    }

                                    paintMap.repaint();
                                    setSelectedTool(TOOL_CURSOR);
                                }
                                else if (clicked instanceof ScreenCable)
                                {
                                    ScreenCable screenCable = (ScreenCable) clicked;
                                    ScreenCablePanel screenCablePanel = new ScreenCablePanel(screenCable);

                                    screenCablePanel.setSize(600, 300);
                                    screenCablePanel.setLocationRelativeTo(Application.getInstance().getMainFrame());
                                    screenCablePanel.setVisible(true);

                                    int code = screenCablePanel.getCode();
                                    if (screenCablePanel.getCode() == JOptionPane.OK_OPTION)
                                    {
                                        screenCable = screenCablePanel.getScreenCable();
                                        ScreenCableManager.getInstance().save(screenCable);
                                    }
                                    else if (code == 4)
                                    {
                                        paintMap.removeDisplay(screenCable);
                                        ScreenCableManager.getInstance().delete(screenCable);
                                    }

                                    paintMap.repaint();
                                    setSelectedTool(TOOL_CURSOR);
                                }
                            }
                        }
                        break;
                    case TOOL_EQUIPMENT:
                        if (listEquipments.getSelectedIndex() >= 0)
                        {
                            ScreenEquipmentPanel screenEquipmentPanel = new ScreenEquipmentPanel(listEquipments.getSelectedValue());

                            screenEquipmentPanel.setSize(600, 500);
                            screenEquipmentPanel.setLocationRelativeTo(Application.getInstance().getMainFrame());
                            screenEquipmentPanel.setVisible(true);

                            if (screenEquipmentPanel.getCode() == JOptionPane.OK_OPTION)
                            {
                                ScreenEquipment screenEquipment = screenEquipmentPanel.getScreenEquipment();
                                Position position = new Position(e.getX(), e.getY());
                                PositionManager.getInstance().save(position);

                                screenEquipment.setPositionId(position.getId());
                                ScreenEquipmentManager.getInstance().save(screenEquipment);
                                paintMap.addDisplay(screenEquipment);

                                for (ScreenPort screenPort : screenEquipmentPanel.getScreenPorts())
                                {
                                    screenPort.setEquipmentId(screenEquipment.getId());
                                    ScreenPortManager.getInstance().save(screenPort);
                                }
                            }

                            paintMap.repaint();
                            setSelectedTool(TOOL_CURSOR);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(
                                    Application.getInstance().getMainFrame(),
                                    "Selectione um equipamento na listagem",
                                    "Adicionar equipamento",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                        break;
                    case TOOL_CABLE:
                        if (currentCable == null)
                        {
                            currentCable = new ScreenCable();
                            currentCable.setStartPosition(new Position(e.getX(), e.getY()));
                            paintMap.addDisplay(currentCable);
                        }
                        else
                        {
                            Position start = currentCable.getStartPosition();
                            Position end = new Position(e.getX(), e.getY());
                            ScreenCablePanel screenCablePanel = new ScreenCablePanel();

                            screenCablePanel.setSize(600, 300);
                            screenCablePanel.setLocationRelativeTo(Application.getInstance().getMainFrame());
                            screenCablePanel.setVisible(true);

                            if (screenCablePanel.getCode() == JOptionPane.OK_OPTION)
                            {
                                
                                ScreenCable screenCable = screenCablePanel.getScreenCable();
                                screenCable.setStartPosition(start);
                                screenCable.setEndPosition(end);

                                PositionManager.getInstance().save(screenCable.getStartPosition());
                                PositionManager.getInstance().save(screenCable.getEndPosition());
                                ScreenCableManager.getInstance().save(screenCable);

                                paintMap.addDisplay(screenCable);
                            }

                            paintMap.repaint();
                            setSelectedTool(TOOL_CURSOR);
                            paintMap.removeDisplay(currentCable);
                            currentCable = null;
                        }
                        break;
                }
            }
        });
        paintMap.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                if (selectedTool == TOOL_CABLE && currentCable != null)
                {
                    paintMap.repaint();
                }
            }
        });
        /*paintMap.addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                int level = Math.min(4, Math.max(paintMap.getZoom().ordinal() + e.getWheelRotation() * -1, 0));
                paintMap.setZoom(PaintMapPanel.ZOOM.values()[level]);
            }
        });*/

        status = STATUS_VIEW;
    }

    public void load()
    {
        String search = tfSearchEquipment.getText().trim();
        List<Equipment> equipments;

        if (search.isEmpty())
        {
            equipments = EquipmentManager.getInstance().getAll();
        }
        else
        {
            equipments = EquipmentManager.getInstance().getAllByDescription(search);
        }
        listEquipments.setListData(equipments.toArray(new Equipment[equipments.size()]));
    }

    public int getStatus()
    {
        return status;
    }

    public boolean setStatus(int status)
    {
        if (this.status != status)
        {
            if (this.status == STATUS_VIEW && status == STATUS_EDIT)
            {
                //check if map can be edited
                this.status = status;

                return true;
            }
            else if (this.status == STATUS_EDIT && status == STATUS_VIEW)
            {
                JOptionPane.showMessageDialog(
                        Application.getInstance().getMainFrame(),
                        "Mapa salvo!",
                        "Salvar",
                        JOptionPane.INFORMATION_MESSAGE
                );
                // check for validation errors and save
                this.status = status;

                return true;
            }
        }

        return false;
    }

    public void setSelectedTool(int tool)
    {
        reset();

        selectedTool = tool;

        switch (selectedTool)
        {
            case TOOL_CURSOR:
                btToolCursor.setSelected(true);
                break;
            case TOOL_EQUIPMENT:
                btToolEquipment.setSelected(true);
                panelEquipments.setVisible(true);
                EventQueue.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        splitPane.setDividerLocation(dividerLocation);
                    }
                });
                break;
            case TOOL_CABLE:
                btToolCable.setSelected(true);
                break;
            case TOOL_VLAN:
                btToolVlan.setSelected(true);
                break;
        }
    }

    private void updateSearch()
    {
        btToolSearch.setSelected(searchEnabled);
        panelSearch.setVisible(searchEnabled);
    }

    private void reset()
    {
        if (panelEquipments.isVisible())
        {
            dividerLocation = splitPane.getDividerLocation();
        }

        btToolCursor.setSelected(false);
        panelEquipments.setVisible(false);
        btToolEquipment.setSelected(false);
        btToolCable.setSelected(false);
        btToolVlan.setSelected(false);

        paintMap.removeDisplay(currentCable);
        currentCable = null;
    }

    public void saveProperties()
    {
        if (panelEquipments.isVisible())
        {
            dividerLocation = splitPane.getDividerLocation();
        }

        Properties.getInstance().setInt("lastSelectedTool", selectedTool);
        Properties.getInstance().setInt("mapDividerLocation", dividerLocation);
    }
    
    public void exportImage()
    {
        JFileChooser chooser = new JFileChooser(Util.getFileChooserDirectory());
        chooser.setFileFilter(Util.getImageFileFilter());
        int ret = chooser.showSaveDialog(Application.getInstance().getMainFrame());
        if (ret == JFileChooser.APPROVE_OPTION)
        {
            BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = img.createGraphics();
            paintMap.paintAll(g2d);
            g2d.dispose();
            
            try
            {
                ImageIO.write(img, "png", chooser.getSelectedFile());
            }
            catch (IOException e)
            {
                Util.handleException(e);
            }
            
            Util.saveFileChooserDirectory(chooser.getSelectedFile());
            
            JOptionPane.showMessageDialog(Application.getInstance().getMainFrame(), "Arquivo exportado com sucesso");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        panelMapArea = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        btToolCursor = new javax.swing.JButton();
        btToolEquipment = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btToolCable = new javax.swing.JButton();
        btToolVlan = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btToolSearch = new javax.swing.JButton();
        splitPane = new javax.swing.JSplitPane();
        panelEquipments = new javax.swing.JPanel();
        scrollEquipments = new javax.swing.JScrollPane();
        listEquipments = new javax.swing.JList<>();
        panelSearchEquipment = new javax.swing.JPanel();
        tfSearchEquipment = new javax.swing.JTextField();
        btSearchEquipment = new javax.swing.JButton();
        panelInternal = new javax.swing.JPanel();
        scrollPaint = new javax.swing.JScrollPane();
        panelSearch = new javax.swing.JPanel();
        tfSearch = new javax.swing.JTextField();
        btSearch = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setLayout(new java.awt.GridBagLayout());

        panelMapArea.setLayout(new java.awt.GridBagLayout());

        toolBar.setFloatable(false);
        toolBar.setOrientation(javax.swing.SwingConstants.VERTICAL);
        toolBar.setRollover(true);

        btToolCursor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cursor.png"))); // NOI18N
        btToolCursor.setToolTipText("Selecionar");
        btToolCursor.setFocusable(false);
        btToolCursor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btToolCursor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btToolCursor.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btToolCursorActionPerformed(evt);
            }
        });
        toolBar.add(btToolCursor);

        btToolEquipment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/switch-minicon.png"))); // NOI18N
        btToolEquipment.setToolTipText("Adicionar equipamento");
        btToolEquipment.setFocusable(false);
        btToolEquipment.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btToolEquipment.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btToolEquipment.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btToolEquipmentActionPerformed(evt);
            }
        });
        toolBar.add(btToolEquipment);
        toolBar.add(jSeparator1);

        btToolCable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/link-2-icon.png"))); // NOI18N
        btToolCable.setToolTipText("Adicionar cabo");
        btToolCable.setFocusable(false);
        btToolCable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btToolCable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btToolCable.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btToolCableActionPerformed(evt);
            }
        });
        toolBar.add(btToolCable);

        btToolVlan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/social-normal-icon.png"))); // NOI18N
        btToolVlan.setToolTipText("Adicionar vlan");
        btToolVlan.setFocusable(false);
        btToolVlan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btToolVlan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btToolVlan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btToolVlanActionPerformed(evt);
            }
        });
        toolBar.add(btToolVlan);
        toolBar.add(jSeparator2);

        btToolSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        btToolSearch.setToolTipText("Busca");
        btToolSearch.setFocusable(false);
        btToolSearch.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btToolSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btToolSearch.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btToolSearchActionPerformed(evt);
            }
        });
        toolBar.add(btToolSearch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panelMapArea.add(toolBar, gridBagConstraints);

        splitPane.setDividerLocation(180);

        panelEquipments.setLayout(new java.awt.GridBagLayout());

        listEquipments.setBackground(java.awt.SystemColor.menu);
        listEquipments.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollEquipments.setViewportView(listEquipments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelEquipments.add(scrollEquipments, gridBagConstraints);

        panelSearchEquipment.setLayout(new java.awt.GridBagLayout());

        tfSearchEquipment.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                tfSearchEquipmentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        panelSearchEquipment.add(tfSearchEquipment, gridBagConstraints);

        btSearchEquipment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        btSearchEquipment.setPreferredSize(new java.awt.Dimension(23, 23));
        btSearchEquipment.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btSearchEquipmentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panelSearchEquipment.add(btSearchEquipment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        panelEquipments.add(panelSearchEquipment, gridBagConstraints);

        splitPane.setLeftComponent(panelEquipments);

        panelInternal.setLayout(new java.awt.GridBagLayout());

        scrollPaint.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaint.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        panelInternal.add(scrollPaint, gridBagConstraints);

        panelSearch.setLayout(new java.awt.GridBagLayout());

        tfSearch.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panelSearch.add(tfSearch, gridBagConstraints);

        btSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        btSearch.setPreferredSize(new java.awt.Dimension(23, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelSearch.add(btSearch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelInternal.add(panelSearch, gridBagConstraints);

        splitPane.setRightComponent(panelInternal);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelMapArea.add(splitPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panelMapArea, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btToolSearchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btToolSearchActionPerformed
    {//GEN-HEADEREND:event_btToolSearchActionPerformed
        searchEnabled = !searchEnabled;
        Properties.getInstance().setBoolean("lastSearchEnabled", searchEnabled);
        updateSearch();
    }//GEN-LAST:event_btToolSearchActionPerformed

    private void btToolCursorActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btToolCursorActionPerformed
    {//GEN-HEADEREND:event_btToolCursorActionPerformed
        setSelectedTool(TOOL_CURSOR);
    }//GEN-LAST:event_btToolCursorActionPerformed

    private void btToolEquipmentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btToolEquipmentActionPerformed
    {//GEN-HEADEREND:event_btToolEquipmentActionPerformed
        setSelectedTool(TOOL_EQUIPMENT);
    }//GEN-LAST:event_btToolEquipmentActionPerformed

    private void btToolCableActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btToolCableActionPerformed
    {//GEN-HEADEREND:event_btToolCableActionPerformed
        setSelectedTool(TOOL_CABLE);
    }//GEN-LAST:event_btToolCableActionPerformed

    private void btToolVlanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btToolVlanActionPerformed
    {//GEN-HEADEREND:event_btToolVlanActionPerformed
        setSelectedTool(TOOL_VLAN);
    }//GEN-LAST:event_btToolVlanActionPerformed

    private void tfSearchEquipmentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_tfSearchEquipmentActionPerformed
    {//GEN-HEADEREND:event_tfSearchEquipmentActionPerformed
        load();
    }//GEN-LAST:event_tfSearchEquipmentActionPerformed

    private void btSearchEquipmentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btSearchEquipmentActionPerformed
    {//GEN-HEADEREND:event_btSearchEquipmentActionPerformed
        load();
    }//GEN-LAST:event_btSearchEquipmentActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSearch;
    private javax.swing.JButton btSearchEquipment;
    private javax.swing.JButton btToolCable;
    private javax.swing.JButton btToolCursor;
    private javax.swing.JButton btToolEquipment;
    private javax.swing.JButton btToolSearch;
    private javax.swing.JButton btToolVlan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JList<Equipment> listEquipments;
    private javax.swing.JPanel panelEquipments;
    private javax.swing.JPanel panelInternal;
    private javax.swing.JPanel panelMapArea;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelSearchEquipment;
    private javax.swing.JScrollPane scrollEquipments;
    private javax.swing.JScrollPane scrollPaint;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextField tfSearch;
    private javax.swing.JTextField tfSearchEquipment;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
}
