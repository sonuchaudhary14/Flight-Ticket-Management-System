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
import java.sql.SQLException;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class LoginUI extends Application {
    private TextField emailField;
    private PasswordField passwordField;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flight Management System - User Login");
        
        // Create main container with modern gradient background
        VBox mainContainer = new VBox(20);
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.CENTER);
        
        // Create login form container with glass effect
        VBox formBox = createStyledBox();
        formBox.setMaxWidth(450);
        
        // Header with icon
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER);
        
        Label headerLabel = new Label("Welcome Back");
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        headerLabel.setTextFill(Color.rgb(44, 62, 80));
        
        Label subLabel = new Label("Please login to your account");
        subLabel.setFont(Font.font("Segoe UI", 14));
        subLabel.setTextFill(Color.rgb(127, 140, 141));
        
        VBox headerText = new VBox(5);
        headerText.getChildren().addAll(headerLabel, subLabel);
        headerText.setAlignment(Pos.CENTER);
        
        headerBox.getChildren().add(headerText);
        
        // Initialize form fields with modern styling
        emailField = createStyledTextField("Enter your email");
        passwordField = createStyledPasswordField("Enter your password");
        
        // Create buttons with modern styling
        Button loginButton = createStyledButton("Sign In", "login");
        Button registerButton = createStyledButton("Create Account", "register");
        Button backButton = createStyledButton("Back to Main", "back");
        
        // Button actions with fade transitions
        loginButton.setOnAction(e -> handleLogin(primaryStage));
        registerButton.setOnAction(e -> {
            new RegisterUI().start(new Stage());
            primaryStage.close();
        });
        backButton.setOnAction(e -> {
            new MainPage().start(new Stage());
            primaryStage.close();
        });
        
        // Create form fields container with proper spacing
        VBox fieldsContainer = new VBox(15);
        fieldsContainer.setPadding(new Insets(20.0, 0.0, 20.0, 0.0));
        fieldsContainer.getChildren().addAll(
            new Label("Email"),
            emailField,
            new Label("Password"),
            passwordField
        );
        
        // Create button container with proper spacing
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(loginButton, registerButton, backButton);
        
        // Add components to form with better spacing
        formBox.getChildren().addAll(
            headerBox,
            fieldsContainer,
            buttonContainer
        );
        
        // Add form to main container with animation
        mainContainer.getChildren().add(formBox);
        
        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), formBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        
        // Set up the scene
        Scene scene = new Scene(mainContainer, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void handleLogin(Stage primaryStage) {
        try {
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            
            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                showError("Please enter both email and password!");
                return;
            }
            
            // Try to login as staff first
            Staff staff = StaffDAO.login(email, password);
            if (staff != null) {
                showSuccess("Staff login successful!");
                Stage staffStage = new Stage();
                StaffDashboard staffDashboard = new StaffDashboard(staff);
                staffDashboard.start(staffStage);
                primaryStage.close();
                return;
            }
            
            // Try to login as admin
            Admin admin = AdminDAO.login(email, password);
            if (admin != null) {
                showSuccess("Admin login successful!");
                Stage adminStage = new Stage();
                AdminDashboard adminDashboard = new AdminDashboard(admin);
                adminDashboard.start(adminStage);
                primaryStage.close();
                return;
            }
            
            // Try to login as user
            User user = UserDAO.login(email, password);
            if (user != null) {
                showSuccess("Login successful!");
                Stage userStage = new Stage();
                UserDashboard userDashboard = new UserDashboard(user);
                userDashboard.start(userStage);
                primaryStage.close();
                return;
            }
            
            // If no login succeeded
            showError("Invalid email or password!");
            
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Error during login: " + ex.getMessage());
        }
    }
    
    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; " +
                      "-fx-border-color: #bdc3c7; -fx-border-width: 1.5; " +
                      "-fx-padding: 12; -fx-font-size: 14; " +
                      "-fx-background-color: white;");
        field.setPrefHeight(45);
        
        // Add focus effect
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(field.getStyle() + "-fx-border-color: #3498db;");
            } else {
                field.setStyle(field.getStyle().replace("-fx-border-color: #3498db;", "-fx-border-color: #bdc3c7;"));
            }
        });
        
        return field;
    }
    
    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; " +
                      "-fx-border-color: #bdc3c7; -fx-border-width: 1.5; " +
                      "-fx-padding: 12; -fx-font-size: 14; " +
                      "-fx-background-color: white;");
        field.setPrefHeight(45);
        
        // Add focus effect
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(field.getStyle() + "-fx-border-color: #3498db;");
            } else {
                field.setStyle(field.getStyle().replace("-fx-border-color: #3498db;", "-fx-border-color: #bdc3c7;"));
            }
        });
        
        return field;
    }
    
    private Button createStyledButton(String text, String type) {
        Button button = new Button(text);
        String baseStyle = "-fx-background-radius: 8; -fx-padding: 12 25; " +
                         "-fx-font-weight: bold; -fx-font-size: 14; " +
                         "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);";
        
        if (type.equals("login")) {
            button.setStyle(baseStyle + "-fx-background-color: #2ecc71; -fx-text-fill: white;");
        } else if (type.equals("register")) {
            button.setStyle(baseStyle + "-fx-background-color: #3498db; -fx-text-fill: white;");
        } else {
            button.setStyle(baseStyle + "-fx-background-color: #95a5a6; -fx-text-fill: white;");
        }
        
        // Add hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle() + "-fx-opacity: 0.9;");
            button.setEffect(new DropShadow(10, 0, 0, Color.rgb(0, 0, 0, 0.2)));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("-fx-opacity: 0.9;", ""));
            button.setEffect(new DropShadow(10, 0, 0, Color.rgb(0, 0, 0, 0.1)));
        });
        
        return button;
    }
    
    private VBox createStyledBox() {
        VBox box = new VBox(20);
        box.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                    "-fx-background-radius: 15; -fx-padding: 30;");
        
        DropShadow shadow = new DropShadow();
        shadow.setRadius(20.0);
        shadow.setOffsetX(5.0);
        shadow.setOffsetY(5.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        box.setEffect(shadow);
        
        return box;
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