package pkg1;

import pkg2.Flight;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FlightDialog extends Dialog<Flight> {
    private TextField flightNumberField;
    private TextField originField;
    private TextField destinationField;
    private DatePicker departureDatePicker;
    private TextField departureTimeField;
    private DatePicker arrivalDatePicker;
    private TextField arrivalTimeField;
    private TextField priceField;
    private TextField capacityField;
    
    public FlightDialog(Flight flight) {
        setTitle(flight == null ? "Add New Flight" : "Edit Flight");
        setHeaderText(null);
        
        // Create the grid pane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Initialize fields
        flightNumberField = new TextField();
        originField = new TextField();
        destinationField = new TextField();
        departureDatePicker = new DatePicker();
        departureTimeField = new TextField();
        arrivalDatePicker = new DatePicker();
        arrivalTimeField = new TextField();
        priceField = new TextField();
        capacityField = new TextField();
        
        // Add fields to grid
        grid.add(new Label("Flight Number:"), 0, 0);
        grid.add(flightNumberField, 1, 0);
        grid.add(new Label("Origin:"), 0, 1);
        grid.add(originField, 1, 1);
        grid.add(new Label("Destination:"), 0, 2);
        grid.add(destinationField, 1, 2);
        grid.add(new Label("Departure Date:"), 0, 3);
        grid.add(departureDatePicker, 1, 3);
        grid.add(new Label("Departure Time (HH:mm):"), 0, 4);
        grid.add(departureTimeField, 1, 4);
        grid.add(new Label("Arrival Date:"), 0, 5);
        grid.add(arrivalDatePicker, 1, 5);
        grid.add(new Label("Arrival Time (HH:mm):"), 0, 6);
        grid.add(arrivalTimeField, 1, 6);
        grid.add(new Label("Price:"), 0, 7);
        grid.add(priceField, 1, 7);
        grid.add(new Label("Capacity:"), 0, 8);
        grid.add(capacityField, 1, 8);
        
        // Set existing values if editing
        if (flight != null) {
            flightNumberField.setText(flight.getFlightNumber());
            originField.setText(flight.getOrigin());
            destinationField.setText(flight.getDestination());
            departureDatePicker.setValue(flight.getDepartureTime().toLocalDate());
            departureTimeField.setText(String.format("%02d:%02d", 
                flight.getDepartureTime().getHour(), 
                flight.getDepartureTime().getMinute()));
            arrivalDatePicker.setValue(flight.getArrivalTime().toLocalDate());
            arrivalTimeField.setText(String.format("%02d:%02d", 
                flight.getArrivalTime().getHour(), 
                flight.getArrivalTime().getMinute()));
            priceField.setText(String.valueOf(flight.getPrice()));
            capacityField.setText(String.valueOf(flight.getCapacity()));
        }
        
        getDialogPane().setContent(grid);
        
        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Convert the result
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    // Validate and parse time fields
                    LocalTime departureTime = LocalTime.parse(departureTimeField.getText());
                    LocalTime arrivalTime = LocalTime.parse(arrivalTimeField.getText());
                    
                    // Combine date and time
                    LocalDateTime departureDateTime = LocalDateTime.of(
                        departureDatePicker.getValue(), departureTime);
                    LocalDateTime arrivalDateTime = LocalDateTime.of(
                        arrivalDatePicker.getValue(), arrivalTime);
                    
                    // Create and return flight object
                    Flight newFlight = new Flight();
                    if (flight != null) {
                        newFlight.setFlightId(flight.getFlightId());
                    }
                    newFlight.setFlightNumber(flightNumberField.getText());
                    newFlight.setOrigin(originField.getText());
                    newFlight.setDestination(destinationField.getText());
                    newFlight.setDepartureTime(departureDateTime);
                    newFlight.setArrivalTime(arrivalDateTime);
                    newFlight.setPrice(Double.parseDouble(priceField.getText()));
                    newFlight.setCapacity(Integer.parseInt(capacityField.getText()));
                    newFlight.setStatus(flight != null ? flight.getStatus() : "SCHEDULED");
                    
                    return newFlight;
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Please check your input values:\n" + e.getMessage());
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });
    }
} 