/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package whats_left.view;

import java.util.ArrayList;
import java.util.List;
import whats_left.model.FoodItem;
import whats_left.model.ProductionRecord;
import whats_left.model.SurplusListing;
import whats_left.repository.FoodRepository;
import whats_left.repository.ProductionRepository;
import whats_left.repository.SurplusRepository;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Timestamp;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author LOQ
 */
public class SurplusFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SurplusFrame.class.getName());
    private ProductionRepository productionRepository = new ProductionRepository();
    private FoodRepository foodRepository = new FoodRepository();
    private SurplusRepository surplusRepository = new SurplusRepository();

    private List<ProductionRecord> productionList = new ArrayList<>();
    private int selectedSurplusId = -1;

    /**
     * Creates new form SurplusFlame
     */
    public SurplusFrame() {
        initComponents();
        
        setLocationRelativeTo(null);
        selectedSurplusId = -1;
        jTable2.clearSelection();
        loadProductionComboBox();
        loadSurplusTable();
        
        jTable2.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTable2.getSelectedRow();

                if (selectedRow != -1) {
                    int id = Integer.parseInt(
                            jTable2.getValueAt(selectedRow, 0).toString()
                    );

                    SurplusListing surplus = surplusRepository.getSurplusById(id);

                    if (surplus != null) {
                        selectedSurplusId = surplus.getId();

                        jTextField1.setText(
                                String.valueOf(surplus.getDiscountPercentage())
                        );

                        jTextField2.setText(
                                String.valueOf(surplus.getInitialQuantity())
                        );

                        jTextField4.setText(
                                String.valueOf(surplus.getDiscountedPrice())
                        );

                        jTextField5.setText(
                                surplus.getPickupEnd().toLocalDateTime()
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        );

                        try {
                            ProductionRecord production
                                    = productionRepository.getProductionById(
                                            surplus.getProductionId()
                                    );

                            if (production != null) {
                                FoodItem food
                                        = foodRepository.getFoodById(production.getFoodId());

                                if (food != null) {
                                    jTextField3.setText(
                                            String.valueOf(food.getRegularPrice())
                                    );
                                }
                            }

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this,
                                    "Gagal mengambil data makanan: " + ex.getMessage());
                        }
                    }
                }
            }
        });
        
        jTextField2.setEditable(false);
        jTextField3.setEditable(false);
        
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateDiscountedPrice();
            }
        });

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

        jButton4.addActionListener(e -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            this.dispose();
        });
        
    }

    private void loadProductionComboBox() {
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Pilih Makanan");

        productionList.clear();

        try {
            List<ProductionRecord> productions = productionRepository.getAllProductions();
            List<SurplusListing> surpluses = surplusRepository.getAllSurpluses();

            for (ProductionRecord production : productions) {
                int surplus = production.calculateSurplus();
                boolean alreadyListed = false;

                for (SurplusListing listing : surpluses) {
                    if (listing.getProductionId() == production.getId()) {
                        alreadyListed = true;
                        break;
                    }
                }

                if (surplus > 0 && !alreadyListed) {
                    FoodItem food = foodRepository.getFoodById(production.getFoodId());

                    if (food != null) {
                        productionList.add(production);

                        jComboBox1.addItem(
                                food.getFoodName() + " - Surplus: " + surplus
                        );
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mengambil data produksi: " + e.getMessage());
        }
    }
    private void loadSurplusTable() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Nama Makanan");
        model.addColumn("Jumlah");
        model.addColumn("Diskon");
        model.addColumn("Harga Diskon");
        model.addColumn("Batas Ambil");
        model.addColumn("Status");

        List<SurplusListing> surpluses = surplusRepository.getAllSurpluses();

        try {
            for (SurplusListing surplus : surpluses) {
                ProductionRecord production
                        = productionRepository.getProductionById(surplus.getProductionId());

                if (production != null) {
                    FoodItem food = foodRepository.getFoodById(production.getFoodId());

                    if (food != null) {
                        String pickupEnd = surplus.getPickupEnd()
                                .toLocalDateTime()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                        model.addRow(new Object[]{
                            surplus.getId(),
                            food.getFoodName(),
                            surplus.getInitialQuantity(),
                            surplus.getDiscountPercentage() + "%",
                            surplus.getDiscountedPrice(),
                            pickupEnd,
                            surplus.getStatus()
                        });
                    }
                }
            }

            jTable2.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menampilkan data surplus: " + e.getMessage());
        }
    }
    
    private void calculateDiscountedPrice() {
        try {
            double regularPrice = Double.parseDouble(jTextField3.getText());
            double discount = Double.parseDouble(jTextField1.getText());

            if (discount >= 0 && discount <= 100) {
                double discountedPrice = regularPrice - (regularPrice * discount / 100);

                jTextField4.setText(String.valueOf(discountedPrice));
            } else {
                jTextField4.setText("");
            }

        } catch (NumberFormatException e) {
            jTextField4.setText("");
        }
    }
    
    private Timestamp getPickupEnd() {
        try {
            DateTimeFormatter formatter
                    = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDateTime pickupEnd = LocalDateTime.parse(
                    jTextField5.getText().trim(), formatter
            );

            LocalDateTime now = LocalDateTime.now();

            if (!pickupEnd.isAfter(now)) {
                JOptionPane.showMessageDialog(this,
                        "Batas pengambilan harus lebih dari waktu sekarang.");
                return null;
            }

            return Timestamp.valueOf(pickupEnd);

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Format waktu salah!\nGunakan format: yyyy-MM-dd HH:mm");
            return null;
        }
    }
    
    private boolean validateInput(boolean editMode) {
        if (!editMode && jComboBox1.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih makanan terlebih dahulu.");
            return false;
        }

        if (editMode && selectedSurplusId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih penawaran yang ingin diedit.");
            return false;
        }

        if (jTextField1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Diskon tidak boleh kosong.");
            return false;
        }

        try {
            double discount = Double.parseDouble(jTextField1.getText());

            if (discount <= 0 || discount > 100) {
                JOptionPane.showMessageDialog(this,
                        "Diskon harus lebih dari 0 dan maksimal 100%.");
                return false;
            }

            if (jTextField4.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Harga diskon belum dihitung.");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Diskon harus berupa angka.");
            return false;
        }

        if (getPickupEnd() == null) {
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

        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel5.setText("Diskon               :");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nama Makanan", "Jumlah", "Diskon", "Harga Diskon", "Batas Ambil", "Status"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Makanan" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

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
        jButton9.addActionListener(this::jButton9ActionPerformed);

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

        jButton5.setText("Simpan");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel1.setText("Penawaran Surplus");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel8.setText("Harga Normal         :");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel9.setText("Batas Pengambilan :");

        jButton8.setText("Reset");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel3.setText("Nama Makanan :");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel6.setText("Daftar Penawaran");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel4.setText("Jumlah Surplus  :");

        jTextField4.setEnabled(false);

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel10.setText("Harga Diskon          :");

        jButton6.setText("Hapus");
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField5))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(54, 54, 54))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(279, 279, 279)
                    .addComponent(jButton7)
                    .addContainerGap(405, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton8)
                        .addComponent(jButton6))
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(436, Short.MAX_VALUE)
                    .addComponent(jButton7)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if (!validateInput(false)) {
            return;
        }

        try {
            int selectedIndex = jComboBox1.getSelectedIndex();

            ProductionRecord production = productionList.get(selectedIndex - 1);

            int productionId = production.getId();
            int surplus = production.calculateSurplus();

            double discount = Double.parseDouble(jTextField1.getText());
            double discountedPrice = Double.parseDouble(jTextField4.getText());

            Timestamp pickupStart = Timestamp.valueOf(LocalDateTime.now());
            Timestamp pickupEnd = getPickupEnd();

            if (pickupEnd == null) {
                return;
            }

            SurplusListing surplusListing = new SurplusListing(
                    0,
                    productionId,
                    discount,
                    discountedPrice,
                    surplus,
                    surplus,
                    pickupStart,
                    pickupEnd,
                    "Available",
                    null
            );

            boolean result = surplusRepository.addSurplus(surplusListing);

            if (result) {
                JOptionPane.showMessageDialog(this,
                        "Penawaran surplus berhasil disimpan!");

                jComboBox1.setSelectedIndex(0);
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField5.setText("");

                selectedSurplusId = -1;
                jTable2.clearSelection();
                
                loadProductionComboBox();
                loadSurplusTable();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan penawaran surplus.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Pastikan input angka sudah benar.");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        selectedSurplusId = -1;

        jTable2.clearSelection();
        jComboBox1.setSelectedIndex(0);

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        int selectedIndex = jComboBox1.getSelectedIndex();

        if (selectedIndex > 0) {
            ProductionRecord production = productionList.get(selectedIndex - 1);

            FoodItem food = foodRepository.getFoodById(production.getFoodId());

            if (food != null) {
                jTextField2.setText(String.valueOf(production.calculateSurplus()));
                jTextField3.setText(String.valueOf(food.getRegularPrice()));
            }

        } else {
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih penawaran yang ingin dihapus.");
            return;
        }

        int id = Integer.parseInt(
                jTable2.getValueAt(selectedRow, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah kamu yakin ingin menghapus penawaran ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean result = surplusRepository.deleteSurplus(id);

            if (result) {
                JOptionPane.showMessageDialog(this,
                        "Penawaran surplus berhasil dihapus!");

                selectedSurplusId = -1;

                jTable2.clearSelection();
                jComboBox1.setSelectedIndex(0);

                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField5.setText("");

                loadSurplusTable();
                loadProductionComboBox();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus penawaran surplus. "
                        + "Pastikan tidak ada reservasi yang terkait.");
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        if (!validateInput(true)) {
            return;
        }

        try {
            SurplusListing oldSurplus
                    = surplusRepository.getSurplusById(selectedSurplusId);

            if (oldSurplus == null) {
                JOptionPane.showMessageDialog(this,
                        "Data surplus tidak ditemukan.");
                return;
            }

            double discount = Double.parseDouble(jTextField1.getText());
            double discountedPrice = Double.parseDouble(jTextField4.getText());
            Timestamp pickupEnd = getPickupEnd();

            if (pickupEnd == null) {
                return;
            }

            SurplusListing updatedSurplus = new SurplusListing(
                    oldSurplus.getId(),
                    oldSurplus.getProductionId(),
                    discount,
                    discountedPrice,
                    oldSurplus.getInitialQuantity(),
                    oldSurplus.getAvailableQuantity(),
                    oldSurplus.getPickupStart(),
                    pickupEnd,
                    oldSurplus.getStatus(),
                    oldSurplus.getCreatedAt()
            );

            boolean result = surplusRepository.updateSurplus(updatedSurplus);

            if (result) {
                JOptionPane.showMessageDialog(this,
                        "Penawaran surplus berhasil diperbarui!");

                selectedSurplusId = -1;

                jTable2.clearSelection();
                jComboBox1.setSelectedIndex(0);

                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField5.setText("");

                loadSurplusTable();
                loadProductionComboBox();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal memperbarui penawaran surplus.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Pastikan input angka sudah benar.");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        ReservationFrame reservationFrame = new ReservationFrame("Business");
        reservationFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new SurplusFrame().setVisible(true));
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
