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
import whats_left.model.SurplusListing;

/**
 *
 * @author LOQ
 */
public class SurplusRepository {
    public boolean addSurplus(SurplusListing surplus) {
        String sql = "INSERT INTO surplus_listings (production_id, discount_percentage, discounted_price, initial_quantity, available_quantity, pickup_start, pickup_end, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, surplus.getProductionId());
            statement.setDouble(2, surplus.getDiscountPercentage());
            statement.setDouble(3, surplus.getDiscountedPrice());
            statement.setInt(4, surplus.getInitialQuantity());
            statement.setInt(5, surplus.getAvailableQuantity());
            statement.setTimestamp(6, surplus.getPickupStart());
            statement.setTimestamp(7, surplus.getPickupEnd());
            statement.setString(8, surplus.getStatus());

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan surplus: " + e.getMessage());
            return false;
        }
    }

    public List<SurplusListing> getAllSurpluses() {
        List<SurplusListing> surpluses = new ArrayList<>();
        String sql = "SELECT * FROM surplus_listings";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                SurplusListing surplus = new SurplusListing(
                    result.getInt("id"),
                    result.getInt("production_id"),
                    result.getDouble("discount_percentage"),
                    result.getDouble("discounted_price"),
                    result.getInt("initial_quantity"),
                    result.getInt("available_quantity"),
                    result.getTimestamp("pickup_start"),
                    result.getTimestamp("pickup_end"),
                    result.getString("status"),
                    result.getTimestamp("created_at")
                );

                surpluses.add(surplus);
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data surplus: " + e.getMessage());
        }

        return surpluses;
    }

    public SurplusListing getSurplusById(int id) {
        SurplusListing surplus = null;
        String sql = "SELECT * FROM surplus_listings WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                surplus = new SurplusListing(
                    result.getInt("id"),
                    result.getInt("production_id"),
                    result.getDouble("discount_percentage"),
                    result.getDouble("discounted_price"),
                    result.getInt("initial_quantity"),
                    result.getInt("available_quantity"),
                    result.getTimestamp("pickup_start"),
                    result.getTimestamp("pickup_end"),
                    result.getString("status"),
                    result.getTimestamp("created_at")
                );
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mencari data surplus: " + e.getMessage());
        }

        return surplus;
    }

    public boolean updateSurplus(SurplusListing surplus) {
        String sql = "UPDATE surplus_listings SET production_id = ?, discount_percentage = ?, discounted_price = ?, initial_quantity = ?, available_quantity = ?, pickup_start = ?, pickup_end = ?, status = ? WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, surplus.getProductionId());
            statement.setDouble(2, surplus.getDiscountPercentage());
            statement.setDouble(3, surplus.getDiscountedPrice());
            statement.setInt(4, surplus.getInitialQuantity());
            statement.setInt(5, surplus.getAvailableQuantity());
            statement.setTimestamp(6, surplus.getPickupStart());
            statement.setTimestamp(7, surplus.getPickupEnd());
            statement.setString(8, surplus.getStatus());
            statement.setInt(9, surplus.getId());

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal mengubah data surplus: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSurplus(int id) {
        String sql = "DELETE FROM surplus_listings WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal menghapus data surplus: " + e.getMessage());
            return false;
        }
    }
}
