/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package whats_left.view;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import whats_left.model.FoodItem;
import whats_left.model.ProductionRecord;
import whats_left.repository.FoodRepository;
import whats_left.repository.ProductionRepository;
import java.sql.SQLException;

/**
 *
 * @author LOQ
 */
public class ProductionFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ProductionFrame.class.getName());
    private FoodRepository foodRepository = new FoodRepository();
    private ProductionRepository productionRepository = new ProductionRepository();
    private List<FoodItem> foodList;

    /**
     * Creates new form ProductionFrame
     */
    public ProductionFrame() {
        initComponents();
        
        setLocationRelativeTo(null);
        setupTable();
        loadFoodList();
        loadProductionTable();
        setupSurplusCalculation();
        
        jButton1.addActionListener(e -> {
            FoodFrame foodFrame = new FoodFrame("Business");
            foodFrame.setVisible(true);
            this.dispose();
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
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            this.dispose();
        });
        
        jButton9.addActionListener(e -> {
            ReservationFrame reservationFrame = new ReservationFrame("Business");
            reservationFrame.setVisible(true);
            this.dispose();
        });

        jTextField5.setEditable(false);

        jTable2.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTable2.getSelectedRow();

                if (selectedRow != -1) {
                    try {
                        int id = Integer.parseInt(
                                jTable2.getValueAt(selectedRow, 0).toString()
                        );

                        ProductionRecord production
                                = productionRepository.getProductionById(id);

                        if (production != null) {
                            for (int i = 0; i < foodList.size(); i++) {
                                if (foodList.get(i).getId() == production.getFoodId()) {
                                    jComboBox1.setSelectedIndex(i + 1);
                                    break;
                                }
                            }

                            jTextField2.setText(
                                    production.getProductionDate().toLocalDateTime()
                                            .toLocalDate().toString()
                            );
                            jTextField1.setText(String.valueOf(production.getTotalProduced()));
                            jTextField3.setText(String.valueOf(production.getRegularSold()));
                            jTextField4.setText(String.valueOf(production.getWasted()));
                            jTextField5.setText(String.valueOf(production.calculateSurplus()));
                        }

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Gagal mengambil data produksi: " + ex.getMessage());
                    }
                }
            }
        });
    }
    
    private void setupTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Tanggal", "Nama Makanan",
                    "Produksi", "Terjual", "Terbuang", "Surplus"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTable2.setModel(model);
    }
    
    private void loadFoodList() {
        foodList = foodRepository.getAllFoods();

        jComboBox1.removeAllItems();
        jComboBox1.addItem("Pilih Makanan");

        for (FoodItem food : foodList) {
            jComboBox1.addItem(food.getFoodName());
        }
    }
    
    
    private void loadProductionTable() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        try {
            List<ProductionRecord> productions = productionRepository.getAllProductions();

            for (ProductionRecord production : productions) {
                FoodItem food = foodRepository.getFoodById(production.getFoodId());

                String foodName = "-";

                if (food != null) {
                    foodName = food.getFoodName();
                }

                model.addRow(new Object[]{
                    production.getId(),
                    production.getProductionDate().toLocalDateTime().toLocalDate().toString(),
                    foodName,
                    production.getTotalProduced(),
                    production.getRegularSold(),
                    production.getWasted(),
                    production.calculateSurplus()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat data produksi: " + e.getMessage());
        }
    }


    private void setupSurplusCalculation() {
        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateSurplus();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateSurplus();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateSurplus();
            }
        };

        jTextField1.getDocument().addDocumentListener(listener);
        jTextField3.getDocument().addDocumentListener(listener);
        jTextField4.getDocument().addDocumentListener(listener);
    }
    
    private void calculateSurplus() {
        try {
            String totalText = jTextField1.getText().trim();
            String soldText = jTextField3.getText().trim();
            String wastedText = jTextField4.getText().trim();

            if (totalText.isEmpty() || soldText.isEmpty() || wastedText.isEmpty()) {
                jTextField5.setText("");
                return;
            }

            int total = Integer.parseInt(totalText);
            int sold = Integer.parseInt(soldText);
            int wasted = Integer.parseInt(wastedText);

            int surplus = total - sold - wasted;

            if (surplus >= 0) {
                jTextField5.setText(String.valueOf(surplus));
            } else {
                jTextField5.setText("Tidak valid");
            }

        } catch (NumberFormatException e) {
            jTextField5.setText("");
        }
    }
    
    private boolean validateInput() {

        if (jComboBox1.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih makanan terlebih dahulu.");
            return false;
        }

        if (jTextField2.getText().trim().isEmpty()
                || jTextField1.getText().trim().isEmpty()
                || jTextField3.getText().trim().isEmpty()
                || jTextField4.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Semua field harus diisi.");
            return false;
        }

        try {
            LocalDate.parse(jTextField2.getText().trim());

            int total = Integer.parseInt(jTextField1.getText().trim());
            int sold = Integer.parseInt(jTextField3.getText().trim());
            int wasted = Integer.parseInt(jTextField4.getText().trim());

            if (total < 0 || sold < 0 || wasted < 0) {
                JOptionPane.showMessageDialog(this,
                        "Jumlah tidak boleh negatif.");
                return false;
            }

            if (sold + wasted > total) {
                JOptionPane.showMessageDialog(this,
                        "Jumlah terjual dan terbuang tidak boleh melebihi total produksi.");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Jumlah produksi harus berupa angka bulat.");
            return false;

        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Format tanggal harus yyyy-MM-dd.");
            return false;
        }

        return true;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton5.setText("Hapus");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel1.setText("Catatan Produksi");

        jButton8.setText("Reset");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel3.setText("Nama Makanan :");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel4.setText("Tanggal              :");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel5.setText("Total Produksi   :");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Tanggal", "Nama Makanan", "Produksi", "Terjual", "Terbuang", "Surplus"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

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

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel6.setText("Riwayat Produksi");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Makanan", " " }));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel7.setText("Terbuang               :");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel8.setText("Terjual                  :");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel9.setText("Surplus                 :");

        jTextField5.setEnabled(false);
        jTextField5.addActionListener(this::jTextField5ActionPerformed);

        jButton6.setText("Simpan");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton7.setText("Edit");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel6))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1)
                            .addComponent(jTextField2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                    .addComponent(jTextField5))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51))))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(201, 201, 201)
                    .addComponent(jButton6)
                    .addContainerGap(551, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton8)
                    .addComponent(jButton7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(435, Short.MAX_VALUE)
                    .addComponent(jButton6)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data produksi yang ingin dihapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah kamu yakin ingin menghapus data produksi ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(
                    jTable2.getValueAt(selectedRow, 0).toString()
            );

            try {
                boolean result = productionRepository.deleteProduction(id);

                if (result) {
                    JOptionPane.showMessageDialog(this,
                            "Data produksi berhasil dihapus!");

                    loadProductionTable();

                    jComboBox1.setSelectedIndex(0);
                    jTextField1.setText("");
                    jTextField2.setText("");
                    jTextField3.setText("");
                    jTextField4.setText("");
                    jTextField5.setText("");

                    jTable2.clearSelection();

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal menghapus data produksi!");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Terjadi kesalahan database: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        jComboBox1.setSelectedIndex(0);
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");

        jTable2.clearSelection();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if (validateInput()) {
            int selectedIndex = jComboBox1.getSelectedIndex();
            FoodItem selectedFood = foodList.get(selectedIndex - 1);

            ProductionRecord production = new ProductionRecord();

            production.setFoodId(selectedFood.getId());
            production.setProductionDate(
                    Timestamp.valueOf(
                            LocalDate.parse(jTextField2.getText().trim()).atStartOfDay()
                    )
            );
            production.setTotalProduced(Integer.parseInt(jTextField1.getText().trim()));
            production.setRegularSold(Integer.parseInt(jTextField3.getText().trim()));
            production.setWasted(Integer.parseInt(jTextField4.getText().trim()));

            try {
                boolean result = productionRepository.addProduction(production);

                if (result) {
                    JOptionPane.showMessageDialog(this,
                            "Data produksi berhasil disimpan!");

                    loadProductionTable();

                    jComboBox1.setSelectedIndex(0);
                    jTextField1.setText("");
                    jTextField2.setText("");
                    jTextField3.setText("");
                    jTextField4.setText("");
                    jTextField5.setText("");

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal menyimpan data produksi!");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Terjadi kesalahan database: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data produksi yang ingin diedit!");
            return;
        }

        if (!validateInput()) {
            return;
        }

        int id = Integer.parseInt(
                jTable2.getValueAt(selectedRow, 0).toString()
        );

        int selectedIndex = jComboBox1.getSelectedIndex();
        FoodItem selectedFood = foodList.get(selectedIndex - 1);

        ProductionRecord production = new ProductionRecord();

        production.setId(id);
        production.setFoodId(selectedFood.getId());
        production.setProductionDate(
                Timestamp.valueOf(
                        LocalDate.parse(jTextField2.getText().trim()).atStartOfDay()
                )
        );
        production.setTotalProduced(Integer.parseInt(jTextField1.getText().trim()));
        production.setRegularSold(Integer.parseInt(jTextField3.getText().trim()));
        production.setWasted(Integer.parseInt(jTextField4.getText().trim()));

        try {
            boolean result = productionRepository.updateProduction(production);

            if (result) {
                JOptionPane.showMessageDialog(this,
                        "Data produksi berhasil diperbarui!");

                loadProductionTable();
                jTable2.clearSelection();

                jComboBox1.setSelectedIndex(0);
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField5.setText("");

            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal memperbarui data produksi!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan database: " + e.getMessage());
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
        java.awt.EventQueue.invokeLater(() -> new ProductionFrame().setVisible(true));
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
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
