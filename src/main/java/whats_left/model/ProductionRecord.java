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
public class ProductionRecord {
    private int id;
    private int foodId;
    private Timestamp productionDate;
    private int totalProduced;
    private int regularSold;
    private int wasted;
    private Timestamp createdAt;

    public ProductionRecord() {
    }

    public ProductionRecord(int id, int foodId, Timestamp productionDate,
            int totalProduced, int regularSold, int wasted, Timestamp createdAt) {
        this.id = id;
        this.foodId = foodId;
        this.productionDate = productionDate;
        this.totalProduced = totalProduced;
        this.regularSold = regularSold;
        this.wasted = wasted;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public Timestamp getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Timestamp productionDate) {
        this.productionDate = productionDate;
    }

    public int getTotalProduced() {
        return totalProduced;
    }

    public void setTotalProduced(int totalProduced) {
        this.totalProduced = totalProduced;
    }

    public int getRegularSold() {
        return regularSold;
    }

    public void setRegularSold(int regularSold) {
        this.regularSold = regularSold;
    }

    public int getWasted() {
        return wasted;
    }

    public void setWasted(int wasted) {
        this.wasted = wasted;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int calculateSurplus() {
        int surplus = totalProduced - regularSold - wasted;
        return surplus;
    }
}
