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
import whats_left.model.ProductionRecord;

/**
 *
 * @author LOQ
 */
public class ProductionRepository {
    public boolean addProduction(ProductionRecord production) {
        String sql = "INSERT INTO production_records (food_id, production_date, total_produced, regular_sold, wasted) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, production.getFoodId());
            statement.setTimestamp(2, production.getProductionDate());
            statement.setInt(3, production.getTotalProduced());
            statement.setInt(4, production.getRegularSold());
            statement.setInt(5, production.getWasted());

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan data produksi: " + e.getMessage());
            return false;
        }
    }

    public List<ProductionRecord> getAllProductions() {
        List<ProductionRecord> productions = new ArrayList<>();
        String sql = "SELECT * FROM production_records";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                ProductionRecord production = new ProductionRecord(
                    result.getInt("id"),
                    result.getInt("food_id"),
                    result.getTimestamp("production_date"),
                    result.getInt("total_produced"),
                    result.getInt("regular_sold"),
                    result.getInt("wasted"),
                    result.getTimestamp("created_at")
                );

                productions.add(production);
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data produksi: " + e.getMessage());
        }

        return productions;
    }

    public ProductionRecord getProductionById(int id) {
        ProductionRecord production = null;
        String sql = "SELECT * FROM production_records WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                production = new ProductionRecord(
                    result.getInt("id"),
                    result.getInt("food_id"),
                    result.getTimestamp("production_date"),
                    result.getInt("total_produced"),
                    result.getInt("regular_sold"),
                    result.getInt("wasted"),
                    result.getTimestamp("created_at")
                );
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mencari data produksi: " + e.getMessage());
        }

        return production;
    }

    public boolean updateProduction(ProductionRecord production) {
        String sql = "UPDATE production_records SET food_id = ?, production_date = ?, total_produced = ?, regular_sold = ?, wasted = ? WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, production.getFoodId());
            statement.setTimestamp(2, production.getProductionDate());
            statement.setInt(3, production.getTotalProduced());
            statement.setInt(4, production.getRegularSold());
            statement.setInt(5, production.getWasted());
            statement.setInt(6, production.getId());

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal mengubah data produksi: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduction(int id) {
        String sql = "DELETE FROM production_records WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal menghapus data produksi: " + e.getMessage());
            return false;
        }
    }
}
