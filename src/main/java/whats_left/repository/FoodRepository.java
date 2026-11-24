/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package whats_left.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import whats_left.model.FoodItem;

/**
 *
 * @author LOQ
 */
public class FoodRepository {
    public boolean addFood(FoodItem food) {
        String sql = "INSERT INTO food_items (business_id, food_name, category, description, regular_price) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, food.getBusinessId());
            statement.setString(2, food.getFoodName());
            statement.setString(3, food.getCategory());
            statement.setString(4, food.getDescription());
            statement.setDouble(5, food.getRegularPrice());

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan makanan: " + e.getMessage());
            return false;
        }
    }

    public List<FoodItem> getAllFoods() {
        List<FoodItem> foods = new ArrayList<>();
        String sql = "SELECT * FROM food_items";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                FoodItem food = new FoodItem(
                    result.getInt("id"),
                    result.getInt("business_id"),
                    result.getString("food_name"),
                    result.getString("category"),
                    result.getString("description"),
                    result.getDouble("regular_price"),
                    result.getTimestamp("created_at")
                );

                foods.add(food);
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data makanan: " + e.getMessage());
        }

        return foods;
    }

    public FoodItem getFoodById(int id) {
        FoodItem food = null;
        String sql = "SELECT * FROM food_items WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                food = new FoodItem(
                    result.getInt("id"),
                    result.getInt("business_id"),
                    result.getString("food_name"),
                    result.getString("category"),
                    result.getString("description"),
                    result.getDouble("regular_price"),
                    result.getTimestamp("created_at")
                );
            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Gagal mencari makanan: " + e.getMessage());
        }

        return food;
    }

    
    public boolean updateFood(FoodItem food) {
        String sql = "UPDATE food_items SET food_name = ?, category = ?, regular_price = ? WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, food.getFoodName());
            statement.setString(2, food.getCategory());
            statement.setDouble(3, food.getRegularPrice());
            statement.setInt(4, food.getId());

            int result = statement.executeUpdate();

            statement.close();
            connection.close();

            return result > 0;
        } catch (SQLException e) {
            System.out.println("Gagal mengubah data makanan: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteFood(int id) {
        String sql = "DELETE FROM food_items WHERE id = ?";

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Gagal menghapus makanan: " + e.getMessage());
            return false;
        }
    }
}
