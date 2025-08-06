package pkg2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class FlightDAO {
    private static final String INSERT_FLIGHT = "INSERT INTO flights (flight_number, origin, destination, departure_time, arrival_time, price, capacity, available_seats, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_FLIGHT = "UPDATE flights SET flight_number = ?, origin = ?, destination = ?, departure_time = ?, arrival_time = ?, price = ?, capacity = ?, available_seats = ?, status = ? WHERE flight_id = ?";
    private static final String DELETE_FLIGHT = "DELETE FROM flights WHERE flight_id = ?";
    private static final String SELECT_ALL_FLIGHTS = "SELECT * FROM flights ORDER BY departure_time";
    private static final String SELECT_FLIGHT_BY_ID = "SELECT * FROM flights WHERE flight_id = ?";
    private static final String UPDATE_FLIGHT_STATUS = "UPDATE flights SET status = ? WHERE flight_id = ?";
    private static final String SELECT_FLIGHT_BY_NUMBER = "SELECT * FROM flights WHERE flight_number = ?";
    private static final String UPDATE_AVAILABLE_SEATS = "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_id = ? AND available_seats > 0";
    
    private static Flight createFlightFromResultSet(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new SQLException("ResultSet cannot be null");
        }

        Flight flight = new Flight();
        try {
            flight.setLoadingFromDatabase(true);
            
            // Extract all values first to avoid potential SQL errors
            int flightId = rs.getInt("flight_id");
            String flightNumber = rs.getString("flight_number");
            String origin = rs.getString("origin");
            String destination = rs.getString("destination");
            Timestamp depTs = rs.getTimestamp("departure_time");
            Timestamp arrTs = rs.getTimestamp("arrival_time");
            double price = rs.getDouble("price");
            int capacity = rs.getInt("capacity");
            int availableSeats = rs.getInt("available_seats");
            String status = rs.getString("status");

            // Now set all the values
            flight.setFlightId(flightId);
            flight.setFlightNumber(flightNumber);
            flight.setOrigin(origin);
            flight.setDestination(destination);
            flight.setDepartureTime(depTs != null ? depTs.toLocalDateTime() : null);
            flight.setArrivalTime(arrTs != null ? arrTs.toLocalDateTime() : null);
            flight.setPrice(price);
            flight.setCapacity(capacity);
            flight.setAvailableSeats(availableSeats);
            flight.setStatus(status);
            
            return flight;
        } catch (SQLException e) {
            System.out.println("Error reading flight data: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Error creating flight object: " + e.getMessage());
            throw new SQLException("Failed to create flight object: " + e.getMessage(), e);
        } finally {
            if (flight != null) {
                flight.setLoadingFromDatabase(false);
            }
        }
    }
    
    public static List<Flight> getAllFlights() throws SQLException {
        List<Flight> flights = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_FLIGHTS)) {
            
            while (rs.next()) {
                try {
                    Flight flight = createFlightFromResultSet(rs);
                    flights.add(flight);
                } catch (SQLException e) {
                    System.out.println("Error processing flight: " + e.getMessage());
                    // Continue processing other flights
                }
            }
        }
        
        return flights;
    }
    
    public static void addFlight(Flight flight) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_FLIGHT, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, flight.getFlightNumber());
            stmt.setString(2, flight.getOrigin());
            stmt.setString(3, flight.getDestination());
            stmt.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
            stmt.setTimestamp(5, Timestamp.valueOf(flight.getArrivalTime()));
            stmt.setDouble(6, flight.getPrice());
            stmt.setInt(7, flight.getCapacity());
            stmt.setInt(8, flight.getAvailableSeats());
            stmt.setString(9, flight.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating flight failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    flight.setFlightId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating flight failed, no ID obtained.");
                }
            }
        }
    }
    
    public static void updateFlight(Flight flight) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_FLIGHT)) {
            
            stmt.setString(1, flight.getFlightNumber());
            stmt.setString(2, flight.getOrigin());
            stmt.setString(3, flight.getDestination());
            stmt.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
            stmt.setTimestamp(5, Timestamp.valueOf(flight.getArrivalTime()));
            stmt.setDouble(6, flight.getPrice());
            stmt.setInt(7, flight.getCapacity());
            stmt.setInt(8, flight.getAvailableSeats());
            stmt.setString(9, flight.getStatus());
            stmt.setInt(10, flight.getFlightId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating flight failed, no rows affected.");
            }
        }
    }
    
    public static void deleteFlight(int flightId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_FLIGHT)) {
            
            stmt.setInt(1, flightId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting flight failed, no rows affected.");
            }
        }
    }
    
    public static Flight getFlightById(int flightId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_FLIGHT_BY_ID)) {
            
            stmt.setInt(1, flightId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createFlightFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public static List<Flight> searchFlights(String origin, String destination, LocalDateTime departureDate) throws SQLException {
        List<Flight> flights = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM flights WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (origin != null && !origin.trim().isEmpty()) {
            queryBuilder.append(" AND UPPER(origin) = UPPER(?)");
            params.add(origin.trim());
        }
        
        if (destination != null && !destination.trim().isEmpty()) {
            queryBuilder.append(" AND UPPER(destination) = UPPER(?)");
            params.add(destination.trim());
        }
        
        if (departureDate != null) {
            queryBuilder.append(" AND DATE(departure_time) = DATE(?)");
            params.add(departureDate);
        }
        
        queryBuilder.append(" ORDER BY departure_time");
        
        System.out.println("Executing query: " + queryBuilder.toString());
        System.out.println("With parameters: " + params);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof LocalDateTime) {
                    stmt.setTimestamp(i + 1, Timestamp.valueOf((LocalDateTime) params.get(i)));
                } else {
                    stmt.setString(i + 1, (String) params.get(i));
                }
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    try {
                        Flight flight = createFlightFromResultSet(rs);
                        flights.add(flight);
                        
                        System.out.println("Loaded flight: " + flight.getFlightNumber() + 
                                         " departure: " + flight.getDepartureTime() +
                                         " status: " + flight.getStatus());
                    } catch (SQLException e) {
                        System.out.println("Error processing flight row: " + e.getMessage());
                        // Continue processing other flights
                    }
                }
            }
        }
        
        System.out.println("Found " + flights.size() + " flights");
        return flights;
    }
    
    public static void updateFlightStatus(int flightId, String status) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_FLIGHT_STATUS)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, flightId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating flight status failed, no rows affected.");
            }
        }
    }
    
    public static Flight getFlightByNumber(String flightNumber) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_FLIGHT_BY_NUMBER)) {
            
            stmt.setString(1, flightNumber);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createFlightFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public static void decrementAvailableSeats(int flightId) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // First check if flight exists and has available seats
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT available_seats FROM flights WHERE flight_id = ?")) {
                checkStmt.setInt(1, flightId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next()) {
                    throw new SQLException("Flight not found with ID: " + flightId);
                }
                
                int availableSeats = rs.getInt("available_seats");
                if (availableSeats <= 0) {
                    throw new SQLException("No available seats for flight ID: " + flightId);
                }
            }
            
            // Update available seats
            try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE_AVAILABLE_SEATS)) {
                updateStmt.setInt(1, flightId);
                int affectedRows = updateStmt.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Failed to update available seats for flight ID: " + flightId);
                }
            }
            
            conn.commit(); // Commit transaction
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    System.out.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            throw new SQLException("Error updating available seats: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    public static void incrementAvailableSeats(int flightId) throws SQLException {
        String sql = "UPDATE flights SET available_seats = available_seats + 1 WHERE flight_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, flightId);
            pstmt.executeUpdate();
        }
    }
} 