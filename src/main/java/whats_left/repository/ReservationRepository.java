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
        String sql = "INSERT INTO reservations (listing_id, customer_name, customer_phone, reservation_code, quantity, total_price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, reservation.getListingId());
            statement.setString(2, reservation.getCustomerName());
            statement.setString(3, reservation.getCustomerPhone());
            statement.setString(4, reservation.getReservationCode());
            statement.setInt(5, reservation.getQuantity());
            statement.setDouble(6, reservation.getTotalPrice());
            statement.setString(7, reservation.getStatus());

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan reservasi: " + e.getMessage());
            return false;
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
}
