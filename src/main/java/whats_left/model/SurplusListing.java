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
public class SurplusListing {
    private int id;
    private int productionId;
    private double discountPercentage;
    private double discountedPrice;
    private int initialQuantity;
    private int availableQuantity;
    private Timestamp pickupStart;
    private Timestamp pickupEnd;
    private String status;
    private Timestamp createdAt;

    public SurplusListing() {
    }

    public SurplusListing(int id, int productionId, double discountPercentage,
            double discountedPrice, int initialQuantity, int availableQuantity,
            Timestamp pickupStart, Timestamp pickupEnd, String status,
            Timestamp createdAt) {
        this.id = id;
        this.productionId = productionId;
        this.discountPercentage = discountPercentage;
        this.discountedPrice = discountedPrice;
        this.initialQuantity = initialQuantity;
        this.availableQuantity = availableQuantity;
        this.pickupStart = pickupStart;
        this.pickupEnd = pickupEnd;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductionId() {
        return productionId;
    }

    public void setProductionId(int productionId) {
        this.productionId = productionId;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Timestamp getPickupStart() {
        return pickupStart;
    }

    public void setPickupStart(Timestamp pickupStart) {
        this.pickupStart = pickupStart;
    }

    public Timestamp getPickupEnd() {
        return pickupEnd;
    }

    public void setPickupEnd(Timestamp pickupEnd) {
        this.pickupEnd = pickupEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int calculateReservedQuantity() {
        int reservedQuantity = initialQuantity - availableQuantity;
        return reservedQuantity;
    }

    public double calculateTotalPrice(int quantity) {
        double totalPrice = discountedPrice * quantity;
        return totalPrice;
    }
}
