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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.sql.SQLException;
import javafx.scene.effect.DropShadow;
import java.util.Arrays;
import javafx.scene.shape.Circle;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.Optional;

public class StaffDashboard extends Application {
    private Staff currentStaff;
    private TableView<Flight> flightTable;
    private TableView<Ticket> ticketTable;
    
    public StaffDashboard(Staff staff) {
        this.currentStaff = staff;
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flight Management System - Staff Dashboard");
        
        // Create main container with modern gradient background
        VBox mainContainer = new VBox(0);
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8f9fa, #e9ecef);");
        
        // Create header with staff info
        HBox header = new HBox(20);
        header.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #3498db); -fx-padding: 25;");
        header.setAlignment(Pos.CENTER_LEFT);
        
        // Staff profile section with avatar
        HBox profileBox = new HBox(20);
        profileBox.setStyle("-fx-background-color: transparent;");
        profileBox.setAlignment(Pos.CENTER_LEFT);
        
        // Profile info
        VBox profileInfo = new VBox(8);
        profileInfo.setStyle("-fx-background-color: transparent;");
        
        Label welcomeLabel = new Label("Welcome, " + currentStaff.getFullName());
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        welcomeLabel.setTextFill(Color.WHITE);
        
        Label roleLabel = new Label(currentStaff.getPosition() + " | " + currentStaff.getDepartment());
        roleLabel.setFont(Font.font("Segoe UI", 18));
        roleLabel.setTextFill(Color.rgb(255, 255, 255, 0.9));
        
        profileInfo.getChildren().addAll(welcomeLabel, roleLabel);
        profileBox.getChildren().addAll(profileInfo);
        
        // Spacer to push logout button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Logout button with modern styling
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-font-size: 16; -fx-cursor: hand; " +
                           "-fx-padding: 15 30; -fx-border-color: white; -fx-border-radius: 30; " +
                           "-fx-border-width: 2;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle(logoutButton.getStyle() + "-fx-background-color: rgba(255,255,255,0.15);"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(logoutButton.getStyle().replace("-fx-background-color: rgba(255,255,255,0.15);", "")));
        logoutButton.setOnAction(e -> {
            new MainPage().start(new Stage());
            primaryStage.close();
        });
        
        header.getChildren().addAll(profileBox, spacer, logoutButton);
        
        // Create tab pane with modern styling
        TabPane tabPane = new TabPane();
        tabPane.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-tab-min-height: 60px; " +
            "-fx-tab-max-height: 60px; " +
            "-fx-tab-background-color: #f8f9fa; " +
            "-fx-tab-text-fill: #2c3e50; " +
            "-fx-tab-selected-background-color: white; " +
            "-fx-tab-selected-text-fill: #3498db; " +
            "-fx-focus-color: transparent; " +
            "-fx-faint-focus-color: transparent; " +
            "-fx-tab-border-color: transparent; " +
            "-fx-tab-border-width: 0; " +
            "-fx-tab-padding: 0 30; " +
            "-fx-tab-spacing: 0; " +
            "-fx-font-size: 16; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, -2, 0);"
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Create tabs with modern styling
        Tab profileTab = new Tab("Profile");
        profileTab.setClosable(false);
        profileTab.setContent(createProfileTab());
        
        Tab flightTab = new Tab("Flights");
        flightTab.setClosable(false);
        flightTab.setContent(createFlightTab());
        
        Tab bookingTab = new Tab("Bookings");
        bookingTab.setClosable(false);
        bookingTab.setContent(createBookingTab());
        
        tabPane.getTabs().addAll(profileTab, flightTab, bookingTab);
        
        // Add components to main container
        mainContainer.getChildren().addAll(header, tabPane);
        
        // Set up the scene
        Scene scene = new Scene(mainContainer, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createProfileTab() {
        VBox profileContent = new VBox(20);
        profileContent.setPadding(new Insets(30));
        profileContent.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
        
        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(15.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        profileContent.setEffect(shadow);
        
        // Profile header with icon
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label profileHeader = new Label("Staff Profile");
        profileHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        profileHeader.setTextFill(Color.rgb(44, 62, 80));
        
        headerBox.getChildren().addAll(profileHeader);
        
        // Profile information grid with modern styling
        GridPane profileGrid = new GridPane();
        profileGrid.setHgap(30);
        profileGrid.setVgap(20);
        profileGrid.setPadding(new Insets(30));
        profileGrid.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 20;");
        
        // Create two columns for better layout
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        profileGrid.getColumnConstraints().addAll(col1, col2);
        
        // Add profile information with enhanced styling
        addProfileField(profileGrid, "Full Name:", currentStaff.getFullName(), 0, 0);
        addProfileField(profileGrid, "Email:", currentStaff.getEmail(), 0, 1);
        addProfileField(profileGrid, "Phone:", currentStaff.getPhone(), 1, 0);
        addProfileField(profileGrid, "Gender:", currentStaff.getGender(), 1, 1);
        addProfileField(profileGrid, "Position:", currentStaff.getPosition(), 2, 0);
        addProfileField(profileGrid, "Department:", currentStaff.getDepartment(), 2, 1);
        addProfileField(profileGrid, "Status:", currentStaff.getStatus(), 3, 0);
        
        // Create a container for the update button with proper spacing
        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(10, 0, 0, 0));
        
        // Update profile button with enhanced styling
        Button updateButton = createStyledButton("Update Profile", "update");
        updateButton.setStyle(updateButton.getStyle() + 
            "-fx-font-size: 14; " +
            "-fx-padding: 8 35; " +
            "-fx-background-color: #2980b9; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        
        updateButton.setOnMouseEntered(e -> updateButton.setStyle(updateButton.getStyle() + 
            "-fx-background-color: #3498db; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0, 0, 3);"));
        
        updateButton.setOnMouseExited(e -> updateButton.setStyle(updateButton.getStyle()
            .replace("-fx-background-color: #3498db;", "")
            .replace("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0, 0, 3);", "")));
        
        updateButton.setOnAction(e -> showUpdateProfileDialog());
        
        buttonContainer.getChildren().add(updateButton);
        
        // Add all components directly to profileContent
        profileContent.getChildren().addAll(headerBox, profileGrid, buttonContainer);
        return profileContent;
    }
    
    private void addProfileField(GridPane grid, String label, String value, int row, int col) {
        Label fieldLabel = new Label(label);
        fieldLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        fieldLabel.setTextFill(Color.rgb(44, 62, 80));
        
        Label fieldValue = new Label(value);
        fieldValue.setFont(Font.font("Segoe UI", 16));
        fieldValue.setTextFill(Color.rgb(52, 73, 94));
        
        // Add subtle background to each field with hover effect
        HBox fieldBox = new HBox(20);
        fieldBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);");
        fieldBox.getChildren().addAll(fieldLabel, fieldValue);
        
        // Add hover effect
        fieldBox.setOnMouseEntered(e -> fieldBox.setStyle(fieldBox.getStyle() + 
            "-fx-background-color: #f0f4f8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 12, 0, 0, 3);"));
        
        fieldBox.setOnMouseExited(e -> fieldBox.setStyle(fieldBox.getStyle()
            .replace("-fx-background-color: #f0f4f8;", "")
            .replace("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 12, 0, 0, 3);", "")));
        
        grid.add(fieldBox, col, row);
    }
    
    private VBox createFlightTab() {
        VBox flightContent = new VBox(30);
        flightContent.setPadding(new Insets(40));
        flightContent.setStyle("-fx-background-color: white; -fx-background-radius: 25;");
        
        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(20.0);
        shadow.setOffsetX(5.0);
        shadow.setOffsetY(5.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        flightContent.setEffect(shadow);
        
        // Flight header with icon
        HBox headerBox = new HBox(25);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label flightHeader = new Label("Flight Management");
        flightHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        flightHeader.setTextFill(Color.rgb(44, 62, 80));
        
        headerBox.getChildren().addAll(flightHeader);
        
        // Create flight table
        flightTable = new TableView<>();
        setupFlightTable();
        
        // Add buttons for flight management
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button addFlightBtn = createStyledButton("Add Flight", "add");
        Button updateFlightBtn = createStyledButton("Update Flight", "update");
        Button deleteFlightBtn = createStyledButton("Delete Flight", "delete");
        
        buttonBox.getChildren().addAll(addFlightBtn, updateFlightBtn, deleteFlightBtn);
        
        flightContent.getChildren().addAll(headerBox, flightTable, buttonBox);
        return flightContent;
    }
    
    private VBox createBookingTab() {
        VBox bookingContent = new VBox(30);
        bookingContent.setPadding(new Insets(40));
        bookingContent.setStyle("-fx-background-color: white; -fx-background-radius: 25;");
        
        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(20.0);
        shadow.setOffsetX(5.0);
        shadow.setOffsetY(5.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        bookingContent.setEffect(shadow);
        
        // Booking header with icon
        HBox headerBox = new HBox(25);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label bookingHeader = new Label("Booking Management");
        bookingHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        bookingHeader.setTextFill(Color.rgb(44, 62, 80));
        
        headerBox.getChildren().addAll(bookingHeader);
        
        // Create booking table
        ticketTable = new TableView<>();
        setupTicketTable();
        
        // Add buttons for booking management
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button viewDetailsBtn = createStyledButton("View Details", "view");
        viewDetailsBtn.setOnAction(e -> handleViewTicketDetails());
        
        Button updateStatusBtn = createStyledButton("Update Status", "update");
        updateStatusBtn.setOnAction(e -> handleUpdateTicketStatus());
        
        Button cancelBookingBtn = createStyledButton("Cancel Booking", "cancel");
        cancelBookingBtn.setOnAction(e -> handleCancelBooking());
        
        Button refreshBtn = createStyledButton("Refresh", "view");
        refreshBtn.setOnAction(e -> loadAllBookings());
        
        buttonBox.getChildren().addAll(viewDetailsBtn, updateStatusBtn, cancelBookingBtn, refreshBtn);
        
        bookingContent.getChildren().addAll(headerBox, ticketTable, buttonBox);
        return bookingContent;
    }
    
    private Button createStyledButton(String text, String type) {
        Button button = new Button(text);
        String baseStyle = "-fx-font-weight: bold; -fx-font-size: 14; -fx-cursor: hand; -fx-padding: 8 20; -fx-background-radius: 20;";
        
        switch (type) {
            case "add":
                button.setStyle(baseStyle + "-fx-background-color: #2ecc71; -fx-text-fill: white;");
                break;
            case "update":
                button.setStyle(baseStyle + "-fx-background-color: #3498db; -fx-text-fill: white;");
                break;
            case "delete":
                button.setStyle(baseStyle + "-fx-background-color: #e74c3c; -fx-text-fill: white;");
                break;
            case "view":
                button.setStyle(baseStyle + "-fx-background-color: #9b59b6; -fx-text-fill: white;");
                break;
            case "cancel":
                button.setStyle(baseStyle + "-fx-background-color: #e74c3c; -fx-text-fill: white;");
                break;
            default:
                button.setStyle(baseStyle + "-fx-background-color: #3498db; -fx-text-fill: white;");
        }
        
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.9;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.9;", "")));
        
        return button;
    }
    
    private void showUpdateProfileDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Update Profile");
        dialog.setHeaderText("Update your profile information");
        
        // Create form fields
        TextField nameField = new TextField(currentStaff.getFullName());
        TextField phoneField = new TextField(currentStaff.getPhone());
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    currentStaff.setFullName(nameField.getText());
                    currentStaff.setPhone(phoneField.getText());
                    StaffDAO.updateStaff(currentStaff);
                    showSuccess("Profile updated successfully!");
                } catch (SQLException ex) {
                    showError("Error updating profile: " + ex.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
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
    
    private void setupFlightTable() {
        flightTable = new TableView<>();
        
        TableColumn<Flight, String> flightNumberCol = new TableColumn<>("Flight Number");
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        
        TableColumn<Flight, String> originCol = new TableColumn<>("Origin");
        originCol.setCellValueFactory(new PropertyValueFactory<>("origin"));
        
        TableColumn<Flight, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        
        TableColumn<Flight, LocalDateTime> departureCol = new TableColumn<>("Departure");
        departureCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        departureCol.setCellFactory(column -> new TableCell<Flight, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });
        
        TableColumn<Flight, LocalDateTime> arrivalCol = new TableColumn<>("Arrival");
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        arrivalCol.setCellFactory(column -> new TableCell<Flight, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });
        
        TableColumn<Flight, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(column -> new TableCell<Flight, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%.2f", price));
            }
        });
        
        TableColumn<Flight, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        
        TableColumn<Flight, Integer> availableSeatsCol = new TableColumn<>("Available");
        availableSeatsCol.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
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
        
        TableColumn<Flight, Integer> bookedSeatsCol = new TableColumn<>("Booked");
        bookedSeatsCol.setCellValueFactory(cellData -> {
            Flight flight = cellData.getValue();
            int booked = flight.getCapacity() - flight.getAvailableSeats();
            return new SimpleIntegerProperty(booked).asObject();
        });
        
        TableColumn<Flight, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
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
        
        flightTable.getColumns().addAll(
            flightNumberCol, originCol, destinationCol, 
            departureCol, arrivalCol, priceCol, 
            capacityCol, availableSeatsCol, bookedSeatsCol, statusCol
        );
        
        // Set column resize policy
        flightTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Add some padding to the table
        flightTable.setStyle("-fx-background-color: transparent;");
        
        // Add view bookings button
        Button viewBookingsBtn = new Button("View Bookings");
        viewBookingsBtn.setOnAction(e -> handleViewBookings());
        
        // Add the button to the layout
        VBox flightBox = new VBox(10);
        flightBox.getChildren().addAll(flightTable, viewBookingsBtn);
        
        // Load flights
        loadFlights();
    }
    
    private void handleViewBookings() {
        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            showError("Please select a flight to view bookings");
            return;
        }
        
        try {
            List<Ticket> tickets = TicketDAO.getTicketsByFlight(selectedFlight.getFlightId());
            
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Flight Bookings");
            dialog.setHeaderText("Bookings for Flight " + selectedFlight.getFlightNumber());
            
            VBox content = new VBox(10);
            content.setPadding(new Insets(20));
            
            // Create a table for tickets
            TableView<Ticket> ticketTable = new TableView<>();
            
            TableColumn<Ticket, String> passengerCol = new TableColumn<>("Passenger");
            passengerCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            
            TableColumn<Ticket, String> seatCol = new TableColumn<>("Seat");
            seatCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
            
            TableColumn<Ticket, LocalDateTime> bookingTimeCol = new TableColumn<>("Booking Time");
            bookingTimeCol.setCellValueFactory(new PropertyValueFactory<>("bookingDateTime"));
            bookingTimeCol.setCellFactory(column -> new TableCell<Ticket, LocalDateTime>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : formatter.format(item));
                }
            });
            
            TableColumn<Ticket, String> statusCol = new TableColumn<>("Status");
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
            
            ticketTable.getColumns().addAll(passengerCol, seatCol, bookingTimeCol, statusCol);
            ticketTable.getItems().addAll(tickets);
            
            // Add summary labels
            Label summaryLabel = new Label(String.format(
                "Total Bookings: %d\nAvailable Seats: %d\nTotal Capacity: %d",
                tickets.size(),
                selectedFlight.getAvailableSeats(),
                selectedFlight.getCapacity()
            ));
            
            content.getChildren().addAll(summaryLabel, ticketTable);
            
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            
            dialog.showAndWait();
            
        } catch (SQLException e) {
            showError("Error loading bookings: " + e.getMessage());
        }
    }
    
    private void loadFlights() {
        try {
            List<Flight> flights = FlightDAO.getAllFlights();
            flightTable.getItems().clear();
            flightTable.getItems().addAll(flights);
        } catch (SQLException e) {
            showError("Error loading flights: " + e.getMessage());
        }
    }
    
    private void setupTicketTable() {
        ticketTable = new TableView<>();
        
        TableColumn<Ticket, Integer> ticketIdCol = new TableColumn<>("Ticket ID");
        ticketIdCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        
        TableColumn<Ticket, String> fullNameCol = new TableColumn<>("Passenger");
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<Ticket, String> flightNumberCol = new TableColumn<>("Flight");
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        
        TableColumn<Ticket, String> seatNumberCol = new TableColumn<>("Seat");
        seatNumberCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        
        TableColumn<Ticket, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(column -> new TableCell<Ticket, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%.2f", price));
            }
        });
        
        TableColumn<Ticket, String> paymentStatusCol = new TableColumn<>("Payment");
        paymentStatusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        paymentStatusCol.setCellFactory(column -> new TableCell<Ticket, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status.toUpperCase()) {
                        case "COMPLETED":
                            setStyle("-fx-text-fill: green;");
                            break;
                        case "PENDING":
                            setStyle("-fx-text-fill: orange;");
                            break;
                        case "FAILED":
                            setStyle("-fx-text-fill: red;");
                            break;
                        default:
                            setStyle("");
                    }
                }
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
                    switch (status.toUpperCase()) {
                        case "BOOKED":
                            setStyle("-fx-text-fill: green;");
                            break;
                        case "CHECKED_IN":
                            setStyle("-fx-text-fill: blue;");
                            break;
                        case "CANCELLED":
                            setStyle("-fx-text-fill: red;");
                            break;
                        case "PENDING":
                            setStyle("-fx-text-fill: orange;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        
        TableColumn<Ticket, LocalDateTime> bookingTimeCol = new TableColumn<>("Booking Time");
        bookingTimeCol.setCellValueFactory(new PropertyValueFactory<>("bookingDateTime"));
        bookingTimeCol.setCellFactory(column -> new TableCell<Ticket, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });
        
        ticketTable.getColumns().addAll(
            ticketIdCol, fullNameCol, flightNumberCol,
            seatNumberCol, priceCol, paymentStatusCol, 
            statusCol, bookingTimeCol
        );
        
        // Set column resize policy
        ticketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Add some padding to the table
        ticketTable.setStyle("-fx-background-color: transparent;");
        
        // Load all bookings
        loadAllBookings();
    }
    
    private void loadAllBookings() {
        try {
            List<Ticket> tickets = TicketDAO.getAllTickets();
            ticketTable.getItems().clear();
            ticketTable.getItems().addAll(tickets);
            System.out.println("Loaded " + tickets.size() + " bookings");
        } catch (SQLException e) {
            showError("Error loading bookings: " + e.getMessage());
        }
    }
    
    private void handleViewTicketDetails() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket == null) {
            showError("Please select a booking to view details");
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
                new Label("Passenger: " + selectedTicket.getFullName()),
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
    
    private void handleUpdateTicketStatus() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket == null) {
            showError("Please select a booking to update");
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update Booking Status");
        dialog.setHeaderText("Update status for Ticket #" + selectedTicket.getTicketId());

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("BOOKED", "CHECKED_IN", "CANCELLED");
        statusCombo.setValue(selectedTicket.getStatus());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.add(new Label("New Status:"), 0, 0);
        grid.add(statusCombo, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return statusCombo.getValue();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                String newStatus = result.get();
                TicketDAO.updateTicketStatus(selectedTicket.getTicketId(), newStatus);
                
                // If status is CANCELLED, create a payment record for refund
                if ("CANCELLED".equals(newStatus)) {
                    Payment payment = new Payment(selectedTicket.getTicketId(), selectedTicket.getPrice(), "REFUND");
                    payment.setTransactionId("REF" + System.currentTimeMillis());
                    payment.setPaymentStatus("COMPLETED");
                    payment.setPaymentDate(LocalDateTime.now());
                    PaymentDAO.addPayment(payment);
                }
                
                loadAllBookings();
                showSuccess("Booking status updated successfully");
            } catch (SQLException e) {
                showError("Error updating booking status: " + e.getMessage());
            }
        }
    }
    
    private void handleCancelBooking() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket == null) {
            showError("Please select a booking to cancel");
            return;
        }
        
        if ("CANCELLED".equals(selectedTicket.getStatus())) {
            showError("This booking is already cancelled");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Booking");
        alert.setHeaderText("Are you sure you want to cancel this booking?");
        alert.setContentText("This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    TicketDAO.cancelTicket(selectedTicket.getTicketId());
                    loadAllBookings(); // Refresh the table
                    showSuccess("Booking cancelled successfully");
                } catch (SQLException e) {
                    showError("Error cancelling booking: " + e.getMessage());
                }
            }
        });
    }
} 