package whats_left.model;

import java.sql.Timestamp;

/**
 * Akun pengguna aplikasi (Business atau Customer).
 */
public class User {
    public static final String ROLE_BUSINESS = "Business";
    public static final String ROLE_CUSTOMER = "Customer";

    private int id;
    private String username;
    private String passwordHash;
    private String role;
    private Integer businessId; // hanya terisi untuk role Business
    private String fullName;
    private String phone;
    private Timestamp createdAt;

    public User() {
    }

    public User(int id, String username, String passwordHash, String role,
            Integer businessId, String fullName, String phone,
            Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.businessId = businessId;
        this.fullName = fullName;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isBusiness() {
        return ROLE_BUSINESS.equals(role);
    }

    public boolean isCustomer() {
        return ROLE_CUSTOMER.equals(role);
    }
}
