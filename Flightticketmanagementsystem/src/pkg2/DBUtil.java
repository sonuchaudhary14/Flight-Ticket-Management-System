package pkg2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/flight_management";
    private static final String USER = "root";
    private static final String PASSWORD = "pcps12@";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL driver", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("Attempting to connect to database at: " + URL);
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
            System.err.println("Connection URL: " + URL);
            System.err.println("Please check if:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'flight_management' exists");
            System.err.println("3. User 'root' has correct permissions");
            System.err.println("4. Password is correct");
            throw e;
        }
    }
    
    // Test connection method
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Test connection successful!");
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Connection Test Failed: " + e.getMessage());
            return false;
        }
    }
} 