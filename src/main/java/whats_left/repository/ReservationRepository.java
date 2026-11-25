/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package whats_left.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import whats_left.model.Reservation;

/**
 *
 * @author LOQ
 */
public class ReservationRepository {
    public boolean addReservation(Reservation reservation) {
        String updateStock = "UPDATE surplus_listings "
                + "SET available_quantity = available_quantity - ? "
                + "WHERE id = ? AND status = 'Available' "
                + "AND available_quantity >= ?";

        String updateStatus = "UPDATE surplus_listings "
                + "SET status = 'Sold Out' "
                + "WHERE id = ? AND available_quantity = 0";

        String insertReservation = "INSERT INTO reservations "
                + "(listing_id, customer_name, customer_phone, "
                + "reservation_code, quantity, total_price, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;

        try {
            if (reservation.getQuantity() <= 0) {
                return false;
            }

            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement stockStatement
                    = connection.prepareStatement(updateStock);

            stockStatement.setInt(1, reservation.getQuantity());
            stockStatement.setInt(2, reservation.getListingId());
            stockStatement.setInt(3, reservation.getQuantity());

            int stockResult = stockStatement.executeUpdate();
            stockStatement.close();

            if (stockResult == 0) {
                connection.rollback();
                return false;
            }

            PreparedStatement statusStatement
                    = connection.prepareStatement(updateStatus);

            statusStatement.setInt(1, reservation.getListingId());
            statusStatement.executeUpdate();
            statusStatement.close();

            PreparedStatement reservationStatement
                    = connection.prepareStatement(insertReservation);

            reservationStatement.setInt(1, reservation.getListingId());
            reservationStatement.setString(2, reservation.getCustomerName());
            reservationStatement.setString(3, reservation.getCustomerPhone());
            reservationStatement.setString(4, reservation.getReservationCode());
            reservationStatement.setInt(5, reservation.getQuantity());
            reservationStatement.setDouble(6, reservation.getTotalPrice());
            reservationStatement.setString(7, reservation.getStatus());

            reservationStatement.executeUpdate();
            reservationStatement.close();

            connection.commit();

            return true;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Gagal membatalkan transaksi: " + ex.getMessage());
            }

            System.out.println("Gagal menambahkan reservasi: " + e.getMessage());
            return false;

        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Reservation reservation = new Reservation(
                    result.getInt("id"),
                    result.getInt("listing_id"),
                    result.getString("customer_name"),
                    result.getString("customer_phone"),
                    result.getString("reservation_code"),
                    result.getInt("quantity"),
                    result.getDouble("total_price"),
                    result.getTimestamp("reservation_date"),
                    result.getString("status")
                );

                reservations.add(reservation);
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data reservasi: " + e.getMessage());
        }

