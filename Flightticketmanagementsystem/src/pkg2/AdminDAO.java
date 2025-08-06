package pkg2;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    // SQL Queries
    private static final String GET_ADMIN_BY_EMAIL = 
        "SELECT * FROM admin WHERE email=?";
    
    private static final String UPDATE_ADMIN = 
        "UPDATE admin SET full_name=? WHERE admin_id=?";
    
    private static final String UPDATE_PASSWORD = 
        "UPDATE admin SET password=? WHERE admin_id=?";
        
    private static final String INSERT_DEFAULT_ADMIN =
        "INSERT INTO admin (email, password, full_name) VALUES (?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE password=VALUES(password), full_name=VALUES(full_name)";
    
    private static final String CHECK_AND_UPDATE_ADMIN =
        "UPDATE admin SET password=? WHERE email=?";
    
    static {
        // Create or update default admin account
        try {
            createOrUpdateDefaultAdmin();
        } catch (SQLException e) {
            System.err.println("Error creating/updating default admin: " + e.getMessage());
        }
    }
    
    private static void createOrUpdateDefaultAdmin() throws SQLException {
        String defaultEmail = "admin@system.com";
        String defaultPassword = hashPassword("admin123");
        String defaultName = "System Administrator";
        
        // First try to update existing admin
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_AND_UPDATE_ADMIN)) {
            stmt.setString(1, defaultPassword);
            stmt.setString(2, defaultEmail);
            int updated = stmt.executeUpdate();
            
            // If no admin exists, create new one
            if (updated == 0) {
                try (PreparedStatement insertStmt = conn.prepareStatement(INSERT_DEFAULT_ADMIN)) {
                    insertStmt.setString(1, defaultEmail);
                    insertStmt.setString(2, defaultPassword);
                    insertStmt.setString(3, defaultName);
                    insertStmt.executeUpdate();
                    System.out.println("Default admin account created with hashed password.");
                }
            } else {
                System.out.println("Default admin account password updated.");
            }
        }
    }
    
    public static Admin login(String email, String password) throws SQLException {
        System.out.println("Attempting login for email: " + email);
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ADMIN_BY_EMAIL)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String hashedInputPassword = hashPassword(password);
                
                System.out.println("Debug - Stored password hash: " + storedPassword);
                System.out.println("Debug - Input password hash: " + hashedInputPassword);
                
                if (storedPassword.equals(hashedInputPassword)) {
                    Admin admin = new Admin();
                    admin.setAdminId(rs.getInt("admin_id"));
                    admin.setEmail(rs.getString("email"));
                    admin.setFullName(rs.getString("full_name"));
                    System.out.println("Login successful for: " + email);
                    return admin;
                } else {
                    System.out.println("Password mismatch for: " + email);
                    System.out.println("Expected: " + storedPassword);
                    System.out.println("Received: " + hashedInputPassword);
                }
            } else {
                System.out.println("No admin found with email: " + email);
            }
        }
        return null;
    }
    
    public static void updateAdmin(Admin admin) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ADMIN)) {
            
            stmt.setString(1, admin.getFullName());
            stmt.setInt(2, admin.getAdminId());
            
            stmt.executeUpdate();
        }
    }
    
    public static void updatePassword(int adminId, String newPassword) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PASSWORD)) {
            
            stmt.setString(1, hashPassword(newPassword));
            stmt.setInt(2, adminId);
            
            stmt.executeUpdate();
        }
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
            
            String hashedPassword = hexString.toString();
            System.out.println("Debug - Hashing password. Result: " + hashedPassword);
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
} 