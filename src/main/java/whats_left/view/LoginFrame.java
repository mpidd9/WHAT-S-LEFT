package whats_left.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import whats_left.model.Session;
import whats_left.model.User;
import whats_left.repository.UserRepository;

/**
 * Halaman login. Menggantikan MainFrame sebagai halaman awal aplikasi.
 */
public class LoginFrame extends JFrame {
    private final UserRepository userRepository = new UserRepository();

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public LoginFrame() {
        setTitle("WHAT'S LEFT - Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));
        setContentPane(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridwidth = 2;

        JLabel title = new JLabel("WHAT'S LEFT", SwingConstants.CENTER);
        title.setFont(new Font("Tw Cen MT", Font.BOLD, 40));
        c.gridy = 0;
        panel.add(title, c);

        JLabel subtitle = new JLabel("Silakan login untuk melanjutkan",
                SwingConstants.CENTER);
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        c.gridy = 1;
        c.insets = new Insets(0, 6, 20, 6);
        panel.add(subtitle, c);

        c.insets = new Insets(6, 6, 6, 6);
        c.gridwidth = 1;

        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0;
        panel.add(label("Username :"), c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(usernameField, c);

        c.gridy = 3;
        c.gridx = 0;
        c.weightx = 0;
        panel.add(label("Password :"), c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(passwordField, c);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 204, 51));
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        loginButton.addActionListener(e -> doLogin());

        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = 2;
        c.insets = new Insets(18, 6, 6, 6);
        panel.add(loginButton, c);

        JButton registerButton = new JButton("Belum punya akun? Daftar");
        registerButton.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });

        c.gridy = 5;
        c.insets = new Insets(6, 6, 6, 6);
        panel.add(registerButton, c);

        // Enter di kolom mana pun = login
        usernameField.addActionListener(e -> doLogin());
        passwordField.addActionListener(e -> doLogin());
        getRootPane().setDefaultButton(loginButton);

        pack();
        setLocationRelativeTo(null);
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        return label;
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        try {
            if (username.isEmpty() || password.length == 0) {
                JOptionPane.showMessageDialog(this,
                        "Username dan password harus diisi!");
                return;
            }

            User user = userRepository.authenticate(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Username atau password salah!",
                        "Login gagal", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                return;
            }

            Session.login(user);

            FoodFrame foodFrame = new FoodFrame(user.getRole());
            foodFrame.setVisible(true);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat terhubung ke database.\n"
                    + "Pastikan MySQL (Laragon) sudah menyala dan tabel sudah dibuat "
                    + "(jalankan schema.sql).\n\n" + e.getMessage(),
                    "Koneksi database gagal", JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
