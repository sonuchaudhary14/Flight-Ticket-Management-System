package pkg2;

import java.time.LocalDateTime;

public class Admin {
    private int adminId;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
    
    public Admin() {
        this.createdAt = LocalDateTime.now();
        this.role = "ADMIN";
    }
    
    public Admin(String email, String password, String fullName, String phone) {
        this();
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
    }
    
    // Getters and setters
    public int getAdminId() {
        return adminId;
    }
    
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        if (!phone.matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        this.phone = phone.trim();
    }
    
    public String getRole() {
        return role;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 