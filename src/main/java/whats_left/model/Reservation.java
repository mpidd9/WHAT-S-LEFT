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
public class Reservation {
    private int id;
    private int listingId;
    private String customerName;
    private String customerPhone;
    private String reservationCode;
    private int quantity;
    private double totalPrice;
    private Timestamp reservationDate;
    private String status;

    public Reservation() {
    }

    public Reservation(int id, int listingId, String customerName,
            String customerPhone, String reservationCode, int quantity,
            double totalPrice, Timestamp reservationDate, String status) {
        this.id = id;
        this.listingId = listingId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.reservationCode = reservationCode;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
