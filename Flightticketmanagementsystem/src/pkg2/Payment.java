package pkg2;

import java.time.LocalDateTime;

public class Payment {
    private int paymentId;
    private int ticketId;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String remarks;

    // Default constructor
    public Payment() {
    }

    // Constructor with essential fields
    public Payment(int ticketId, double amount, String paymentMethod) {
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "PENDING";
        this.transactionId = "TXN" + System.currentTimeMillis();
        this.paymentDate = LocalDateTime.now();
    }

    // Full constructor
    public Payment(int paymentId, int ticketId, double amount, String paymentMethod, 
                  String paymentStatus, String transactionId, LocalDateTime paymentDate, String remarks) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
        this.remarks = remarks;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
} 