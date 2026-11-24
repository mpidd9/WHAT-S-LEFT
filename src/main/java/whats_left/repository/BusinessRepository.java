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
import whats_left.model.Business;

/**
 *
 * @author LOQ
 */
public class BusinessRepository {
    // Menambahkan data bisnis
    public boolean addBusiness(Business business) {
        String sql = "INSERT INTO businesses (business_name, address, phone) VALUES (?, ?, ?)";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, business.getBusinessName());
            statement.setString(2, business.getAddress());
            statement.setString(3, business.getPhone());

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Gagal menambahkan data bisnis: " + e.getMessage());
            return false;
        }
    }

    // Mengambil seluruh data bisnis
    public List<Business> getAllBusinesses() {
        List<Business> businesses = new ArrayList<>();
        String sql = "SELECT * FROM businesses";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Business business = new Business(
                    result.getInt("id"),
                    result.getString("business_name"),
                    result.getString("address"),
                    result.getString("phone")
                );

                businesses.add(business);
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data bisnis: " + e.getMessage());
        }

        return businesses;
    }

    // Mengambil data bisnis berdasarkan ID
    public Business getBusinessById(int id) {
        Business business = null;
        String sql = "SELECT * FROM businesses WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                business = new Business(
                    result.getInt("id"),
                    result.getString("business_name"),
                    result.getString("address"),
                    result.getString("phone")
                );
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mencari data bisnis: " + e.getMessage());
        }

        return business;
    }

    // Mengubah data bisnis
    public boolean updateBusiness(Business business) {
        String sql = "UPDATE businesses SET business_name = ?, address = ?, phone = ? WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, business.getBusinessName());
            statement.setString(2, business.getAddress());
            statement.setString(3, business.getPhone());
            statement.setInt(4, business.getId());

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Gagal mengubah data bisnis: " + e.getMessage());
            return false;
        }
    }

    // Menghapus data bisnis
    public boolean deleteBusiness(int id) {
        String sql = "DELETE FROM businesses WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Gagal menghapus data bisnis: " + e.getMessage());
            return false;
        }
    }
}
