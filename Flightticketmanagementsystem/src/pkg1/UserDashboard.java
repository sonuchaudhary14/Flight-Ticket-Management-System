package pkg1;

import pkg2.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.sql.SQLException;
import javafx.scene.effect.DropShadow;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDashboard extends Application {
    private User currentUser;
    private TableView<Flight> flightTable;
    private TableView<Ticket> bookingTable;
    
    public UserDashboard(User user) {
        this.currentUser = user;
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flight Management System - User Dashboard");
        
        // Create main container with modern design
        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        
        // Create header
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #4361ee; -fx-padding: 20;");
        header.setPrefHeight(80);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label welcomeLabel = new Label("Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.WHITE);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button logoutBtn = createStyledButton("Logout", "logout");
        logoutBtn.setOnAction(e -> {
            try {
                Stage mainStage = new Stage();
                MainPage mainPage = new MainPage();
                mainPage.start(mainStage);
                primaryStage.close();
            } catch (Exception ex) {
                showError("Error during logout: " + ex.getMessage());
            }
        });
        
        header.getChildren().addAll(welcomeLabel, spacer, logoutBtn);
        
        // Create tabs with modern styling
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab searchTab = createSearchTab();
        Tab bookingsTab = createBookingsTab();
        Tab profileTab = createProfileTab();
        
        tabPane.getTabs().addAll(searchTab, bookingsTab, profileTab);
        
        // Style the tabs
        tabPane.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-tab-min-height: 50px; " +
            "-fx-tab-max-height: 50px;"
        );
        
        // Add components to main container
        mainContainer.getChildren().addAll(header, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        
        // Set up the scene
        Scene scene = new Scene(mainContainer, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Load initial data
        loadMyBookings();
    }
    
    private Tab createSearchTab() {
        Tab searchTab = new Tab("Search Flights");
        VBox searchBox = createStyledBox();
        
        // Search form
        GridPane searchForm = new GridPane();
        searchForm.setHgap(10);
        searchForm.setVgap(10);
        searchForm.setAlignment(Pos.CENTER);
        
        TextField originField = new TextField();
        originField.setPromptText("From");
        TextField destinationField = new TextField();
        destinationField.setPromptText("To");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Departure Date");
        
        Button searchBtn = createStyledButton("Search", "search");
        searchBtn.setOnAction(e -> handleSearch(
            originField.getText().trim(),
            destinationField.getText().trim(),
            datePicker.getValue()
        ));
        
        searchForm.add(new Label("From:"), 0, 0);
        searchForm.add(originField, 1, 0);
        searchForm.add(new Label("To:"), 2, 0);
        searchForm.add(destinationField, 3, 0);
        searchForm.add(new Label("Date:"), 4, 0);
        searchForm.add(datePicker, 5, 0);
        searchForm.add(searchBtn, 6, 0);
        
        // Flight table
        flightTable = new TableView<>();
        setupFlightTable();
        
        Button bookBtn = createStyledButton("Book Selected Flight", "book");
        bookBtn.setOnAction(e -> handleBookFlight());
        
        searchBox.getChildren().addAll(
            createSectionTitle("Search Flights"),
            searchForm,
            flightTable,
            bookBtn
        );
        
        searchTab.setContent(searchBox);
        return searchTab;
    }
    
    private Tab createBookingsTab() {
        Tab bookingsTab = new Tab("My Bookings");
        
        // Create table
        bookingTable = new TableView<>();
        
        setupBookingTable();
        
        // Create buttons
        Button viewButton = createStyledButton("View Details", "view");
        Button cancelButton = createStyledButton("Cancel Booking", "cancel");
        
        viewButton.setOnAction(e -> handleViewTicket());
        cancelButton.setOnAction(e -> handleCancelBooking());
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(viewButton, cancelButton);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(bookingTable, buttonBox);
        
        bookingsTab.setContent(content);
        
        // Load bookings
        loadMyBookings();
        
        return bookingsTab;
    }
    
    private Tab createProfileTab() {
        Tab profileTab = new Tab("Profile");
        VBox profileBox = createStyledBox();
        
        GridPane profileForm = new GridPane();
        profileForm.setHgap(15);
        profileForm.setVgap(10);
        profileForm.setAlignment(Pos.CENTER);
        
        TextField emailField = new TextField(currentUser.getEmail());
        TextField fullNameField = new TextField(currentUser.getFullName());
        TextField phoneField = new TextField(currentUser.getPhone());
        TextField addressField = new TextField(currentUser.getAddress());
        
        emailField.setEditable(false);
        
        profileForm.add(new Label("Email:"), 0, 0);
        profileForm.add(emailField, 1, 0);
        profileForm.add(new Label("Full Name:"), 0, 1);
        profileForm.add(fullNameField, 1, 1);
        profileForm.add(new Label("Phone:"), 0, 2);
        profileForm.add(phoneField, 1, 2);
        profileForm.add(new Label("Address:"), 0, 3);
        profileForm.add(addressField, 1, 3);
        
        Button updateProfileBtn = createStyledButton("Update Profile", "edit");
        updateProfileBtn.setOnAction(e -> handleUpdateProfile(
            fullNameField.getText(),
            phoneField.getText(),
            addressField.getText()
        ));
        
        profileBox.getChildren().addAll(
            createSectionTitle("Personal Information"),
            profileForm,
            updateProfileBtn
        );
        
        profileTab.setContent(profileBox);
        return profileTab;
    }
    
    private void setupFlightTable() {
        // Set minimum height for the table
        flightTable.setMinHeight(300);
        
        TableColumn<Flight, String> flightNumberCol = new TableColumn<>("Flight #");
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        flightNumberCol.setPrefWidth(100);
        
        TableColumn<Flight, String> originCol = new TableColumn<>("From");
        originCol.setCellValueFactory(new PropertyValueFactory<>("origin"));
        originCol.setPrefWidth(120);
        
        TableColumn<Flight, String> destinationCol = new TableColumn<>("To");
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        destinationCol.setPrefWidth(120);
        
        TableColumn<Flight, LocalDateTime> departureCol = new TableColumn<>("Departure");
        departureCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        departureCol.setPrefWidth(150);
        departureCol.setCellFactory(column -> new TableCell<Flight, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
            
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    try {
                        // Format the date and add day of week
                        String formattedDate = formatter.format(item);
                        String dayOfWeek = item.getDayOfWeek().toString().substring(0, 3);
                        setText(dayOfWeek + ", " + formattedDate);
                        
                        // Style based on whether flight is in past or future
                        LocalDateTime now = LocalDateTime.now();
                        if (item.isBefore(now)) {
                            setStyle("-fx-text-fill: #666666;"); // Past flights in gray
                        } else if (item.isBefore(now.plusHours(24))) {
                            setStyle("-fx-text-fill: #e67e22;"); // Flights within 24 hours in orange
                        } else {
                            setStyle("-fx-text-fill: #2ecc71;"); // Future flights in green
                        }
                    } catch (Exception e) {
                        System.out.println("Error formatting departure date: " + e.getMessage());
                        e.printStackTrace();
                        setText("Invalid Date");
                        setStyle("-fx-text-fill: red;");
                    }
                }
            }
        });
        
        TableColumn<Flight, LocalDateTime> arrivalCol = new TableColumn<>("Arrival");
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        arrivalCol.setPrefWidth(150);
        arrivalCol.setCellFactory(column -> new TableCell<Flight, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
            
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    try {
                        // Format the date and add day of week
                        String formattedDate = formatter.format(item);
                        String dayOfWeek = item.getDayOfWeek().toString().substring(0, 3);
                        setText(dayOfWeek + ", " + formattedDate);
                    } catch (Exception e) {
                        System.out.println("Error formatting arrival date: " + e.getMessage());
                        e.printStackTrace();
                        setText("Invalid Date");
                    }
                }
            }
        });
        
        TableColumn<Flight, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);
        priceCol.setCellFactory(column -> new TableCell<Flight, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });
        
        TableColumn<Flight, Integer> availableSeatsCol = new TableColumn<>("Available Seats");
        availableSeatsCol.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        availableSeatsCol.setPrefWidth(120);
        availableSeatsCol.setCellFactory(column -> new TableCell<Flight, Integer>() {
            @Override
            protected void updateItem(Integer seats, boolean empty) {
                super.updateItem(seats, empty);
                if (empty || seats == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(seats));
                    if (seats == 0) {
                        setStyle("-fx-text-fill: red;");
                    } else if (seats < 10) {
                        setStyle("-fx-text-fill: orange;");
                    } else {
                        setStyle("-fx-text-fill: green;");
                    }
                }
            }
        });
        
        TableColumn<Flight, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);
        statusCol.setCellFactory(column -> new TableCell<Flight, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                } else {
                    setText(status);
                    switch (status.toUpperCase()) {
                        case "SCHEDULED":
                            setStyle("-fx-text-fill: green;");
                            break;
                        case "DELAYED":
                            setStyle("-fx-text-fill: orange;");
                            break;
                        case "CANCELLED":
                            setStyle("-fx-text-fill: red;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        
        // Clear existing columns if any
        flightTable.getColumns().clear();
        
        // Add all columns
        flightTable.getColumns().addAll(
            flightNumberCol, originCol, destinationCol, departureCol, arrivalCol, priceCol, availableSeatsCol, statusCol
        );
        
        // Set selection mode
        flightTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Set resize policy
        flightTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Add table style
        flightTable.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        // Load initial data
        try {
            List<Flight> flights = FlightDAO.getAllFlights();
            flightTable.getItems().clear();
            flightTable.getItems().addAll(flights);
            System.out.println("Loaded " + flights.size() + " flights initially");
        } catch (SQLException e) {
            System.out.println("Error loading initial flight data: " + e.getMessage());
            e.printStackTrace();
            showError("Error loading flights: " + e.getMessage());
        }
    }
    
    private void setupBookingTable() {
        // Create table columns
        TableColumn<Ticket, Integer> ticketIdCol = new TableColumn<>("Ticket ID");
        ticketIdCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        
        TableColumn<Ticket, String> flightNumberCol = new TableColumn<>("Flight");
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        
        TableColumn<Ticket, String> seatCol = new TableColumn<>("Seat");
        seatCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        
        TableColumn<Ticket, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(column -> new TableCell<Ticket, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%.2f", price));
            }
        });
        
        TableColumn<Ticket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(column -> new TableCell<Ticket, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "BOOKED":
                            setStyle("-fx-text-fill: #2ecc71;"); // Green
                            break;
                        case "CANCELLED":
                            setStyle("-fx-text-fill: #e74c3c;"); // Red
                            break;
                        case "CHECKED_IN":
                            setStyle("-fx-text-fill: #3498db;"); // Blue
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });
        
        TableColumn<Ticket, String> paymentStatusCol = new TableColumn<>("Payment");
        paymentStatusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        paymentStatusCol.setCellFactory(column -> new TableCell<Ticket, String>() {
            @Override
            protected void updateItem(String paymentStatus, boolean empty) {
                super.updateItem(paymentStatus, empty);
                if (empty || paymentStatus == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(paymentStatus);
                    if ("COMPLETED".equals(paymentStatus)) {
                        setStyle("-fx-text-fill: #2ecc71;"); // Green
                    } else if ("PENDING".equals(paymentStatus)) {
                        setStyle("-fx-text-fill: #f39c12;"); // Orange
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        // Add columns to table
        bookingTable.getColumns().addAll(
            ticketIdCol, flightNumberCol, seatCol,
            priceCol, statusCol, paymentStatusCol
        );
        
        // Set table style
        bookingTable.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-table-cell-border-color: transparent;");
        
        // Set fixed cell size for compact rows
        bookingTable.setFixedCellSize(30);
        
        // Remove header border
        bookingTable.getStylesheets().add("data:text/css," + 
            ".table-view .column-header-background { -fx-background-color: white; }" +
            ".table-view .column-header { -fx-background-color: white; -fx-border-width: 0 0 1 0; -fx-border-color: #ddd; }" +
            ".table-view .table-row-cell { -fx-border-width: 0; }" +
            ".table-view .table-cell { -fx-border-width: 0; -fx-padding: 5 10; }" +
            ".table-view .table-row-cell:empty { -fx-background-color: white; }" +
            ".table-view .table-row-cell:filled { -fx-background-color: white; }" +
            ".table-view .table-row-cell:filled:selected { -fx-background-color: #f8f9fa; }"
        );
        
        // Set column resize policy
        bookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Add row styling
        bookingTable.setRowFactory(tv -> new TableRow<Ticket>() {
            @Override
            protected void updateItem(Ticket ticket, boolean empty) {
                super.updateItem(ticket, empty);
                if (empty || ticket == null) {
                    setStyle("-fx-background-color: white;");
                } else {
                    switch (ticket.getStatus()) {
                        case "CHECKED_IN":
                            setStyle("-fx-background-color: #e8f5e9;"); // Light green
                            break;
                        case "CANCELLED":
                            setStyle("-fx-background-color: #ffebee;"); // Light red
                            break;
                        default:
                            setStyle("-fx-background-color: white;");
                            break;
                    }
                }
            }
        });
    }
    
    private void handleBookFlight() {
        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            showError("Please select a flight to book.");
            return;
        }

        // Create payment dialog
        Dialog<Payment> paymentDialog = new Dialog<>();
        paymentDialog.setTitle("Payment Details");
        paymentDialog.setHeaderText("Complete your booking payment");

        // Create dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Payment method selection
        ComboBox<String> paymentMethod = new ComboBox<>();
        paymentMethod.getItems().addAll("eSewa", "Cash in Hand");
        paymentMethod.setPromptText("Select Payment Method");
        grid.add(new Label("Payment Method:"), 0, 0);
        grid.add(paymentMethod, 1, 0);

        // Amount field (read-only)
        TextField amountField = new TextField();
        amountField.setText(String.format("%.2f", selectedFlight.getPrice()));
        amountField.setEditable(false);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);

        // Remarks field
        TextArea remarksField = new TextArea();
        remarksField.setPrefRowCount(3);
        remarksField.setPromptText("Enter any remarks (optional)");
        grid.add(new Label("Remarks:"), 0, 2);
        grid.add(remarksField, 1, 2);

        paymentDialog.getDialogPane().setContent(grid);

        // Add buttons
        ButtonType payButtonType = new ButtonType("Pay Now", ButtonBar.ButtonData.OK_DONE);
        paymentDialog.getDialogPane().getButtonTypes().addAll(payButtonType, ButtonType.CANCEL);

        // Convert the result to a Payment object
        paymentDialog.setResultConverter(dialogButton -> {
            if (dialogButton == payButtonType) {
                if (paymentMethod.getValue() == null) {
                    showError("Please select a payment method.");
                    return null;
                }

                // Show confirmation dialog
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirm Payment");
                confirmDialog.setHeaderText("Payment Confirmation");
                confirmDialog.setContentText(String.format(
                    "Are you sure you want to proceed with the payment?\n\n" +
                    "Amount: %.2f\n" +
                    "Payment Method: %s\n" +
                    "Remarks: %s",
                    selectedFlight.getPrice(),
                    paymentMethod.getValue(),
                    remarksField.getText()
                ));

                if (confirmDialog.showAndWait().get() == ButtonType.OK) {
                    Payment payment = new Payment(0, selectedFlight.getPrice(), paymentMethod.getValue());
                    payment.setTransactionId("TXN" + System.currentTimeMillis());
                    payment.setPaymentStatus("COMPLETED");
                    payment.setPaymentDate(LocalDateTime.now());
                    return payment;
                }
            }
            return null;
        });

        // Show the dialog and handle the result
        Optional<Payment> result = paymentDialog.showAndWait();
        if (result.isPresent()) {
            try {
                // Create ticket
                Ticket ticket = new Ticket();
                ticket.setUserId(currentUser.getUserId());
                ticket.setFlightId(selectedFlight.getFlightId());
                ticket.setFullName(currentUser.getFullName());
                ticket.setFlightNumber(selectedFlight.getFlightNumber());
                ticket.setSeatNumber(generateSeatNumber());
                ticket.setStatus("BOOKED");
                ticket.setPaymentStatus("COMPLETED");
                ticket.setPrice(selectedFlight.getPrice());
                ticket.setBookingDateTime(LocalDateTime.now());
                ticket.setPaymentMethod(paymentMethod.getValue());

                // Add ticket to database
                TicketDAO.addTicket(ticket);

                // Process payment
                Payment payment = result.get();
                payment.setTicketId(ticket.getTicketId());
                PaymentDAO.addPayment(payment);

                // Update available seats
                FlightDAO.decrementAvailableSeats(selectedFlight.getFlightId());

                showSuccess("Flight booked successfully! Your ticket number is: " + ticket.getTicketId());
                loadMyBookings();
            } catch (SQLException e) {
                showError("Error booking flight: " + e.getMessage());
            }
        }
    }
    
    private String generateSeatNumber() {
        return String.format("%d%c", (int)(Math.random() * 30) + 1, 
                                   (char)('A' + (int)(Math.random() * 6)));
    }
    
    private void handleViewTicket() {
        Ticket selectedTicket = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedTicket == null) {
            showError("Please select a booking to view");
            return;
        }
        
        try {
            Flight flight = FlightDAO.getFlightById(selectedTicket.getFlightId());
            Payment payment = PaymentDAO.getPaymentByTicket(selectedTicket.getTicketId());
            
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Booking Details");
            dialog.setHeaderText("Ticket #" + selectedTicket.getTicketId());
            
            VBox content = new VBox(10);
            content.setPadding(new Insets(20));
            
            // Add booking details
            content.getChildren().addAll(
                new Label("Flight Number: " + selectedTicket.getFlightNumber()),
                new Label("From: " + flight.getOrigin()),
                new Label("To: " + flight.getDestination()),
                new Label("Departure: " + flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))),
                new Label("Arrival: " + flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))),
                new Label("Seat Number: " + selectedTicket.getSeatNumber()),
                new Label("Price: $" + String.format("%.2f", selectedTicket.getPrice())),
                new Label("Status: " + selectedTicket.getStatus()),
                new Label("Payment Status: " + selectedTicket.getPaymentStatus())
            );
            
            // Add payment details if available
            if (payment != null) {
                content.getChildren().addAll(
                    new Label("Payment Method: " + payment.getPaymentMethod()),
                    new Label("Transaction ID: " + payment.getTransactionId()),
                    new Label("Payment Date: " + (payment.getPaymentDate() != null ? 
                        payment.getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "Not paid"))
                );
            }
            
            content.getChildren().add(
                new Label("Booking Time: " + selectedTicket.getBookingDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
            );
            
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            
            dialog.showAndWait();
        } catch (SQLException e) {
            showError("Error loading ticket details: " + e.getMessage());
        }
    }
    
    private void handleCancelBooking() {
        Ticket selectedTicket = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedTicket == null) {
            showError("Please select a booking to cancel");
            return;
        }

        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Booking");
        alert.setHeaderText("Cancel Booking Confirmation");
        alert.setContentText("Are you sure you want to cancel this booking?\nTicket #" + selectedTicket.getTicketId());

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                TicketDAO.cancelTicket(selectedTicket.getTicketId());
                showSuccess("Booking cancelled successfully!");
                loadMyBookings(); // Refresh the bookings table
            } catch (SQLException e) {
                showError("Error cancelling booking: " + e.getMessage());
            }
        }
    }
    
    private void handleUpdateProfile(String fullName, String phone, String address) {
        try {
            currentUser.setFullName(fullName);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);
            
            UserDAO.updateUser(currentUser);
            showSuccess("Profile updated successfully!");
        } catch (SQLException e) {
            showError("Error updating profile: " + e.getMessage());
        }
    }
    
    private void loadMyBookings() {
        try {
            List<Ticket> tickets = TicketDAO.getTicketsByUser(currentUser.getUserId());
            bookingTable.getItems().clear();
            bookingTable.getItems().addAll(tickets);
        } catch (SQLException e) {
            showError("Error loading bookings: " + e.getMessage());
        }
    }
    
    private void handleSearch(String origin, String destination, LocalDate date) {
        try {
            System.out.println("Searching for flights with criteria:");
            System.out.println("Origin: " + (origin != null ? origin : "Any"));
            System.out.println("Destination: " + (destination != null ? destination : "Any"));
            System.out.println("Date: " + (date != null ? date : "Any"));
            
            // Clear the table first
            flightTable.getItems().clear();
            
            // Validate inputs
            if ((origin == null || origin.trim().isEmpty()) && 
                (destination == null || destination.trim().isEmpty()) && 
                date == null) {
                showInfo("Please enter at least one search criteria");
                return;
            }
            
            // Convert date to start of day if provided, otherwise null
            LocalDateTime dateTime = null;
            if (date != null) {
                dateTime = date.atStartOfDay();
                System.out.println("Searching for date: " + dateTime);
            }
            
            // Get flights based on search criteria
            List<Flight> flights = FlightDAO.searchFlights(
                origin != null && !origin.trim().isEmpty() ? origin.trim() : null,
                destination != null && !destination.trim().isEmpty() ? destination.trim() : null,
                dateTime
            );
            
            System.out.println("Found " + flights.size() + " flights matching criteria");
            
            // Debug output for first flight if available
            if (!flights.isEmpty()) {
                try {
                    Flight first = flights.get(0);
                    System.out.println("Sample flight details:");
                    System.out.println("  Flight Number: " + first.getFlightNumber());
                    System.out.println("  Route: " + first.getOrigin() + " -> " + first.getDestination());
                    System.out.println("  Departure: " + first.getDepartureTime());
                    System.out.println("  Available Seats: " + first.getAvailableSeats());
                    System.out.println("  Status: " + first.getStatus());
                } catch (Exception e) {
                    System.out.println("Error printing sample flight details: " + e.getMessage());
                }
            }
            
            // Update the table with search results
            flightTable.getItems().addAll(flights);
            
            // Show appropriate message based on results
            if (flights.isEmpty()) {
                showInfo("No flights found matching your search criteria.\nTry different dates or locations.");
            } else {
                String message = String.format("Found %d flight%s matching your criteria", 
                    flights.size(), flights.size() == 1 ? "" : "s");
                if (date != null && date.isBefore(LocalDate.now())) {
                    message += "\nNote: Showing historical flight data";
                }
                showSuccess(message);
                flightTable.refresh();
            }
            
        } catch (SQLException e) {
            System.out.println("Database error during search: " + e.getMessage());
            e.printStackTrace();
            showError("Error searching flights: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error during search: " + e.getMessage());
            e.printStackTrace();
            showError("An unexpected error occurred while searching flights");
        }
    }
    
    private Button createStyledButton(String text, String type) {
        Button button = new Button(text);
        String baseStyle = "-fx-font-weight: bold; -fx-font-size: 14; -fx-cursor: hand;";
        
        switch (type) {
            case "header":
                button.setStyle(baseStyle + "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 20;");
                break;
            case "search":
                button.setStyle(baseStyle + "-fx-background-color: #4361ee; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
                break;
            case "book":
                button.setStyle(baseStyle + "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
                break;
            case "view":
                button.setStyle(baseStyle + "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
                break;
            case "cancel":
                button.setStyle(baseStyle + "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
                break;
            case "edit":
                button.setStyle(baseStyle + "-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
                break;
            case "logout":
                button.setStyle(baseStyle + "-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
                break;
            default:
                button.setStyle(baseStyle + "-fx-background-color: #4361ee; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
        }
        
        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.8;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.8;", "")));
        
        return button;
    }
    
    private VBox createStyledBox() {
        VBox box = new VBox(20);
        box.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"
        );
        box.setPadding(new Insets(30));
        return box;
    }
    
    private Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        label.setTextFill(Color.rgb(44, 62, 80));
        label.setPadding(new Insets(0, 0, 20, 0));
        return label;
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 