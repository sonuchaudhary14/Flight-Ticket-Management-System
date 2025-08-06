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
import javafx.beans.property.SimpleStringProperty;

public class AdminDashboard extends Application {
    private Admin currentAdmin;
    private TableView<Staff> staffTable;
    private TableView<User> userTable;
    private TableView<Ticket> bookingStatusTable;
    private TableView<Flight> allFlightsTable;
    
    public AdminDashboard(Admin admin) {
        this.currentAdmin = admin;
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flight Management System - Admin Dashboard");
        
        // Create main container with gradient background
        VBox mainContainer = new VBox(20);
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a237e, #0d47a1);");
        mainContainer.setPadding(new Insets(20));
        
        // Header with welcome message
        Label welcomeLabel = new Label("Admin Dashboard - Welcome, " + currentAdmin.getEmail());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.WHITE);
        
        // Create tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab staffTab = new Tab("Manage Staff");
        Tab userTab = new Tab("Manage Users");
        Tab bookingStatusTab = new Tab("Booking Status");
        Tab allFlightsTab = new Tab("All Flights");
        tabPane.getTabs().addAll(staffTab, userTab, bookingStatusTab, allFlightsTab);
        
        // Staff management tab content
        VBox staffBox = createStyledBox();
        setupStaffTable();
        
        HBox staffButtonBox = new HBox(10);
        staffButtonBox.setAlignment(Pos.CENTER);
        Button approveStaffBtn = createStyledButton("Approve Staff", "approve");
        Button rejectStaffBtn = createStyledButton("Reject Staff", "reject");
        staffButtonBox.getChildren().addAll(approveStaffBtn, rejectStaffBtn);
        
        staffBox.getChildren().addAll(
            createSectionTitle("Staff Management"),
            staffButtonBox,
            staffTable
        );
        staffTab.setContent(staffBox);
        
        // User management tab content
        VBox userBox = createStyledBox();
        setupUserTable();
        
        HBox userButtonBox = new HBox(10);
        userButtonBox.setAlignment(Pos.CENTER);
        Button approveUserBtn = createStyledButton("Approve User", "approve");
        Button rejectUserBtn = createStyledButton("Reject User", "reject");
        userButtonBox.getChildren().addAll(approveUserBtn, rejectUserBtn);
        
        userBox.getChildren().addAll(
            createSectionTitle("User Management"),
            userButtonBox,
            userTable
        );
        userTab.setContent(userBox);
        
        // Booking Status tab content
        VBox bookingStatusBox = createStyledBox();
        setupBookingStatusTable();
        
        HBox bookingStatusButtonBox = new HBox(10);
        bookingStatusButtonBox.setAlignment(Pos.CENTER);
        Button refreshBookingStatusBtn = createStyledButton("Refresh Bookings", "default");
        bookingStatusButtonBox.getChildren().add(refreshBookingStatusBtn);
        
        bookingStatusBox.getChildren().addAll(
            createSectionTitle("All Bookings Status"),
            bookingStatusButtonBox,
            bookingStatusTable
        );
        bookingStatusTab.setContent(bookingStatusBox);

        // All Flights tab content
        VBox allFlightsBox = createStyledBox();
        setupAllFlightsTable();
        
        HBox allFlightsButtonBox = new HBox(10);
        allFlightsButtonBox.setAlignment(Pos.CENTER);
        Button refreshAllFlightsBtn = createStyledButton("Refresh Flights", "default");
        allFlightsButtonBox.getChildren().add(refreshAllFlightsBtn);
        
        allFlightsBox.getChildren().addAll(
            createSectionTitle("Available Flights"),
            allFlightsButtonBox,
            allFlightsTable
        );
        allFlightsTab.setContent(allFlightsBox);
        
        // Logout button
        Button logoutBtn = createStyledButton("Logout", "logout");
        logoutBtn.setOnAction(e -> {
            new LoginUI().start(new Stage());
            primaryStage.close();
        });
        
        mainContainer.getChildren().addAll(welcomeLabel, tabPane, logoutBtn);
        
        // Set up button actions
        approveStaffBtn.setOnAction(e -> handleStaffApproval(true));
        rejectStaffBtn.setOnAction(e -> handleStaffApproval(false));
        approveUserBtn.setOnAction(e -> handleUserApproval(true));
        rejectUserBtn.setOnAction(e -> handleUserApproval(false));
        refreshBookingStatusBtn.setOnAction(e -> loadBookingStatusData());
        refreshAllFlightsBtn.setOnAction(e -> loadAllFlightsData());
        
        // Load initial data
        loadStaffData();
        loadUserData();
        loadBookingStatusData();
        loadAllFlightsData();
        
