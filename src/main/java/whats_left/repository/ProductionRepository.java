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
    public boolean addProduction(ProductionRecord production) throws SQLException {
        String sql = "INSERT INTO production_records "
                + "(food_id, production_date, total_produced, regular_sold, wasted) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, production.getFoodId());
            statement.setTimestamp(2, production.getProductionDate());
            statement.setInt(3, production.getTotalProduced());
            statement.setInt(4, production.getRegularSold());
            statement.setInt(5, production.getWasted());

            int result = statement.executeUpdate();

            return result > 0;

        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup statement: " + e.getMessage());
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup connection: " + e.getMessage());
            }
        }
    }

    public List<ProductionRecord> getAllProductions() throws SQLException {
        List<ProductionRecord> productions = new ArrayList<>();
        String sql = "SELECT * FROM production_records";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            connection = DatabaseUtil.getConnection();
            statement = connection.prepareStatement(sql);
            result = statement.executeQuery();

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

        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup result set: " + e.getMessage());
            }

            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup statement: " + e.getMessage());
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup connection: " + e.getMessage());
            }
        }

        return productions;
    }

    /** Catatan produksi milik satu bisnis (lewat relasi food_items). */
    public List<ProductionRecord> getProductionsByBusiness(int businessId) throws SQLException {
        List<ProductionRecord> productions = new ArrayList<>();
        String sql = "SELECT p.* FROM production_records p "
                + "JOIN food_items f ON p.food_id = f.id "
                + "WHERE f.business_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, businessId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    productions.add(new ProductionRecord(
                            result.getInt("id"),
                            result.getInt("food_id"),
                            result.getTimestamp("production_date"),
                            result.getInt("total_produced"),
                            result.getInt("regular_sold"),
                            result.getInt("wasted"),
                            result.getTimestamp("created_at")
                    ));
                }
            }
        }

        return productions;
    }

    public ProductionRecord getProductionById(int id) throws SQLException {
        ProductionRecord production = null;
        String sql = "SELECT * FROM production_records WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            connection = DatabaseUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            result = statement.executeQuery();

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

        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup result set: " + e.getMessage());
            }

            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup statement: " + e.getMessage());
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup connection: " + e.getMessage());
            }
        }

        return production;
    }

    public boolean updateProduction(ProductionRecord production) throws SQLException {
        String sql = "UPDATE production_records SET "
                + "food_id = ?, production_date = ?, total_produced = ?, "
                + "regular_sold = ?, wasted = ? WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, production.getFoodId());
            statement.setTimestamp(2, production.getProductionDate());
            statement.setInt(3, production.getTotalProduced());
            statement.setInt(4, production.getRegularSold());
            statement.setInt(5, production.getWasted());
            statement.setInt(6, production.getId());

            int result = statement.executeUpdate();

            return result > 0;

        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup statement: " + e.getMessage());
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup connection: " + e.getMessage());
            }
        }
    }

    public boolean deleteProduction(int id) throws SQLException {
        String sql = "DELETE FROM production_records WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            int result = statement.executeUpdate();

            return result > 0;

        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup statement: " + e.getMessage());
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup connection: " + e.getMessage());
            }
        }
    }
}
