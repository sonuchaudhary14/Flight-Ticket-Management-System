package pkg2;

import java.time.LocalDateTime;

public class Staff {
    private int staffId;
    private String email;
    private String password;
    private String fullName;
    private String gender;
    private String phone;
    private String position;
    private String department;
    private String status;
    private LocalDateTime createdAt;
    
    public Staff() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING"; // Default status for new staff
    }
    
    public int getStaffId() {
        return staffId;
    }
    
    public void setStaffId(int staffId) {
        this.staffId = staffId;
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
        this.phone = phone;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
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
    
    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 