        return reservations;
    }

    public Reservation getReservationById(int id) {
        Reservation reservation = null;
        String sql = "SELECT * FROM reservations WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                reservation = new Reservation(
                    result.getInt("id"),
                    result.getInt("listing_id"),
                    result.getString("customer_name"),
                    result.getString("customer_phone"),
                    result.getString("reservation_code"),
                    result.getInt("quantity"),
                    result.getDouble("total_price"),
                    result.getTimestamp("reservation_date"),
                    result.getString("status")
                );
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mencari reservasi: " + e.getMessage());
        }

        return reservation;
    }

    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_CONFIRMED = "Confirmed";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";

    /**
     * Mengubah status reservasi (dipakai Business).
     * Completed dan Cancelled bersifat final. Jika menjadi Cancelled, stok
     * penawaran dikembalikan.
     */
    public boolean updateReservationStatus(int id, String status) {
        return changeStatus(id, status, null) == null;
    }

    /**
     * Customer membatalkan reservasinya sendiri. Hanya boleh saat status
     * masih Pending. Mengembalikan null jika berhasil, atau pesan error.
     */
    public String cancelReservationByCustomer(int id, String customerPhone) {
        return changeStatus(id, STATUS_CANCELLED, customerPhone);
    }

    /**
     * Ubah status dalam satu transaksi.
     *
     * @param customerPhone null untuk Business (tanpa batasan kepemilikan);
     *                      terisi untuk Customer (hanya boleh membatalkan
     *                      reservasi miliknya yang masih Pending)
     * @return null jika berhasil, selain itu pesan error
     */
    public String changeStatus(int id, String newStatus, String customerPhone) {
        if (!STATUS_PENDING.equals(newStatus)
                && !STATUS_CONFIRMED.equals(newStatus)
                && !STATUS_COMPLETED.equals(newStatus)
                && !STATUS_CANCELLED.equals(newStatus)) {
            return "Status tidak dikenal: " + newStatus;
        }

        String select = "SELECT listing_id, quantity, status, customer_phone "
                + "FROM reservations WHERE id = ? FOR UPDATE";
        String updateStatus = "UPDATE reservations SET status = ? WHERE id = ?";
        String restoreStock = "UPDATE surplus_listings "
                + "SET available_quantity = LEAST(initial_quantity, available_quantity + ?), "
                + "status = CASE WHEN status = 'Sold Out' THEN 'Available' ELSE status END "
                + "WHERE id = ?";

        Connection connection = null;

        try {
            connection = DatabaseUtil.getConnection();
            connection.setAutoCommit(false);

            int listingId;
            int quantity;
            String currentStatus;
            String ownerPhone;

            try (PreparedStatement statement = connection.prepareStatement(select)) {
                statement.setInt(1, id);

                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        connection.rollback();
                        return "Reservasi tidak ditemukan.";
                    }

                    listingId = result.getInt("listing_id");
                    quantity = result.getInt("quantity");
                    currentStatus = result.getString("status");
                    ownerPhone = result.getString("customer_phone");
                }
            }

            String error = null;

            if (customerPhone != null) {
                if (!customerPhone.equals(ownerPhone)) {
                    error = "Reservasi ini bukan milik Anda.";
                } else if (!STATUS_CANCELLED.equals(newStatus)) {
                    error = "Customer hanya dapat membatalkan reservasi.";
                } else if (!STATUS_PENDING.equals(currentStatus)) {
                    error = "Reservasi hanya dapat dibatalkan saat status masih Pending.";
                }
            }

            if (error == null
                    && (STATUS_COMPLETED.equals(currentStatus)
                    || STATUS_CANCELLED.equals(currentStatus))
                    && !currentStatus.equals(newStatus)) {
                error = "Reservasi yang sudah " + currentStatus
                        + " tidak dapat diubah lagi.";
            }

            if (error != null) {
                connection.rollback();
                return error;
            }

            if (currentStatus != null && currentStatus.equals(newStatus)) {
                connection.rollback();
                return null; // tidak ada perubahan
            }

            try (PreparedStatement statement = connection.prepareStatement(updateStatus)) {
                statement.setString(1, newStatus);
                statement.setInt(2, id);
                statement.executeUpdate();
            }

            if (STATUS_CANCELLED.equals(newStatus)) {
                try (PreparedStatement statement = connection.prepareStatement(restoreStock)) {
                    statement.setInt(1, quantity);
                    statement.setInt(2, listingId);
                    statement.executeUpdate();
                }
            }

            connection.commit();
            return null;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Gagal rollback: " + ex.getMessage());
            }

            System.out.println("Gagal mengubah status reservasi: " + e.getMessage());
            return "Gagal mengubah status reservasi: " + e.getMessage();

        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }

    /**
     * Menghapus riwayat reservasi. Hanya reservasi yang sudah selesai
     * (Completed) atau dibatalkan (Cancelled) yang boleh dihapus.
     */
    public boolean deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE id = ? "
                + "AND status IN ('Completed', 'Cancelled')";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            int deleted = statement.executeUpdate();

            statement.close();
            connection.close();

            return deleted > 0;
        } catch (SQLException e) {
            System.out.println("Gagal menghapus reservasi: " + e.getMessage());
            return false;
        }
    }

    /** Reservasi yang masuk ke satu bisnis (lewat listing, produksi, makanan). */
    public List<Reservation> getReservationsByBusiness(int businessId) {
        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT r.* FROM reservations r "
                + "JOIN surplus_listings s ON r.listing_id = s.id "
                + "JOIN production_records p ON s.production_id = p.id "
                + "JOIN food_items f ON p.food_id = f.id "
                + "WHERE f.business_id = ? "
                + "ORDER BY r.reservation_date DESC, r.id DESC";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, businessId);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                reservations.add(new Reservation(
                        result.getInt("id"),
                        result.getInt("listing_id"),
                        result.getString("customer_name"),
                        result.getString("customer_phone"),
                        result.getString("reservation_code"),
                        result.getInt("quantity"),
                        result.getDouble("total_price"),
                        result.getTimestamp("reservation_date"),
                        result.getString("status")
                ));
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mengambil reservasi bisnis: " + e.getMessage());
        }

        return reservations;
    }

    public List<Reservation> getReservationsByPhone(String customerPhone) {
        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT * FROM reservations WHERE customer_phone = ? "
                + "ORDER BY reservation_date DESC, id DESC";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, customerPhone);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Reservation reservation = new Reservation(
                        result.getInt("id"),
                        result.getInt("listing_id"),
                        result.getString("customer_name"),
                        result.getString("customer_phone"),
                        result.getString("reservation_code"),
                        result.getInt("quantity"),
                        result.getDouble("total_price"),
                        result.getTimestamp("reservation_date"),
                        result.getString("status")
                );

                reservations.add(reservation);
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mencari reservasi berdasarkan nomor telepon: "
                    + e.getMessage());
        }

        return reservations;
    }
}
