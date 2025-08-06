package pkg2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StaffDAO {
    // SQL Queries
    private static final String INSERT_STAFF = 
        "INSERT INTO staff (email, password, full_name, gender, phone, position, department, status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_STAFF = 
        "UPDATE staff SET full_name=?, gender=?, phone=?, position=?, department=?, updated_at=CURRENT_TIMESTAMP " +
        "WHERE staff_id=?";
    
    private static final String UPDATE_PASSWORD = 
        "UPDATE staff SET password=?, updated_at=CURRENT_TIMESTAMP WHERE staff_id=?";
    
    private static final String UPDATE_STATUS = 
        "UPDATE staff SET status=?, updated_at=CURRENT_TIMESTAMP WHERE staff_id=?";
    
    private static final String DELETE_STAFF = 
        "DELETE FROM staff WHERE staff_id=?";
    
    private static final String GET_STAFF_BY_EMAIL = 
        "SELECT * FROM staff WHERE email=?";
    
    private static final String GET_STAFF_BY_ID = 
        "SELECT * FROM staff WHERE staff_id=?";
    
    private static final String GET_ALL_STAFF = 
        "SELECT * FROM staff ORDER BY created_at DESC";
    
    private static final String GET_STAFF_BY_STATUS = 
        "SELECT * FROM staff WHERE status=? ORDER BY created_at DESC";
    
    private static final String GET_STAFF_BY_DEPARTMENT = 
        "SELECT * FROM staff WHERE department=? AND status='ACTIVE' ORDER BY full_name";
    
    public static void register(Staff staff) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_STAFF)) {
            
            stmt.setString(1, staff.getEmail());
            stmt.setString(2, hashPassword(staff.getPassword()));
            stmt.setString(3, staff.getFullName());
            stmt.setString(4, staff.getGender());
            stmt.setString(5, staff.getPhone());
            stmt.setString(6, staff.getPosition());
            stmt.setString(7, staff.getDepartment());
            stmt.setString(8, staff.getStatus());
            
            stmt.executeUpdate();
        }
    }
    
    public static Staff login(String email, String password) throws SQLException {
        System.out.println("=== Staff Login Attempt ===");
        System.out.println("Email: " + email);
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_STAFF_BY_EMAIL)) {
            
            stmt.setString(1, email.toLowerCase()); // Convert email to lowercase
            System.out.println("Executing query: " + GET_STAFF_BY_EMAIL);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Staff record found in database");
                String storedPassword = rs.getString("password");
                String hashedInputPassword = hashPassword(password);
                String status = rs.getString("status");
                
                System.out.println("Account status: " + status);
                System.out.println("Password comparison:");
                System.out.println("Stored hash : " + storedPassword);
                System.out.println("Input hash  : " + hashedInputPassword);
                System.out.println("Match result: " + storedPassword.equals(hashedInputPassword));
                
                if (storedPassword.equals(hashedInputPassword)) {
                    Staff staff = new Staff();
                    staff.setStaffId(rs.getInt("staff_id"));
                    staff.setEmail(rs.getString("email"));
                    staff.setFullName(rs.getString("full_name"));
                    staff.setGender(rs.getString("gender"));
                    staff.setPhone(rs.getString("phone"));
                    staff.setPosition(rs.getString("position"));
                    staff.setDepartment(rs.getString("department"));
                    staff.setStatus(status);
                    
                    System.out.println("Login successful");
                    System.out.println("Staff ID: " + staff.getStaffId());
                    System.out.println("Status: " + staff.getStatus());
                    
                    if (!"ACTIVE".equals(status)) {
                        System.out.println("Account is not active. Current status: " + status);
                    }
                    
                    return staff;
                } else {
                    System.out.println("Password mismatch");
                }
            } else {
                System.out.println("No staff record found with email: " + email);
            }
        } catch (SQLException e) {
            System.err.println("Database error during staff login:");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            throw e;
        }
        System.out.println("Login failed");
        return null;
    }
    
    public static void updateStaff(Staff staff) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STAFF)) {
            
            stmt.setString(1, staff.getFullName());
            stmt.setString(2, staff.getGender());
            stmt.setString(3, staff.getPhone());
            stmt.setString(4, staff.getPosition());
            stmt.setString(5, staff.getDepartment());
            stmt.setInt(6, staff.getStaffId());
            
            stmt.executeUpdate();
        }
    }
    
    public static void updatePassword(int staffId, String newPassword) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PASSWORD)) {
            
            stmt.setString(1, hashPassword(newPassword));
            stmt.setInt(2, staffId);
            
            stmt.executeUpdate();
        }
    }
    
    public static void updateStatus(int staffId, String status) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STATUS)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, staffId);
            
            stmt.executeUpdate();
        }
    }
    
    public static void deleteStaff(int staffId) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_STAFF)) {
            
            stmt.setInt(1, staffId);
            stmt.executeUpdate();
        }
    }
    
    public static Staff getStaffById(int staffId) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_STAFF_BY_ID)) {
            
            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getInt("staff_id"));
                staff.setEmail(rs.getString("email"));
                staff.setFullName(rs.getString("full_name"));
                staff.setGender(rs.getString("gender"));
                staff.setPhone(rs.getString("phone"));
                staff.setPosition(rs.getString("position"));
                staff.setDepartment(rs.getString("department"));
                staff.setStatus(rs.getString("status"));
                return staff;
            }
        }
        return null;
    }
    
    public static List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_STAFF)) {
            
            while (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getInt("staff_id"));
                staff.setEmail(rs.getString("email"));
                staff.setFullName(rs.getString("full_name"));
                staff.setGender(rs.getString("gender"));
                staff.setPhone(rs.getString("phone"));
                staff.setPosition(rs.getString("position"));
                staff.setDepartment(rs.getString("department"));
                staff.setStatus(rs.getString("status"));
                staffList.add(staff);
            }
        }
        return staffList;
    }
    
    private static boolean emailExists(String email) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_STAFF_BY_EMAIL)) {
            
            stmt.setString(1, email.toLowerCase());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
} 