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

    public boolean updateReservationStatus(int id, String status) {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, status);
            statement.setInt(2, id);

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal mengubah status reservasi: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal menghapus reservasi: " + e.getMessage());
            return false;
        }
    }
    
    public List<Reservation> getReservationsByPhone(String customerPhone) {
        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT * FROM reservations WHERE customer_phone = ?";

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
