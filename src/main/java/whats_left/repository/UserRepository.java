package whats_left.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import whats_left.model.User;

/**
 * Mengelola akun pengguna (login dan register).
 */
public class UserRepository {

    /**
     * Login. Mengembalikan User jika username dan password cocok,
     * atau null jika salah.
     */
    public User authenticate(String username, char[] password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    User user = mapUser(result);

                    if (PasswordUtil.verify(password, user.getPasswordHash())) {
                        return user;
                    }
                }
            }
        }

        return null;
    }

    public boolean usernameExists(String username) throws SQLException {
        return exists("SELECT 1 FROM users WHERE username = ?", username);
    }

    public boolean customerPhoneExists(String phone) throws SQLException {
        return exists("SELECT 1 FROM users WHERE role = 'Customer' AND phone = ?",
                phone);
    }

    private boolean exists(String sql, String value) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, value);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    /** Daftar akun Customer. */
    public void registerCustomer(String username, char[] password,
            String fullName, String phone) throws SQLException {
        String sql = "INSERT INTO users "
                + "(username, password_hash, role, business_id, full_name, phone) "
                + "VALUES (?, ?, 'Customer', NULL, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, PasswordUtil.hash(password));
            statement.setString(3, fullName);
            statement.setString(4, phone);
            statement.executeUpdate();
        }
    }

    /**
     * Daftar akun Business. Baris bisnis dan akun dibuat dalam satu transaksi,
     * jadi keduanya berhasil atau keduanya dibatalkan.
     */
    public void registerBusiness(String username, char[] password,
            String businessName, String address, String phone) throws SQLException {
        String insertBusiness
                = "INSERT INTO businesses (business_name, address, phone) VALUES (?, ?, ?)";
        String insertUser = "INSERT INTO users "
                + "(username, password_hash, role, business_id, full_name, phone) "
                + "VALUES (?, ?, 'Business', ?, ?, ?)";

        Connection connection = null;

        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false);

            int businessId;

            try (PreparedStatement statement = connection.prepareStatement(
                    insertBusiness, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, businessName);
                statement.setString(2, address);
                statement.setString(3, phone);
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("ID bisnis tidak dihasilkan");
                    }
                    businessId = keys.getInt(1);
                }
            }

            try (PreparedStatement statement = connection.prepareStatement(insertUser)) {
                statement.setString(1, username);
                statement.setString(2, PasswordUtil.hash(password));
                statement.setInt(3, businessId);
                statement.setString(4, businessName);
                statement.setString(5, phone);
                statement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println("Gagal rollback: " + ex.getMessage());
                }
            }
            throw e;

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Gagal menutup koneksi: " + e.getMessage());
                }
            }
        }
    }

    private User mapUser(ResultSet result) throws SQLException {
        int businessId = result.getInt("business_id");
        Integer businessIdValue = result.wasNull() ? null : businessId;

        return new User(
                result.getInt("id"),
                result.getString("username"),
                result.getString("password_hash"),
                result.getString("role"),
                businessIdValue,
                result.getString("full_name"),
                result.getString("phone"),
                result.getTimestamp("created_at")
        );
    }
}
