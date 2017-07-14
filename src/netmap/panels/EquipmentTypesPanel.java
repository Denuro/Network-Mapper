/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.panels;

import netmap.entities.EquipmentType;
import netmap.util.Application;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import netmap.components.EquipmentTypesListCellRenderer;
import netmap.database.managers.EquipmentTypeManager;

/**
 *
 * @author darlan.ullmann
 */
public class EquipmentTypesPanel extends javax.swing.JPanel
{
    private boolean changed = false;
    private EquipmentType selectedEquipmentType = null;

    /**
     * Creates new form EquipmentTypesPanel
     */
    public EquipmentTypesPanel()
    {
        initComponents();

        listEquipmentTypes.setCellRenderer(new EquipmentTypesListCellRenderer());
        listEquipmentTypes.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                loadSelectedValue();
            }
        });

        panelEquipmentTypeEdit.setVisible(false);
    }

    public void loadEquipmentTypes()
    {
        String search = tfSearch.getText().trim();
        List<EquipmentType> equipmentTypes;

        if (search.isEmpty())
        {
            equipmentTypes = EquipmentTypeManager.getInstance().getAll();
        }
        else
        {
            equipmentTypes = EquipmentTypeManager.getInstance().getAllByDescription(search);
        }
        listEquipmentTypes.setListData(equipmentTypes.toArray(new EquipmentType[equipmentTypes.size()]));
        
        clear();
    }

    private void loadSelectedValue()
    {
        refresh();

        selectedEquipmentType = listEquipmentTypes.getSelectedValue();

        if (selectedEquipmentType != null)
        {
            lbTitle.setText("Tipo de Equipamento " + selectedEquipmentType.getDescription());
            tfDescription.setText(selectedEquipmentType.getDescription());

            tfDescription.requestFocus();
        }
    }

    private void newEquipmentType()
    {
        refresh();

        lbTitle.setText("Novo Tipo de Equipamento");
        tfDescription.setText("");

        if (panelNoEquipmentTypeSelected.isVisible())
        {
            panelNoEquipmentTypeSelected.setVisible(false);
            panelEquipmentTypeEdit.setVisible(true);
        }

        listEquipmentTypes.clearSelection();

        tfDescription.requestFocus();
    }

    private void save()
    {
        String error = "";
        
        if (tfDescription.getText().trim().isEmpty())
        {
            error = "A descrição do Tipo de Equipamento é obrigatória.";
        }
        else if (tfDescription.getText().trim().length() > 200)
        {
            error = "A descrição do Tipo de Equipamento deve conter no máximo 200 caracteres.";
        }
        
        if (!error.isEmpty())
        {
            JOptionPane.showMessageDialog(
                        Application.getInstance().getMainFrame(),
                        error,
                        "Salvar Tipo de equipamento",
                        JOptionPane.ERROR_MESSAGE
                );
            return;
        }
        
        EquipmentType equipmentType = selectedEquipmentType;

        if (equipmentType == null)
        {
            equipmentType = new EquipmentType();
            equipmentType.setDescription(tfDescription.getText().trim());

            EquipmentTypeManager.getInstance().save(equipmentType);
        }
        else
        {
            equipmentType.setDescription(tfDescription.getText());

            EquipmentTypeManager.getInstance().save(equipmentType);
        }

        changed = false;
        tfSearch.setText("");

        clear();
        loadEquipmentTypes();
    }

    private void refresh()
    {
        if (changed)
        {
            int opt = JOptionPane.showConfirmDialog(
                    Application.getInstance().getMainFrame(),
                    "O Tipo de Equipamento " + selectedEquipmentType.getDescription() + " foi alterada, deseja salvá-la?",
                    "Salvar Tipo de Equipamento",
                    JOptionPane.YES_NO_OPTION
            );

            if (opt == JOptionPane.YES_OPTION)
            {
                save();
                return;
            }
        }

        if (panelNoEquipmentTypeSelected.isVisible())
        {
            panelNoEquipmentTypeSelected.setVisible(false);
            panelEquipmentTypeEdit.setVisible(true);
        }

        changed = false;
        selectedEquipmentType = null;

        btDelete.setVisible(listEquipmentTypes.getSelectedIndex() >= 0);
    }

    private void clear()
    {
        changed = false;
        selectedEquipmentType = null;
        listEquipmentTypes.clearSelection();

        panelNoEquipmentTypeSelected.setVisible(true);
        panelEquipmentTypeEdit.setVisible(false);
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
        spEquipmentTypes = new javax.swing.JScrollPane();
        listEquipmentTypes = new javax.swing.JList<>();
        btSearch = new javax.swing.JButton();
        tfSearch = new javax.swing.JTextField();
        btAdd = new javax.swing.JButton();
        panelEquipmentTypeData = new javax.swing.JPanel();
        panelEquipmentTypeEdit = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        pnData = new javax.swing.JPanel();
        lbDescription = new javax.swing.JLabel();
        tfDescription = new javax.swing.JTextField();
        pnButton = new javax.swing.JPanel();
        btSave = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        panelNoEquipmentTypeSelected = new javax.swing.JPanel();
        lbNoEquipmentTypeSelected = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        splitPane.setBorder(null);
        splitPane.setDividerLocation(250);

        panelList.setLayout(new java.awt.GridBagLayout());

        listEquipmentTypes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        spEquipmentTypes.setViewportView(listEquipmentTypes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 10, 10);
        panelList.add(spEquipmentTypes, gridBagConstraints);

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

        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add-1.png"))); // NOI18N
        btAdd.setText("Novo Tipo de Equipamento");
        btAdd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 15, 15);
        panelList.add(btAdd, gridBagConstraints);

        splitPane.setLeftComponent(panelList);

        panelEquipmentTypeData.setLayout(new java.awt.GridBagLayout());

        panelEquipmentTypeEdit.setLayout(new java.awt.GridBagLayout());

        lbTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTitle.setText("Novo Tipo de Equipamento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        panelEquipmentTypeEdit.add(lbTitle, gridBagConstraints);

        pnData.setLayout(new java.awt.GridBagLayout());

        lbDescription.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbDescription.setText("Descrição:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnData.add(lbDescription, gridBagConstraints);

        tfDescription.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                tfDescriptionKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnData.add(tfDescription, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 10, 15);
        panelEquipmentTypeEdit.add(pnData, gridBagConstraints);

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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 15, 15);
        panelEquipmentTypeEdit.add(pnButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelEquipmentTypeData.add(panelEquipmentTypeEdit, gridBagConstraints);

        panelNoEquipmentTypeSelected.setLayout(new java.awt.GridBagLayout());

        lbNoEquipmentTypeSelected.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbNoEquipmentTypeSelected.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbNoEquipmentTypeSelected.setText("Selecione um Tipo de Equipamento ao lado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 10);
        panelNoEquipmentTypeSelected.add(lbNoEquipmentTypeSelected, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelEquipmentTypeData.add(panelNoEquipmentTypeSelected, gridBagConstraints);

        splitPane.setRightComponent(panelEquipmentTypeData);

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
        loadEquipmentTypes();
    }//GEN-LAST:event_tfSearchActionPerformed

    private void tfDescriptionKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tfDescriptionKeyTyped
    {//GEN-HEADEREND:event_tfDescriptionKeyTyped
        changed = true;
    }//GEN-LAST:event_tfDescriptionKeyTyped

    private void btAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btAddActionPerformed
    {//GEN-HEADEREND:event_btAddActionPerformed
        newEquipmentType();
    }//GEN-LAST:event_btAddActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btSaveActionPerformed
    {//GEN-HEADEREND:event_btSaveActionPerformed
        save();
    }//GEN-LAST:event_btSaveActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btDeleteActionPerformed
    {//GEN-HEADEREND:event_btDeleteActionPerformed
        if (selectedEquipmentType != null)
        {
            int opt = JOptionPane.showConfirmDialog(
                    Application.getInstance().getMainFrame(),
                    "Tem certeza que deseja excluir o Tipo de Equipamento " + selectedEquipmentType.getDescription(),
                    "Excluir Tipo de Equipamento",
                    JOptionPane.YES_NO_OPTION
            );

            if (opt == JOptionPane.YES_OPTION)
            {
                EquipmentTypeManager.getInstance().delete(selectedEquipmentType);

                panelEquipmentTypeEdit.setVisible(false);
                panelNoEquipmentTypeSelected.setVisible(true);

                loadEquipmentTypes();
            }
        }
    }//GEN-LAST:event_btDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btSave;
    private javax.swing.JButton btSearch;
    private javax.swing.JLabel lbDescription;
    private javax.swing.JLabel lbNoEquipmentTypeSelected;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JList<EquipmentType> listEquipmentTypes;
    private javax.swing.JPanel panelEquipmentTypeData;
    private javax.swing.JPanel panelEquipmentTypeEdit;
    private javax.swing.JPanel panelList;
    private javax.swing.JPanel panelNoEquipmentTypeSelected;
    private javax.swing.JPanel pnButton;
    private javax.swing.JPanel pnData;
    private javax.swing.JScrollPane spEquipmentTypes;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextField tfDescription;
    private javax.swing.JTextField tfSearch;
    // End of variables declaration//GEN-END:variables
}
