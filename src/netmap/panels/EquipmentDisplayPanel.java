/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.panels;

import netmap.entities.Equipment;

/**
 *
 * @author Julia
 */
public class EquipmentDisplayPanel extends javax.swing.JPanel
{

    /**
     * Creates new form EquipmentDisplayPanel
     */
    public EquipmentDisplayPanel()
    {
        initComponents();
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

        lbEquipment = new javax.swing.JLabel();
        javax.swing.JComboBox<Equipment> cbEquipment = new javax.swing.JComboBox<>();
        pnEquipmentData = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        lbEquipment.setText("Equipamento:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 10, 10);
        add(lbEquipment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 10, 15);
        add(cbEquipment, gridBagConstraints);

        javax.swing.GroupLayout pnEquipmentDataLayout = new javax.swing.GroupLayout(pnEquipmentData);
        pnEquipmentData.setLayout(pnEquipmentDataLayout);
        pnEquipmentDataLayout.setHorizontalGroup(
            pnEquipmentDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 455, Short.MAX_VALUE)
        );
        pnEquipmentDataLayout.setVerticalGroup(
            pnEquipmentDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 15, 15);
        add(pnEquipmentData, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbEquipment;
    private javax.swing.JPanel pnEquipmentData;
    // End of variables declaration//GEN-END:variables
}
