/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package whats_left.model;

/**
 *
 * @author LOQ
 */
public class Business {
    private int id;
    private String businessName;
    private String address;
    private String phone;

    public Business() {
    }

    public Business(int id, String businessName, String address, String phone) {
        this.id = id;
        this.businessName = businessName;
        this.address = address;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessInfo() {
        return businessName + " - " + address + " - " + phone;
    }
}
