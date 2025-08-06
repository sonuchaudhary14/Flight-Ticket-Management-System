package pkg2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class UserDAO {
    // SQL Queries
    private static final String INSERT_USER = 
        "INSERT INTO users (email, password, full_name, gender, phone, address, status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_USER = 
        "UPDATE users SET full_name=?, gender=?, phone=?, address=?, updated_at=CURRENT_TIMESTAMP " +
        "WHERE user_id=?";
    
    private static final String UPDATE_PASSWORD = 
        "UPDATE users SET password=?, updated_at=CURRENT_TIMESTAMP WHERE user_id=?";
    
    private static final String UPDATE_STATUS = 
        "UPDATE users SET status=?, updated_at=CURRENT_TIMESTAMP WHERE user_id=?";
    
    private static final String DELETE_USER = 
        "DELETE FROM users WHERE user_id=?";
    
    private static final String GET_USER_BY_EMAIL = 
        "SELECT * FROM users WHERE email=?";
    
    private static final String GET_USER_BY_ID = 
        "SELECT * FROM users WHERE user_id=?";
    
    private static final String GET_ALL_USERS = 
        "SELECT * FROM users ORDER BY created_at DESC";
    
    private static final String GET_USERS_BY_STATUS = 
        "SELECT * FROM users WHERE status=? ORDER BY created_at DESC";
    
    public static User login(String email, String password) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_EMAIL)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next() && rs.getString("password").equals(password)) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setGender(rs.getString("gender"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setStatus(rs.getString("status"));
                return user;
            }
        }
        return null;
    }
    
    public static void register(User user) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {
            
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getGender());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getStatus());
            
            stmt.executeUpdate();
        }
    }
    
    public static void updateUser(User user) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getGender());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getAddress());
            stmt.setInt(5, user.getUserId());
            
            stmt.executeUpdate();
        }
    }
    
    public static void updatePassword(int userId, String newPassword) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PASSWORD)) {
            
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            
            stmt.executeUpdate();
        }
    }
    
    public static void updateStatus(int userId, String status) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STATUS)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, userId);
            
            stmt.executeUpdate();
        }
    }
    
    public static void deleteUser(int userId) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
    
    public static User getUserById(int userId) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_ID)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setGender(rs.getString("gender"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setStatus(rs.getString("status"));
                return user;
            }
        }
        return null;
    }
    
    public static List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY full_name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setStatus(rs.getString("status"));
                users.add(user);
            }
        }
        return users;
    }
    
    public static boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email.toLowerCase());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public static boolean verifyPassword(String email, String password) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email.toLowerCase());
            stmt.setString(2, hashPassword(password));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            throw new SQLException("Error verifying password: " + e.getMessage());
        }
        return false;
    }
    
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
} 