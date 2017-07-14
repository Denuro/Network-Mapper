/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.panels;

import netmap.components.EquipmentsListCellRenderer;
import netmap.util.Application;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import netmap.util.Util;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import netmap.components.BrandsListCellRenderer;
import netmap.components.EquipmentTypesListCellRenderer;
import netmap.components.PortTableModel;
import netmap.database.Database;
import netmap.database.managers.BrandManager;
import netmap.database.managers.EquipmentManager;
import netmap.database.managers.EquipmentTypeManager;
import netmap.database.managers.PortManager;
import netmap.entities.Brand;
import netmap.entities.Equipment;
import netmap.entities.EquipmentType;
import netmap.entities.Port;

/**
 *
 * @author darlan.ullmann
 */
public class EquipmentsPanel extends javax.swing.JPanel
{

    private boolean changed = false;
    private Equipment selectedEquipment = null;
    private BufferedImage selImage = null;

    /**
     * Creates new form EquipmentsPanel
     */
    public EquipmentsPanel()
    {
        initComponents();

        listEquipments.setCellRenderer(new EquipmentsListCellRenderer());
        listEquipments.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                loadSelectedValue();
            }
        });

        cbType.setRenderer(new EquipmentTypesListCellRenderer());
        cbBrand.setRenderer(new BrandsListCellRenderer(50));

        panelEquipmentEdit.setVisible(false);
    }

    public void load()
    {
        List<EquipmentType> equipmentTypes = EquipmentTypeManager.getInstance().getAll();
        cbType.setModel(new DefaultComboBoxModel<>(equipmentTypes.toArray(new EquipmentType[equipmentTypes.size()])));

        List<Brand> brands = BrandManager.getInstance().getAll();
        cbBrand.setModel(new DefaultComboBoxModel<>(brands.toArray(new Brand[brands.size()])));

        tfSearch.setText("");
        loadEquipments();

        clear();
    }

    public void loadEquipments()
    {
        String search = tfSearch.getText().trim();
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

    private void loadSelectedValue()
    {
        refresh();

        selectedEquipment = listEquipments.getSelectedValue();

        if (selectedEquipment != null)
        {
            lbTitle.setText("Equipamento " + selectedEquipment.getDescription());
            cbBrand.setSelectedItem(BrandManager.getInstance().get(selectedEquipment.getBrandId()));
            cbType.setSelectedItem(EquipmentTypeManager.getInstance().get(selectedEquipment.getEquipmentTypeId()));
            selImage = selectedEquipment.getImage();
            if (selImage != null)
            {
                lbImageSelected.setIcon(new ImageIcon(Util.resizeImage(selImage, 100)));
            }
            else
            {
                lbImageSelected.setIcon(new ImageIcon(getClass().getResource("/img/forbidden.png")));
            }
            lbImageSelected.setPreferredSize(new Dimension(100, 100));
            taInfo.setText(selectedEquipment.getInfo());
            tfName.setText(selectedEquipment.getDescription());

            ((PortTableModel) tablePorts.getModel()).setPorts(PortManager.getInstance().get(selectedEquipment));

            tfName.requestFocus();
        }
    }

    private void newEquipment()
    {
        refresh();

        lbTitle.setText("Novo Equipamento");
        lbImageSelected.setIcon(new ImageIcon(getClass().getResource("/img/forbidden.png")));
        lbImageSelected.setPreferredSize(new Dimension(100, 100));
        taInfo.setText("");
        tfName.setText("");
        ((PortTableModel) tablePorts.getModel()).clear();
        if (cbBrand.getItemCount() > 0)
        {
            cbBrand.setSelectedIndex(0);
        }
        if (cbType.getItemCount() > 0)
        {
            cbType.setSelectedIndex(0);
        }

        if (panelNoEquipmentSelected.isVisible())
        {
            panelNoEquipmentSelected.setVisible(false);
            panelEquipmentEdit.setVisible(true);
        }

        listEquipments.clearSelection();

        tfName.requestFocus();
    }

    private void save()
    {
        String error = "";

        if (tfName.getText().trim().isEmpty())
        {
            error = "O nome do equipamento é obrigatória.";
        }
        else if (tfName.getText().trim().length() > 200)
        {
            error = "O nome do equipamento deve conter no máximo 200 caracteres.";
        }
        else if (cbType.getSelectedIndex() < 0)
        {
            error = "O tipo do equipamento deve ser selecionado.";
        }
        else if (cbBrand.getSelectedIndex() < 0)
        {
            error = "Uma marca deve ser selecionada.";
        }
        else if (tablePorts.getRowCount() == 0)
        {
            error = "Pelo menos uma porta deve ser adicionada no equipamento.";
        }

        if (!error.isEmpty())
        {
            JOptionPane.showMessageDialog(
                    Application.getInstance().getMainFrame(),
                    error,
                    "Salvar equipamento",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (selectedEquipment == null)
        {
            selectedEquipment = new Equipment();
        }
        selectedEquipment.setDescription(tfName.getText());
        selectedEquipment.setBrandId(((Brand) cbBrand.getSelectedItem()).getId());
        selectedEquipment.setEquipmentTypeId(((EquipmentType) cbType.getSelectedItem()).getId());
        selectedEquipment.setInfo(taInfo.getText());
        selectedEquipment.setImage(selImage);

        EquipmentManager.getInstance().save(selectedEquipment);

        PortManager.getInstance().delete(selectedEquipment);

        List<Port> ports = ((PortTableModel) tablePorts.getModel()).getPorts();
        for (Port port : ports)
        {
            port.setEquipmentId(selectedEquipment.getId());
        }
        PortManager.getInstance().add(ports);

        changed = false;
        tfSearch.setText("");

        clear();
        loadEquipments();
    }

    private void refresh()
    {
        if (changed)
        {
            int opt = JOptionPane.showConfirmDialog(
                    Application.getInstance().getMainFrame(),
                    "O equipamento " + selectedEquipment.getDescription() + " foi alterada, deseja salvá-la?",
                    "Salvar equipamento",
                    JOptionPane.YES_NO_OPTION
            );

            if (opt == JOptionPane.YES_OPTION)
            {
                save();
                return;
            }
        }

        if (panelNoEquipmentSelected.isVisible())
        {
            panelNoEquipmentSelected.setVisible(false);
            panelEquipmentEdit.setVisible(true);
        }

        changed = false;
        selImage = null;
        selectedEquipment = null;

        btDelete.setVisible(listEquipments.getSelectedIndex() >= 0);
    }

    private void clear()
    {
        changed = false;
        selImage = null;
        selectedEquipment = null;
        listEquipments.clearSelection();

        panelNoEquipmentSelected.setVisible(true);
        panelEquipmentEdit.setVisible(false);
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

        splitPane = new javax.swing.JSplitPane();
        panelList = new javax.swing.JPanel();
        spBrands = new javax.swing.JScrollPane();
        listEquipments = new javax.swing.JList<>();
        btSearch = new javax.swing.JButton();
        tfSearch = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        panelEquipmentData = new javax.swing.JPanel();
        scrollEdit = new javax.swing.JScrollPane();
        panelEquipmentEdit = new javax.swing.JPanel();
        panelEquipmentDataTop = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        pnData = new javax.swing.JPanel();
        lbName = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        lbBrand = new javax.swing.JLabel();
        cbBrand = new javax.swing.JComboBox<>();
        lbType = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox<>();
        lbImage = new javax.swing.JLabel();
        pnImage = new javax.swing.JPanel();
        lbImageSelected = new javax.swing.JLabel();
        btImage = new javax.swing.JButton();
        panelFiller = new javax.swing.JPanel();
        lbInfo = new javax.swing.JLabel();
        scrollInfo = new javax.swing.JScrollPane();
        taInfo = new javax.swing.JTextArea();
        pnButton = new javax.swing.JPanel();
        btSave = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        panelPorts = new javax.swing.JPanel();
        scrollPorts = new javax.swing.JScrollPane();
        tablePorts = new javax.swing.JTable();
        addPorts = new javax.swing.JButton();
        deletePorts = new javax.swing.JButton();
        panelNoEquipmentSelected = new javax.swing.JPanel();
        lbNoEquipmentSelected = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        splitPane.setBorder(null);
        splitPane.setDividerLocation(250);

        panelList.setLayout(new java.awt.GridBagLayout());

        listEquipments.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        spBrands.setViewportView(listEquipments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 10, 10);
        panelList.add(spBrands, gridBagConstraints);

        btSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        btSearch.setMaximumSize(new java.awt.Dimension(23, 23));
        btSearch.setMinimumSize(new java.awt.Dimension(23, 23));
        btSearch.setPreferredSize(new java.awt.Dimension(23, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 10, 15);
        panelList.add(btSearch, gridBagConstraints);

        tfSearch.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                tfSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 10, 10);
        panelList.add(tfSearch, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add-1.png"))); // NOI18N
        jButton1.setText("Novo Equipamento");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 15);
        panelList.add(jButton1, gridBagConstraints);
        panelList.add(jPanel1, new java.awt.GridBagConstraints());

        jButton2.setText("Imprimir");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 15, 10);
        panelList.add(jButton2, gridBagConstraints);

        splitPane.setLeftComponent(panelList);

        panelEquipmentData.setLayout(new java.awt.GridBagLayout());

        scrollEdit.setBorder(null);
        scrollEdit.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelEquipmentEdit.setLayout(new java.awt.GridBagLayout());

        panelEquipmentDataTop.setLayout(new java.awt.GridBagLayout());

        lbTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTitle.setText("Novo Equipamento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        panelEquipmentDataTop.add(lbTitle, gridBagConstraints);

        pnData.setLayout(new java.awt.GridBagLayout());

        lbName.setFont(lbName.getFont().deriveFont(lbName.getFont().getStyle() | java.awt.Font.BOLD));
        lbName.setText("Nome:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        pnData.add(lbName, gridBagConstraints);

        tfName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                tfNameKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnData.add(tfName, gridBagConstraints);

        lbBrand.setFont(lbBrand.getFont().deriveFont(lbBrand.getFont().getStyle() | java.awt.Font.BOLD));
        lbBrand.setText("Marca:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnData.add(lbBrand, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnData.add(cbBrand, gridBagConstraints);

        lbType.setFont(lbType.getFont().deriveFont(lbType.getFont().getStyle() | java.awt.Font.BOLD));
        lbType.setText("Tipo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnData.add(lbType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnData.add(cbType, gridBagConstraints);

        lbImage.setText("Imagem:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnData.add(lbImage, gridBagConstraints);

        pnImage.setLayout(new java.awt.GridBagLayout());

        lbImageSelected.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbImageSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/forbidden.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        pnImage.add(lbImageSelected, gridBagConstraints);

        btImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        btImage.setText("Buscar imagem");
        btImage.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btImageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnImage.add(btImage, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnImage.add(panelFiller, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnData.add(pnImage, gridBagConstraints);

        lbInfo.setText("Informações:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnData.add(lbInfo, gridBagConstraints);

        taInfo.setColumns(20);
        taInfo.setRows(1);
        taInfo.setTabSize(4);
        taInfo.setToolTipText("");
        scrollInfo.setViewportView(taInfo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnData.add(scrollInfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 10, 15);
        panelEquipmentDataTop.add(pnData, gridBagConstraints);

        pnButton.setLayout(new java.awt.GridBagLayout());

        btSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/success.png"))); // NOI18N
        btSave.setText("Salvar");
        btSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btSaveActionPerformed(evt);
            }
        });
        pnButton.add(btSave, new java.awt.GridBagConstraints());

        btDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        btDelete.setText("Excluir");
        btDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnButton.add(btDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 15, 15);
        panelEquipmentDataTop.add(pnButton, gridBagConstraints);

        panelPorts.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Portas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        panelPorts.setPreferredSize(new java.awt.Dimension(571, 85));
        panelPorts.setLayout(new java.awt.GridBagLayout());

        tablePorts.setModel(new PortTableModel());
        scrollPorts.setViewportView(tablePorts);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        panelPorts.add(scrollPorts, gridBagConstraints);

        addPorts.setText("Adicionar");
        addPorts.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addPortsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panelPorts.add(addPorts, gridBagConstraints);

        deletePorts.setText("Excluir");
        deletePorts.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deletePortsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        panelPorts.add(deletePorts, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 10, 15);
        panelEquipmentDataTop.add(panelPorts, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelEquipmentEdit.add(panelEquipmentDataTop, gridBagConstraints);

        scrollEdit.setViewportView(panelEquipmentEdit);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelEquipmentData.add(scrollEdit, gridBagConstraints);

        panelNoEquipmentSelected.setLayout(new java.awt.GridBagLayout());

        lbNoEquipmentSelected.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbNoEquipmentSelected.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbNoEquipmentSelected.setText("Selecione um equipmento ao lado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 10);
        panelNoEquipmentSelected.add(lbNoEquipmentSelected, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelEquipmentData.add(panelNoEquipmentSelected, gridBagConstraints);

        splitPane.setRightComponent(panelEquipmentData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(splitPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void tfSearchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_tfSearchActionPerformed
    {//GEN-HEADEREND:event_tfSearchActionPerformed
        loadEquipments();
    }//GEN-LAST:event_tfSearchActionPerformed

    private void btImageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btImageActionPerformed
    {//GEN-HEADEREND:event_btImageActionPerformed
        JFileChooser chooser = new JFileChooser(Util.getFileChooserDirectory());
        chooser.setFileFilter(Util.getImageFileFilter());
        int ret = chooser.showOpenDialog(Application.getInstance().getMainFrame());
        if (ret == JFileChooser.APPROVE_OPTION)
        {
            BufferedImage img = Util.readImage(chooser.getSelectedFile());
            lbImageSelected.setIcon(new ImageIcon(Util.resizeImage(img, 100)));
            lbImageSelected.setPreferredSize(new Dimension(100, 100));

            changed = true;
            selImage = Util.resizeImage(img, 400);

            Util.saveFileChooserDirectory(chooser.getSelectedFile());
        }
    }//GEN-LAST:event_btImageActionPerformed

    private void tfNameKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tfNameKeyTyped
    {//GEN-HEADEREND:event_tfNameKeyTyped
        changed = true;
    }//GEN-LAST:event_tfNameKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        newEquipment();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btSaveActionPerformed
    {//GEN-HEADEREND:event_btSaveActionPerformed
        save();
    }//GEN-LAST:event_btSaveActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btDeleteActionPerformed
    {//GEN-HEADEREND:event_btDeleteActionPerformed
        if (selectedEquipment != null)
        {
            int opt = JOptionPane.showConfirmDialog(
                    Application.getInstance().getMainFrame(),
                    "Tem certeza que deseja excluir o equipmamento " + selectedEquipment.getDescription(),
                    "Excluir equipamento",
                    JOptionPane.YES_NO_OPTION
            );

            if (opt == JOptionPane.YES_OPTION)
            {
                PortManager.getInstance().delete(selectedEquipment);
                EquipmentManager.getInstance().delete(selectedEquipment);

                panelEquipmentEdit.setVisible(false);
                panelNoEquipmentSelected.setVisible(true);
                
                selectedEquipment = null;
                
                loadEquipments();
                clear();
            }
        }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void addPortsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addPortsActionPerformed
    {//GEN-HEADEREND:event_addPortsActionPerformed
        PortEditorPanel editor = new PortEditorPanel();

        int sel = JOptionPane.showConfirmDialog(this, editor, "Nova porta", JOptionPane.OK_CANCEL_OPTION);

        if (sel == JOptionPane.YES_OPTION)
        {
            ((PortTableModel) tablePorts.getModel()).addPorts(editor.getType(), editor.getSpeed(), editor.getAmount());
        }
    }//GEN-LAST:event_addPortsActionPerformed

    private void deletePortsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deletePortsActionPerformed
    {//GEN-HEADEREND:event_deletePortsActionPerformed
        PortTableModel model = ((PortTableModel) tablePorts.getModel());
        int[] rows = tablePorts.getSelectedRows();
        for (int i = 0; i < rows.length; i++)
        {
            model.delete(rows[i] - i);
        }
    }//GEN-LAST:event_deletePortsActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        try (Database db = Database.getInstance())
        {
            PreparedStatement ps = db.prepareStatement(
                    "   SELECT"
                    + "     case e.type when 0 then 'Servidor' when 1 then 'Roteador' when 2 then 'Switch' else 'Outro' end as type,"
                    + "     e.description AS equipment_description,"
                    + "     e.image,"
                    + "     b.description AS brand_description,"
                    + "     (select count(*) from ports where equipment_id = e.id) as c_ports"
                    + " FROM "
                    + "    equipments e "
                    + " JOIN "
                    + "    brands b ON b.id = e.brand_id"
            );

            ResultSet rs = ps.executeQuery();

            JRResultSetDataSource relatResult = new JRResultSetDataSource(rs);

            JasperPrint jpPrint = JasperFillManager.fillReport("iReport/equipments.jasper", new HashMap(), relatResult);

            JasperViewer jpViewer = new JasperViewer(jpPrint, false);

            jpViewer.setVisible(true);

            jpViewer.toFront();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPorts;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btImage;
    private javax.swing.JButton btSave;
    private javax.swing.JButton btSearch;
    private javax.swing.JComboBox<Brand> cbBrand;
    private javax.swing.JComboBox<EquipmentType> cbType;
    private javax.swing.JButton deletePorts;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbBrand;
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbImageSelected;
    private javax.swing.JLabel lbInfo;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbNoEquipmentSelected;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbType;
    private javax.swing.JList<Equipment> listEquipments;
    private javax.swing.JPanel panelEquipmentData;
    private javax.swing.JPanel panelEquipmentDataTop;
    private javax.swing.JPanel panelEquipmentEdit;
    private javax.swing.JPanel panelFiller;
    private javax.swing.JPanel panelList;
    private javax.swing.JPanel panelNoEquipmentSelected;
    private javax.swing.JPanel panelPorts;
    private javax.swing.JPanel pnButton;
    private javax.swing.JPanel pnData;
    private javax.swing.JPanel pnImage;
    private javax.swing.JScrollPane scrollEdit;
    private javax.swing.JScrollPane scrollInfo;
    private javax.swing.JScrollPane scrollPorts;
    private javax.swing.JScrollPane spBrands;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextArea taInfo;
    private javax.swing.JTable tablePorts;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfSearch;
    // End of variables declaration//GEN-END:variables
}
