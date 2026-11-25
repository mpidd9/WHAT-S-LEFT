/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package whats_left.view;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import whats_left.model.FoodItem;
import whats_left.repository.FoodRepository;
import java.sql.SQLException;
import whats_left.model.ProductionRecord;
import whats_left.model.SurplusListing;
import whats_left.repository.ProductionRepository;
import whats_left.repository.SurplusRepository;
/**
 *
 * @author LOQ
 */
public class FoodFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FoodFrame.class.getName());
    private String mode;
    private FoodRepository foodRepository = new FoodRepository();
    
    private ProductionRepository productionRepository = new ProductionRepository();
    private SurplusRepository surplusRepository = new SurplusRepository();
    
    /**
     * Creates new form FoodFrame
     */
    public FoodFrame(String mode) {
        initComponents();

        this.mode = mode;
        jLabel2.setText(mode);

        if (mode.equals("Customer")) {
            jButton1.setText("Katalog");
            
            jTable2.setModel(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{
                        "ID", "Nama Makanan", "Kategori",
                        "Harga Normal", "Diskon", "Harga Diskon",
                        "Stok", "Pickup Mulai", "Pickup Selesai"
                    }
            ));

            jTable2.getColumnModel().getColumn(0).setMinWidth(0);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable2.getColumnModel().getColumn(0).setWidth(0);
            
            jLabel3.setVisible(false);
            jLabel4.setVisible(false);
            jLabel5.setVisible(false);

            jTextField1.setVisible(false);
            jTextField2.setVisible(false);
            jTextField3.setVisible(false);

            jButton2.setVisible(false);
            jButton3.setVisible(false);
            jButton5.setVisible(true);
            jButton5.setText("Reservasi");

            jButton6.setVisible(false);
            jButton7.setVisible(false);
            jButton8.setVisible(false);

            jLabel1.setText("Daftar Makanan");
        }
        
        loadFoods();
        
        jTable2.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && mode.equals("Business")) {
                int selectedRow = jTable2.getSelectedRow();

                if (selectedRow != -1) {
                    jTextField1.setText(jTable2.getValueAt(selectedRow, 1).toString());
                    jTextField2.setText(jTable2.getValueAt(selectedRow, 2).toString());
                    jTextField3.setText(jTable2.getValueAt(selectedRow, 3).toString());
                }
            }
        });

        jButton2.addActionListener(e -> {
            ProductionFrame productionFrame = new ProductionFrame();
            productionFrame.setVisible(true);
            this.dispose();
        });
        jButton3.addActionListener(e -> {
            SurplusFrame surplusFrame = new SurplusFrame();
            surplusFrame.setVisible(true);
            this.dispose();
        });
        jButton4.addActionListener(e -> {
            whats_left.model.Session.logout();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        });
        
        jButton9.addActionListener(e -> {
            ReservationFrame reservationFrame;

            if (mode.equals("Business")) {
                reservationFrame = new ReservationFrame("Business");
            } else {
                reservationFrame = new ReservationFrame("Customer");
            }

            reservationFrame.setVisible(true);
            this.dispose();
        });
    }
    
    private void loadFoods() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        if (mode.equals("Business")) {
            List<FoodItem> foods = foodRepository.getFoodsByBusiness(
                    whats_left.model.Session.getBusinessId());

            for (FoodItem food : foods) {
                Object[] row = {
                    food.getId(),
                    food.getFoodName(),
                    food.getCategory(),
                    food.getRegularPrice()
                };

                model.addRow(row);
            }

        } else {
            List<SurplusListing> surpluses = surplusRepository.getAvailableSurpluses();

            try {
                for (SurplusListing surplus : surpluses) {
                    ProductionRecord production = productionRepository.getProductionById(
                            surplus.getProductionId()
                    );

                    if (production != null) {
                        FoodItem food = foodRepository.getFoodById(
                                production.getFoodId()
                        );

                        if (food != null) {
                            Object[] row = {
                                surplus.getId(),
                                food.getFoodName(),
                                food.getCategory(),
                                food.getRegularPrice(),
                                surplus.getDiscountPercentage() + "%",
                                surplus.getDiscountedPrice(),
                                surplus.getAvailableQuantity(),
                                surplus.getPickupStart(),
                                surplus.getPickupEnd()
                            };

                            model.addRow(row);
                        }
                    }
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gagal memuat katalog: " + e.getMessage()
                );
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel1.setText("Kelola Data Makanan");

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setText("Business");

        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton1.setText("Makanan");

        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton2.setText("Produksi");

        jButton3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton3.setText("Surplus");

        jButton4.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton4.setText("Logout");

        jButton9.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jButton9.setText("Reservation");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(26, 26, 26))
        );

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel3.setText("Nama Makanan :");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel4.setText("Kategori             :");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel5.setText("Harga                 :");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nama Makanan", "Kategori", "Harga Satuan"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel6.setText("Daftar Makanan");

        jButton5.setText("+ Tambah");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setText("Edit");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton7.setText("Hapus");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton8.setText("Reset");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jButton5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if (mode.equals("Customer")) {
            int selectedRow = jTable2.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Pilih makanan yang ingin dipesan!");
                return;
            }

            int surplusId = Integer.parseInt(
                    jTable2.getValueAt(selectedRow, 0).toString()
            );

            ReservationFrame reservationFrame
                    = new ReservationFrame("Customer", surplusId);

            reservationFrame.setVisible(true);
            this.dispose();

            return;
        }
        
        String foodName = jTextField1.getText();
        String category = jTextField2.getText();
        String priceText = jTextField3.getText();

        if (foodName.trim().isEmpty() || category.trim().isEmpty() || priceText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data harus diisi!");
        } else {
            try {
                double price = Double.parseDouble(priceText);

                FoodItem food = new FoodItem();
                food.setBusinessId(whats_left.model.Session.getBusinessId());
                food.setFoodName(foodName);
                food.setCategory(category);
                food.setRegularPrice(price);

                boolean result = foodRepository.addFood(food);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Data makanan berhasil ditambahkan!");
                    loadFoods();

                    jTextField1.setText("");
                    jTextField2.setText("");
                    jTextField3.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan data makanan!");
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka!");
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");

        jTable2.clearSelection();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
        } else {
            String foodName = jTextField1.getText();
            String category = jTextField2.getText();
            String priceText = jTextField3.getText();

            if (foodName.trim().isEmpty() || category.trim().isEmpty() || priceText.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua data harus diisi!");
            } else {
                try {
                    int id = Integer.parseInt(jTable2.getValueAt(selectedRow, 0).toString());
                    double price = Double.parseDouble(priceText);

                    FoodItem food = new FoodItem();
                    food.setId(id);
                    food.setFoodName(foodName);
                    food.setCategory(category);
                    food.setRegularPrice(price);

                    boolean result = foodRepository.updateFood(food);

                    if (result) {
                        JOptionPane.showMessageDialog(this, "Data makanan berhasil diubah!");
                        loadFoods();

                        jTextField1.setText("");
                        jTextField2.setText("");
                        jTextField3.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Gagal mengubah data makanan!");
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Harga harus berupa angka!");
                }
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
        } else {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah kamu yakin ingin menghapus data ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(jTable2.getValueAt(selectedRow, 0).toString());

                boolean result = foodRepository.deleteFood(id);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Data makanan berhasil dihapus!");
                    loadFoods();

                    jTextField1.setText("");
                    jTextField2.setText("");
                    jTextField3.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data makanan!");
                }
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FoodFrame("Business").setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
