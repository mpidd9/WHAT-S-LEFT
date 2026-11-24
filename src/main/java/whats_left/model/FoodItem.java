/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package whats_left.model;

import java.sql.Timestamp;

/**
 *
 * @author LOQ
 */
public class FoodItem {
    private int id;
    private int businessId;
    private String foodName;
    private String category;
    private String description;
    private double regularPrice;
    private Timestamp createdAt;

    public FoodItem() {
    }

    public FoodItem(int id, int businessId, String foodName, String category,
            String description, double regularPrice, Timestamp createdAt) {
        this.id = id;
        this.businessId = businessId;
        this.foodName = foodName;
        this.category = category;
        this.description = description;
        this.regularPrice = regularPrice;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public double calculateDiscountedPrice(double discountPercentage) {
        double discountedPrice = regularPrice - (regularPrice * discountPercentage / 100);
        return discountedPrice;
    }

    public String getFoodInfo() {
        return foodName + " - " + category + " - " + regularPrice;
    }
}
