package pkg2;

import java.time.LocalDateTime;

public class Ticket {
    private int ticketId;
    private int userId;
    private int flightId;
    private String fullName;
    private String flightNumber;
    private String seatNumber;
    private String status;
    private String paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private LocalDateTime paymentDate;
    private double price;
    private LocalDateTime bookingDateTime;
    private LocalDateTime createdAt;
    
    public Ticket() {
        this.status = "PENDING";
        this.paymentStatus = "PENDING";
        this.bookingDateTime = LocalDateTime.now();
    }
    
    // Getters and setters
    public int getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(int ticketId) {
        if (ticketId < 0) throw new IllegalArgumentException("Ticket ID cannot be negative");
        this.ticketId = ticketId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        if (userId < 0) throw new IllegalArgumentException("User ID cannot be negative");
        this.userId = userId;
    }
    
    public int getFlightId() {
        return flightId;
    }
    
    public void setFlightId(int flightId) {
        if (flightId < 0) throw new IllegalArgumentException("Flight ID cannot be negative");
        this.flightId = flightId;
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
    
    public String getFlightNumber() {
        return flightNumber;
    }
    
    public void setFlightNumber(String flightNumber) {
        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be empty");
        }
        this.flightNumber = flightNumber.trim().toUpperCase();
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be empty");
        }
        this.seatNumber = seatNumber.trim().toUpperCase();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        this.status = status.trim().toUpperCase();
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }
    
    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }
    
    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        if (bookingDateTime == null) throw new IllegalArgumentException("Booking time cannot be null");
        this.bookingDateTime = bookingDateTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // New getters and setters for payment-related fields
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        if (paymentStatus == null || paymentStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment status cannot be empty");
        }
        this.paymentStatus = paymentStatus.trim().toUpperCase();
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be empty");
        }
        this.paymentMethod = paymentMethod.trim().toUpperCase();
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be empty");
        }
        this.transactionId = transactionId.trim().toUpperCase();
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        if (paymentDate == null) throw new IllegalArgumentException("Payment date cannot be null");
        this.paymentDate = paymentDate;
    }
    
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", userId=" + userId +
                ", flightId=" + flightId +
                ", fullName='" + fullName + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", bookingDateTime=" + bookingDateTime +
                '}';
    }
} 