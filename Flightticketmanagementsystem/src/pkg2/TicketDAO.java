package pkg2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class TicketDAO {
    private static final String INSERT_TICKET = "INSERT INTO tickets (user_id, flight_id, full_name, flight_number, seat_number, status, payment_status, price, booking_datetime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_TICKET = "UPDATE tickets SET user_id = ?, flight_id = ?, full_name = ?, flight_number = ?, seat_number = ?, status = ?, payment_status = ?, price = ?, booking_datetime = ? WHERE ticket_id = ?";
    private static final String DELETE_TICKET = "DELETE FROM tickets WHERE ticket_id = ?";
    private static final String SELECT_ALL_TICKETS = "SELECT t.*, p.payment_status as payment_status, p.payment_method, p.transaction_id, p.payment_date FROM tickets t LEFT JOIN payments p ON t.ticket_id = p.ticket_id ORDER BY t.booking_datetime DESC";
    private static final String SELECT_TICKET_BY_ID = "SELECT t.*, p.payment_status as payment_status, p.payment_method, p.transaction_id, p.payment_date FROM tickets t LEFT JOIN payments p ON t.ticket_id = p.ticket_id WHERE t.ticket_id = ?";
    private static final String SELECT_TICKETS_BY_USER = "SELECT t.*, p.payment_status, p.payment_method, p.transaction_id, p.payment_date, p.remarks FROM tickets t LEFT JOIN payments p ON t.ticket_id = p.ticket_id WHERE t.user_id = ? ORDER BY t.booking_datetime DESC";
    private static final String SELECT_TICKETS_BY_FLIGHT = "SELECT t.*, p.payment_status as payment_status, p.payment_method, p.transaction_id, p.payment_date FROM tickets t LEFT JOIN payments p ON t.ticket_id = p.ticket_id WHERE t.flight_id = ? ORDER BY t.booking_datetime DESC";
    private static final String UPDATE_TICKET_STATUS = "UPDATE tickets SET status = ? WHERE ticket_id = ? AND payment_status = 'COMPLETED'";
    
    public static List<Ticket> getAllTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_TICKETS)) {
            
            while (rs.next()) {
                tickets.add(createTicketFromResultSet(rs));
            }
        }
        
        return tickets;
    }
    
    public static void addTicket(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO tickets (user_id, flight_id, full_name, flight_number, seat_number, status, payment_status, price, booking_datetime, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, ticket.getUserId());
            pstmt.setInt(2, ticket.getFlightId());
            pstmt.setString(3, ticket.getFullName());
            pstmt.setString(4, ticket.getFlightNumber());
            pstmt.setString(5, ticket.getSeatNumber());
            pstmt.setString(6, ticket.getStatus());
            pstmt.setString(7, ticket.getPaymentStatus());
            pstmt.setDouble(8, ticket.getPrice());
            pstmt.setTimestamp(9, Timestamp.valueOf(ticket.getBookingDateTime()));
            pstmt.setString(10, ticket.getPaymentMethod());
            
            pstmt.executeUpdate();
            
            // Get the generated ticket ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setTicketId(rs.getInt(1));
                    
                    // Create payment record
                    Payment payment = new Payment(ticket.getTicketId(), ticket.getPrice(), ticket.getPaymentMethod());
                    payment.setTransactionId("TXN" + System.currentTimeMillis());
                    payment.setPaymentStatus("COMPLETED");
                    payment.setPaymentDate(LocalDateTime.now());
                    PaymentDAO.addPayment(payment);
                }
            }
        }
    }
    
    public static void updateTicket(Ticket ticket) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_TICKET)) {
            
            setTicketParameters(stmt, ticket);
            stmt.setInt(9, ticket.getTicketId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating ticket failed, no rows affected.");
            }
        }
    }
    
    public static void deleteTicket(int ticketId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_TICKET)) {
            
            stmt.setInt(1, ticketId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting ticket failed, no rows affected.");
            }
        }
    }
    
    public static Ticket getTicketById(int ticketId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TICKET_BY_ID)) {
            
            stmt.setInt(1, ticketId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createTicketFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public static List<Ticket> getTicketsByUser(int userId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TICKETS_BY_USER)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(createTicketFromResultSet(rs));
                }
            }
        }
        
        return tickets;
    }
    
    public static List<Ticket> getTicketsByFlight(int flightId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TICKETS_BY_FLIGHT)) {
            
            stmt.setInt(1, flightId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(createTicketFromResultSet(rs));
                }
            }
        }
        
        return tickets;
    }
    
    public static void checkInTicket(int ticketId) throws SQLException {
        // Only allow check-in if payment is completed
        updateTicketStatus(ticketId, "CHECKED_IN");
    }
    
    public static void cancelTicket(int ticketId) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Get ticket details first
            Ticket ticket = getTicketById(ticketId);
            if (ticket == null) {
                throw new SQLException("Ticket not found with ID: " + ticketId);
            }
            
            // Update ticket status
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE tickets SET status = 'CANCELLED' WHERE ticket_id = ?")) {
                stmt.setInt(1, ticketId);
                stmt.executeUpdate();
            }
            
            // Increment available seats
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE flights SET available_seats = available_seats + 1 WHERE flight_id = ?")) {
                stmt.setInt(1, ticket.getFlightId());
                stmt.executeUpdate();
            }
            
            // Create refund payment record
            Payment payment = new Payment(ticketId, ticket.getPrice(), "REFUND");
            payment.setTransactionId("REF" + System.currentTimeMillis());
            payment.setPaymentStatus("COMPLETED");
            payment.setPaymentDate(LocalDateTime.now());
            
            try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO payments (ticket_id, amount, payment_method, payment_status, transaction_id, payment_date) VALUES (?, ?, ?, ?, ?, ?)"
            )) {
                stmt.setInt(1, payment.getTicketId());
                stmt.setDouble(2, payment.getAmount());
                stmt.setString(3, payment.getPaymentMethod());
                stmt.setString(4, payment.getPaymentStatus());
                stmt.setString(5, payment.getTransactionId());
                stmt.setTimestamp(6, Timestamp.valueOf(payment.getPaymentDate()));
                stmt.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Error rolling back transaction: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    public static void updateTicketStatus(int ticketId, String status) throws SQLException {
        String sql = "UPDATE tickets SET status = ? WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
        }
    }
    
    private static void setTicketParameters(PreparedStatement stmt, Ticket ticket) throws SQLException {
        stmt.setInt(1, ticket.getUserId());
        stmt.setInt(2, ticket.getFlightId());
        stmt.setString(3, ticket.getFullName());
        stmt.setString(4, ticket.getFlightNumber());
        stmt.setString(5, ticket.getSeatNumber());
        stmt.setString(6, ticket.getStatus());
        stmt.setString(7, ticket.getPaymentStatus());
        stmt.setDouble(8, ticket.getPrice());
        stmt.setTimestamp(9, Timestamp.valueOf(ticket.getBookingDateTime()));
    }
    
    private static Ticket createTicketFromResultSet(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setTicketId(rs.getInt("ticket_id"));
        ticket.setUserId(rs.getInt("user_id"));
        ticket.setFlightId(rs.getInt("flight_id"));
        ticket.setFullName(rs.getString("full_name"));
        ticket.setFlightNumber(rs.getString("flight_number"));
        ticket.setSeatNumber(rs.getString("seat_number"));
        ticket.setStatus(rs.getString("status"));
        ticket.setPaymentStatus(rs.getString("payment_status"));
        ticket.setPrice(rs.getDouble("price"));
        ticket.setBookingDateTime(rs.getTimestamp("booking_datetime").toLocalDateTime());
        
        // Add payment information if available
        String paymentMethod = rs.getString("payment_method");
        if (paymentMethod != null) {
            ticket.setPaymentMethod(paymentMethod);
            String transactionId = rs.getString("transaction_id");
            ticket.setTransactionId(transactionId != null ? transactionId : "TXN" + System.currentTimeMillis());
            Timestamp paymentDate = rs.getTimestamp("payment_date");
            if (paymentDate != null) {
                ticket.setPaymentDate(paymentDate.toLocalDateTime());
            }
        }
        
        return ticket;
    }
} 