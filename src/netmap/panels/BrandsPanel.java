/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.panels;

import netmap.components.BrandsListCellRenderer;
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
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import netmap.database.Database;
import netmap.database.managers.BrandManager;
import netmap.entities.Brand;

/**
 *
 * @author darlan.ullmann
 */
public class BrandsPanel extends javax.swing.JPanel
{

    private boolean changed = false;
    private Brand selectedBrand = null;
    private BufferedImage selImage = null;

    /**
     * Creates new form BrandsPanel
     */
    public BrandsPanel()
    {
        initComponents();

        listBrands.setCellRenderer(new BrandsListCellRenderer());
        listBrands.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                loadSelectedValue();
            }
        });

        panelBrandEdit.setVisible(false);
    }

    public void loadBrands()
    {
        String search = tfSearch.getText().trim();
        List<Brand> brands;

        if (search.isEmpty())
        {
            brands = BrandManager.getInstance().getAll();
        }
        else
        {
            brands = BrandManager.getInstance().getAllByDescription(search);
        }
        listBrands.setListData(brands.toArray(new Brand[brands.size()]));
        
        clear();
    }

    private void loadSelectedValue()
    {
        refresh();

        selectedBrand = listBrands.getSelectedValue();

        if (selectedBrand != null)
        {
            selImage = selectedBrand.getImage();

            if (selectedBrand != null)
            {
                lbTitle.setText("Marca " + selectedBrand.getDescription());
                if (selectedBrand.getImage() != null)
                {
                    lbImageSelected.setIcon(new ImageIcon(Util.resizeImage(selImage, 100)));
                }
                else
                {
                    lbImageSelected.setIcon(new ImageIcon(getClass().getResource("/img/forbidden.png")));
                }
                lbImageSelected.setPreferredSize(new Dimension(100, 100));
                tfName.setText(selectedBrand.getDescription());
                tfCompany.setText(selectedBrand.getCompany());
                tfWebsite.setText(selectedBrand.getWebsite());

                tfName.requestFocus();
            }
        }
    }

    private void newBrand()
    {
        refresh();

        lbTitle.setText("Nova Marca");
        lbImageSelected.setIcon(new ImageIcon(getClass().getResource("/img/forbidden.png")));
        lbImageSelected.setPreferredSize(new Dimension(100, 100));
        tfName.setText("");

        if (panelNoBrandSelected.isVisible())
        {
            panelNoBrandSelected.setVisible(false);
            panelBrandEdit.setVisible(true);
        }

        listBrands.clearSelection();

        tfName.requestFocus();
    }

    private void save()
    {
        String error = "";
        
        if (tfName.getText().trim().isEmpty())
        {
            error = "O nome da Marca é obrigatória.";
        }
        else if (tfName.getText().trim().length() > 200)
        {
            error = "O nome da Marca deve conter no máximo 200 caracteres.";
        }
        else if (tfCompany.getText().trim().length() > 200)
        {
            error = "A empresa da Marca deve conter no máximo 200 caracteres.";
        }
        else if (tfWebsite.getText().trim().length() > 200)
        {
            error = "O website da Marca deve conter no máximo 200 caracteres.";
        }
        
        if (!error.isEmpty())
        {
            JOptionPane.showMessageDialog(
                        Application.getInstance().getMainFrame(),
                        error,
                        "Salvar Marca",
                        JOptionPane.ERROR_MESSAGE
                );
            return;
        }
        
        Brand brand = selectedBrand;

        if (brand == null)
        {
            brand = new Brand();
        }
        brand.setDescription(tfName.getText());
        brand.setCompany(tfCompany.getText());
        brand.setWebsite(tfWebsite.getText());
        brand.setImage(selImage);
        BrandManager.getInstance().save(brand);


        changed = false;
        tfSearch.setText("");

        clear();
        loadBrands();
    }

    private void refresh()
    {
        if (changed)
        {
            int opt = JOptionPane.showConfirmDialog(
                    Application.getInstance().getMainFrame(),
                    "A Marca " + selectedBrand.getDescription() + " foi alterada, deseja salvá-la?",
                    "Salvar Marca",
                    JOptionPane.YES_NO_OPTION
            );

            if (opt == JOptionPane.YES_OPTION)
            {
                save();
                return;
            }
        }

        if (panelNoBrandSelected.isVisible())
        {
            panelNoBrandSelected.setVisible(false);
            panelBrandEdit.setVisible(true);
        }

        changed = false;
        selImage = null;
        selectedBrand = null;

        btDelete.setVisible(listBrands.getSelectedIndex() >= 0);
    }

    private void clear()
    {
        changed = false;
        selImage = null;
        selectedBrand = null;
        listBrands.clearSelection();

        panelNoBrandSelected.setVisible(true);
        panelBrandEdit.setVisible(false);
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
        listBrands = new javax.swing.JList<>();
        btSearch = new javax.swing.JButton();
        tfSearch = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        panelBrandData = new javax.swing.JPanel();
        panelBrandEdit = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        pnData = new javax.swing.JPanel();
        lbName = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        lbImage = new javax.swing.JLabel();
        pnImage = new javax.swing.JPanel();
        lbImageSelected = new javax.swing.JLabel();
        btImage = new javax.swing.JButton();
        pnFiller = new javax.swing.JPanel();
        lbWebsite = new javax.swing.JLabel();
        tfWebsite = new javax.swing.JTextField();
        lbCompany = new javax.swing.JLabel();
        tfCompany = new javax.swing.JTextField();
        pnButton = new javax.swing.JPanel();
        btSave = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        panelNoBrandSelected = new javax.swing.JPanel();
        lbNoBrandSelected = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        splitPane.setBorder(null);
        splitPane.setDividerLocation(250);

        panelList.setLayout(new java.awt.GridBagLayout());

        listBrands.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        spBrands.setViewportView(listBrands);

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
        jButton1.setText("Nova Marca");
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
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 15, 15);
        panelList.add(jButton1, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 15, 0);
        panelList.add(jButton2, gridBagConstraints);

        splitPane.setLeftComponent(panelList);

        panelBrandData.setLayout(new java.awt.GridBagLayout());

        panelBrandEdit.setLayout(new java.awt.GridBagLayout());

        lbTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbTitle.setText("Nova Marca");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        panelBrandEdit.add(lbTitle, gridBagConstraints);

        pnData.setLayout(new java.awt.GridBagLayout());

        lbName.setFont(lbName.getFont().deriveFont(lbName.getFont().getStyle() | java.awt.Font.BOLD));
        lbName.setText("Nome:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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

        lbImage.setText("Imagem:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnImage.add(btImage, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnImage.add(pnFiller, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnData.add(pnImage, gridBagConstraints);

        lbWebsite.setText("Site:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnData.add(lbWebsite, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnData.add(tfWebsite, gridBagConstraints);

        lbCompany.setText("Empresa:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnData.add(lbCompany, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnData.add(tfCompany, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 10, 15);
        panelBrandEdit.add(pnData, gridBagConstraints);

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
        panelBrandEdit.add(pnButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelBrandData.add(panelBrandEdit, gridBagConstraints);

        panelNoBrandSelected.setLayout(new java.awt.GridBagLayout());

        lbNoBrandSelected.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbNoBrandSelected.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbNoBrandSelected.setText("Selecione uma Marca ao lado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 10);
        panelNoBrandSelected.add(lbNoBrandSelected, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelBrandData.add(panelNoBrandSelected, gridBagConstraints);

        splitPane.setRightComponent(panelBrandData);

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
        loadBrands();
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
        newBrand();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btSaveActionPerformed
    {//GEN-HEADEREND:event_btSaveActionPerformed
        save();
    }//GEN-LAST:event_btSaveActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btDeleteActionPerformed
    {//GEN-HEADEREND:event_btDeleteActionPerformed
        if (selectedBrand != null)
        {
            int opt = JOptionPane.showConfirmDialog(
                    Application.getInstance().getMainFrame(),
                    "Tem certeza que deseja excluir a Marca " + selectedBrand.getDescription(),
                    "Excluir Marca",
                    JOptionPane.YES_NO_OPTION
            );

            if (opt == JOptionPane.YES_OPTION)
            {
                BrandManager.getInstance().delete(selectedBrand);

                panelBrandEdit.setVisible(false);
                panelNoBrandSelected.setVisible(true);

                loadBrands();
            }
        }
    }//GEN-LAST:event_btDeleteActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        try (Database db = Database.getInstance())
        {
            PreparedStatement ps = db.prepareStatement(
                    "   SELECT"
                    + "     b.description"
                    + " FROM "
                    + "    brands b"
            );

            ResultSet rs = ps.executeQuery();

            JRResultSetDataSource relatResult = new JRResultSetDataSource(rs);

            JasperPrint jpPrint = JasperFillManager.fillReport("iReport/brands.jasper", new HashMap(), relatResult);

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
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btImage;
    private javax.swing.JButton btSave;
    private javax.swing.JButton btSearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel lbCompany;
    private javax.swing.JLabel lbImage;
    private javax.swing.JLabel lbImageSelected;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbNoBrandSelected;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbWebsite;
    private javax.swing.JList<Brand> listBrands;
    private javax.swing.JPanel panelBrandData;
    private javax.swing.JPanel panelBrandEdit;
    private javax.swing.JPanel panelList;
    private javax.swing.JPanel panelNoBrandSelected;
    private javax.swing.JPanel pnButton;
    private javax.swing.JPanel pnData;
    private javax.swing.JPanel pnFiller;
    private javax.swing.JPanel pnImage;
    private javax.swing.JScrollPane spBrands;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextField tfCompany;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfSearch;
    private javax.swing.JTextField tfWebsite;
    // End of variables declaration//GEN-END:variables
}