        // Set up the scene
        Scene scene = new Scene(mainContainer, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void setupStaffTable() {
        staffTable = new TableView<>();
        
        TableColumn<Staff, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        TableColumn<Staff, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<Staff, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        TableColumn<Staff, String> positionCol = new TableColumn<>("Position");
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        
        TableColumn<Staff, String> departmentCol = new TableColumn<>("Department");
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        
        TableColumn<Staff, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        staffTable.getColumns().addAll(emailCol, nameCol, phoneCol, positionCol, departmentCol, statusCol);
        staffTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupUserTable() {
        userTable = new TableView<>();
        
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        TableColumn<User, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        TableColumn<User, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        userTable.getColumns().addAll(emailCol, nameCol, phoneCol, addressCol, statusCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupBookingStatusTable() {
        bookingStatusTable = new TableView<>();
        
        TableColumn<Ticket, Integer> ticketIdCol = new TableColumn<>("Ticket ID");
        ticketIdCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        
        TableColumn<Ticket, String> passengerNameCol = new TableColumn<>("Passenger Name");
        passengerNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<Ticket, String> flightNumberCol = new TableColumn<>("Flight Number");
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        
        TableColumn<Ticket, String> seatNumberCol = new TableColumn<>("Seat Number");
        seatNumberCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        
        TableColumn<Ticket, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<Ticket, String> statusCol = new TableColumn<>("Booking Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Ticket, String> paymentStatusCol = new TableColumn<>("Payment Status");
        paymentStatusCol.setCellValueFactory(cellData -> new SimpleStringProperty("PAID"));
        
        bookingStatusTable.getColumns().addAll(
            ticketIdCol, passengerNameCol, flightNumberCol, seatNumberCol,
            priceCol, statusCol, paymentStatusCol
        );
        bookingStatusTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupAllFlightsTable() {
        allFlightsTable = new TableView<>();
        
        TableColumn<Flight, Integer> flightIdCol = new TableColumn<>("Flight ID");
        flightIdCol.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        
        TableColumn<Flight, String> flightNumberCol = new TableColumn<>("Flight Number");
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        
        TableColumn<Flight, String> originCol = new TableColumn<>("Origin");
        originCol.setCellValueFactory(new PropertyValueFactory<>("origin"));
        
        TableColumn<Flight, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        
        TableColumn<Flight, LocalDateTime> departureTimeCol = new TableColumn<>("Departure Time");
        departureTimeCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        
        TableColumn<Flight, LocalDateTime> arrivalTimeCol = new TableColumn<>("Arrival Time");
        arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        
        TableColumn<Flight, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<Flight, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        
        TableColumn<Flight, Integer> availableSeatsCol = new TableColumn<>("Available Seats");
        availableSeatsCol.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        
        TableColumn<Flight, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        allFlightsTable.getColumns().addAll(
            flightIdCol, flightNumberCol, originCol, destinationCol,
            departureTimeCol, arrivalTimeCol, priceCol, capacityCol,
            availableSeatsCol, statusCol
        );
        allFlightsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void loadStaffData() {
        try {
            List<Staff> staffList = StaffDAO.getAllStaff();
            staffTable.getItems().clear();
            staffTable.getItems().addAll(staffList);
        } catch (SQLException e) {
            showError("Error loading staff data: " + e.getMessage());
        }
    }
    
    private void loadUserData() {
        try {
            List<User> userList = UserDAO.getAllUsers();
            userTable.getItems().clear();
            userTable.getItems().addAll(userList);
        } catch (SQLException e) {
            showError("Error loading user data: " + e.getMessage());
        }
    }
    
    private void loadBookingStatusData() {
        try {
            List<Ticket> tickets = TicketDAO.getAllTickets();
            bookingStatusTable.getItems().clear();
            bookingStatusTable.getItems().addAll(tickets);
        } catch (SQLException e) {
            showError("Error loading booking status data: " + e.getMessage());
        }
    }
    
    private void loadAllFlightsData() {
        try {
            List<Flight> flights = FlightDAO.getAllFlights();
            allFlightsTable.getItems().clear();
            allFlightsTable.getItems().addAll(flights);
        } catch (SQLException e) {
            showError("Error loading flights data: " + e.getMessage());
        }
    }
    
    private void handleStaffApproval(boolean approve) {
        Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        if (selectedStaff == null) {
            showError("Please select a staff member.");
            return;
        }
        
        try {
            StaffDAO.updateStatus(selectedStaff.getStaffId(), approve ? "ACTIVE" : "INACTIVE");
            loadStaffData();
            showSuccess("Staff status updated successfully.");
        } catch (SQLException e) {
            showError("Error updating staff status: " + e.getMessage());
        }
    }
    
    private void handleUserApproval(boolean approve) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showError("Please select a user.");
            return;
        }
        
        try {
            UserDAO.updateStatus(selectedUser.getUserId(), approve ? "ACTIVE" : "INACTIVE");
            loadUserData();
            showSuccess("User status updated successfully.");
        } catch (SQLException e) {
            showError("Error updating user status: " + e.getMessage());
        }
    }
    
    private VBox createStyledBox() {
        VBox box = new VBox(15);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        box.setPadding(new Insets(20));
        
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        box.setEffect(shadow);
        
        return box;
    }
    
    private Label createSectionTitle(String text) {
        Label title = new Label(text);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.rgb(44, 62, 80));
        return title;
    }
    
    private Button createStyledButton(String text, String type) {
        Button button = new Button(text);
        String baseStyle = "-fx-background-radius: 5; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;";
        
        switch (type) {
            case "approve":
                button.setStyle(baseStyle + "-fx-background-color: #4CAF50; -fx-text-fill: white;");
                break;
            case "reject":
                button.setStyle(baseStyle + "-fx-background-color: #f44336; -fx-text-fill: white;");
                break;
            case "logout":
                button.setStyle(baseStyle + "-fx-background-color: #757575; -fx-text-fill: white;");
                break;
            default:
                button.setStyle(baseStyle + "-fx-background-color: #2196F3; -fx-text-fill: white;");
        }
        
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.9;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.9;", "")));
        
        return button;
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
} 