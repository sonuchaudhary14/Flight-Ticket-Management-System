package pkg2;

import java.time.LocalDateTime;

public class Flight {
    private int flightId;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;
    private int capacity;
    private int availableSeats;
    private String status;
    
    private boolean isLoadingFromDatabase = false;
    
    public Flight() {
        this.status = "SCHEDULED"; 
    }
    
    public void setLoadingFromDatabase(boolean loading) {
        this.isLoadingFromDatabase = loading;
    }
    
    public boolean isLoadingFromDatabase() {
        return isLoadingFromDatabase;
    }
    
    // Getters and setters
    public int getFlightId() {
        return flightId;
    }
    
    public void setFlightId(int flightId) {
        if (flightId < 0) throw new IllegalArgumentException("Flight ID cannot be negative");
        this.flightId = flightId;
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
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        if (origin == null || origin.trim().isEmpty()) {
            throw new IllegalArgumentException("Origin cannot be empty");
        }
        this.origin = origin.trim().toUpperCase();
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be empty");
        }
        if (destination.trim().equalsIgnoreCase(origin)) {
            throw new IllegalArgumentException("Destination cannot be same as origin");
        }
        this.destination = destination.trim().toUpperCase();
    }
    
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(LocalDateTime departureTime) {
        if (departureTime == null) {
            throw new IllegalArgumentException("Departure time cannot be null");
        }
        if (!isLoadingFromDatabase && departureTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Departure time cannot be in the past");
        }
        this.departureTime = departureTime;
    }
    
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(LocalDateTime arrivalTime) {
        if (arrivalTime == null) {
            throw new IllegalArgumentException("Arrival time cannot be null");
        }
        if (departureTime != null && arrivalTime.isBefore(departureTime)) {
            throw new IllegalArgumentException("Arrival time cannot be before departure time");
        }
        this.arrivalTime = arrivalTime;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.capacity = capacity;
    }
    
    public int getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(int availableSeats) {
        if (availableSeats < 0) {
            throw new IllegalArgumentException("Available seats cannot be negative");
        }
        if (availableSeats > capacity) {
            throw new IllegalArgumentException("Available seats cannot exceed capacity");
        }
        this.availableSeats = availableSeats;
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
    
    @Override
    public String toString() {
        return "Flight{" +
                "flightId=" + flightId +
                ", flightNumber='" + flightNumber + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", price=" + price +
                ", capacity=" + capacity +
                ", availableSeats=" + availableSeats +
                ", status='" + status + '\'' +
                '}';
    }
} 