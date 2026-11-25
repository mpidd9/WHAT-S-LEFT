package whats_left.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import whats_left.model.User;
import whats_left.repository.UserRepository;

/**
 * Halaman pendaftaran akun Customer atau Business.
 */
public class RegisterFrame extends JFrame {
    private final UserRepository userRepository = new UserRepository();

    private final JComboBox<String> roleBox
            = new JComboBox<>(new String[]{User.ROLE_CUSTOMER, User.ROLE_BUSINESS});
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmField = new JPasswordField(20);
    private final JTextField nameField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JTextField addressField = new JTextField(20);

    private final JLabel nameLabel = label("Nama Lengkap :");
    private final JLabel addressLabel = label("Alamat :");

    public RegisterFrame() {
        setTitle("WHAT'S LEFT - Daftar Akun");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(24, 44, 24, 44));
        setContentPane(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Daftar Akun", SwingConstants.CENTER);
        title.setFont(new Font("Tw Cen MT", Font.BOLD, 32));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0, 6, 16, 6);
        panel.add(title, c);

        c.insets = new Insets(5, 6, 5, 6);
        c.gridwidth = 1;

        int row = 1;
        row = addRow(panel, c, row, label("Daftar sebagai :"), roleBox);
        row = addRow(panel, c, row, label("Username :"), usernameField);
        row = addRow(panel, c, row, label("Password :"), passwordField);
        row = addRow(panel, c, row, label("Ulangi Password :"), confirmField);
        row = addRow(panel, c, row, nameLabel, nameField);
        row = addRow(panel, c, row, label("Nomor Telepon :"), phoneField);
        row = addRow(panel, c, row, addressLabel, addressField);

        JButton registerButton = new JButton("Daftar");
        registerButton.setBackground(new Color(0, 204, 51));
        registerButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        registerButton.addActionListener(e -> doRegister());

        c.gridx = 0;
        c.gridy = row++;
        c.gridwidth = 2;
        c.insets = new Insets(16, 6, 6, 6);
        panel.add(registerButton, c);

        JButton backButton = new JButton("Sudah punya akun? Login");
        backButton.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        backButton.addActionListener(e -> backToLogin());

        c.gridy = row;
        c.insets = new Insets(6, 6, 6, 6);
        panel.add(backButton, c);

        roleBox.addActionListener(e -> updateRoleFields());
        getRootPane().setDefaultButton(registerButton);

        updateRoleFields();
        pack();
        setLocationRelativeTo(null);
    }

    private int addRow(JPanel panel, GridBagConstraints c, int row,
            JLabel label, java.awt.Component field) {
        c.gridy = row;
        c.gridx = 0;
        c.weightx = 0;
        panel.add(label, c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(field, c);
        return row + 1;
    }

    private static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        return label;
    }

    private boolean isBusiness() {
        return User.ROLE_BUSINESS.equals(roleBox.getSelectedItem());
    }

    private void updateRoleFields() {
        boolean business = isBusiness();

        nameLabel.setText(business ? "Nama Bisnis :" : "Nama Lengkap :");
        addressLabel.setVisible(business);
        addressField.setVisible(business);

        pack();
    }

    private void backToLogin() {
        new LoginFrame().setVisible(true);
        dispose();
    }

    private void doRegister() {
        String username = usernameField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        char[] password = passwordField.getPassword();
        char[] confirm = confirmField.getPassword();

        try {
            String error = validate(username, password, confirm, name, phone, address);

            if (error != null) {
                JOptionPane.showMessageDialog(this, error,
                        "Data belum valid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (userRepository.usernameExists(username)) {
                JOptionPane.showMessageDialog(this,
                        "Username sudah dipakai, silakan pilih yang lain.");
                return;
            }

            if (isBusiness()) {
                userRepository.registerBusiness(username, password, name, address, phone);
            } else {
                if (userRepository.customerPhoneExists(phone)) {
                    JOptionPane.showMessageDialog(this,
                            "Nomor telepon ini sudah terdaftar pada akun Customer lain.");
                    return;
                }

                userRepository.registerCustomer(username, password, name, phone);
            }

            JOptionPane.showMessageDialog(this,
                    "Pendaftaran berhasil! Silakan login.");
            backToLogin();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Pendaftaran gagal: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirm, '\0');
        }
    }

    /** Mengembalikan pesan error, atau null jika semua data valid. */
    private String validate(String username, char[] password, char[] confirm,
            String name, String phone, String address) {
        if (!username.matches("[A-Za-z0-9_]{4,20}")) {
            return "Username 4-20 karakter, hanya huruf, angka, dan underscore (_).";
        }

        if (password.length < 6) {
            return "Password minimal 6 karakter.";
        }

        if (!Arrays.equals(password, confirm)) {
            return "Password dan ulangi password tidak sama.";
        }

        if (name.isEmpty()) {
            return (isBusiness() ? "Nama bisnis" : "Nama lengkap") + " harus diisi.";
        }

        if (!phone.matches("[0-9]{10,15}")) {
            return "Nomor telepon harus terdiri dari 10-15 digit angka.";
        }

        if (isBusiness() && address.isEmpty()) {
            return "Alamat bisnis harus diisi.";
        }

        return null;
    }
}
