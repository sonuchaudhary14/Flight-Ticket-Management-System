package pkg2;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class User {
    private int userId;
    private String email;
    private String password;
    private String fullName;
    private String gender;
    private String phone;
    private String address;
    private String status;
    private LocalDateTime createdAt;
    
    // Regular expression for email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    
    // Regular expression for phone number validation
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\+?[1-9][0-9]{7,14}$"
    );
    
    public User() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        if (userId < 0) throw new IllegalArgumentException("User ID cannot be negative");
        this.userId = userId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        this.fullName = fullName.trim();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.trim().toLowerCase();
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        this.password = password;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        this.phone = cleanPhone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        this.address = address.trim();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Created date cannot be null");
        }
        this.createdAt = createdAt;
    }
} 