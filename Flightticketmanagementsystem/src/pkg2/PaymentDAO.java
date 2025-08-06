package pkg2;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    public static void addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (ticket_id, amount, payment_method, payment_status, payment_date, transaction_id, remarks) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, payment.getTicketId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setString(4, payment.getPaymentStatus());
            pstmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));
            pstmt.setString(6, payment.getTransactionId());
            pstmt.setString(7, payment.getRemarks());
            
            pstmt.executeUpdate();
        }
    }
    
    public static Payment getPaymentByTicket(int ticketId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ticketId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("payment_id"));
                    payment.setTicketId(rs.getInt("ticket_id"));
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setPaymentStatus(rs.getString("payment_status"));
                    payment.setTransactionId(rs.getString("transaction_id"));
                    payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                    payment.setRemarks(rs.getString("remarks"));
                    return payment;
                }
            }
        }
        return null;
    }
    
    public static List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payments.add(new Payment(
                    rs.getInt("payment_id"),
                    rs.getInt("ticket_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_method"),
                    rs.getString("payment_status"),
                    rs.getString("transaction_id"),
                    rs.getTimestamp("payment_date").toLocalDateTime(),
                    rs.getString("remarks")
                ));
            }
        }
        return payments;
    }
    
    public static void updatePaymentStatus(int paymentId, String status) throws SQLException {
        String sql = "UPDATE payments SET payment_status = ? WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, paymentId);
            
            pstmt.executeUpdate();
        }
    }
    
    public static void deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            pstmt.executeUpdate();
        }
    }
} 