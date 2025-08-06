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

public class StaffRegisterUI extends Application {
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField fullNameField;
    private TextField phoneField;
    private ComboBox<String> genderComboBox;
    private ComboBox<String> positionComboBox;
    private ComboBox<String> departmentComboBox;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flight Management System - Staff Registration");
        
        // Create main container with gradient background
        VBox mainContainer = new VBox(20);
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a237e, #0d47a1);");
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.CENTER);
        
        // Create registration form container
        VBox formBox = createStyledBox();
        formBox.setMaxWidth(400);
        
        // Header
        Label headerLabel = new Label("Staff Registration");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        headerLabel.setTextFill(Color.rgb(44, 62, 80));
        
        // Initialize form fields
        emailField = createStyledTextField("Email");
        passwordField = createStyledPasswordField("Password");
        confirmPasswordField = createStyledPasswordField("Confirm Password");
        fullNameField = createStyledTextField("Full Name");
        phoneField = createStyledTextField("Phone Number");
        
        // Gender ComboBox
        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        genderComboBox.setPromptText("Select Gender");
        genderComboBox.setMaxWidth(Double.MAX_VALUE);
        genderComboBox.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #cccccc; -fx-border-width: 1;");
        
        // Position ComboBox
        positionComboBox = new ComboBox<>();
        positionComboBox.getItems().addAll(
            "Flight Attendant",
            "Pilot",
            "Ground Staff",
            "Maintenance Staff",
            "Customer Service"
        );
        positionComboBox.setPromptText("Select Position");
        positionComboBox.setMaxWidth(Double.MAX_VALUE);
        positionComboBox.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #cccccc; -fx-border-width: 1;");
        
        // Department ComboBox
        departmentComboBox = new ComboBox<>();
        departmentComboBox.getItems().addAll(
            "Flight Operations",
            "Cabin Services",
            "Ground Operations",
            "Maintenance",
            "Customer Relations"
        );
        departmentComboBox.setPromptText("Select Department");
        departmentComboBox.setMaxWidth(Double.MAX_VALUE);
        departmentComboBox.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #cccccc; -fx-border-width: 1;");
        
        // Create buttons
        Button registerButton = createStyledButton("Register", "register");
        Button backButton = createStyledButton("Back to Main", "back");
        
        // Button actions
        registerButton.setOnAction(e -> handleRegistration(primaryStage));
        backButton.setOnAction(e -> {
            new MainPage().start(new Stage());
            primaryStage.close();
        });
        
        // Add components to form
        formBox.getChildren().addAll(
            headerLabel,
            new Label("Email:"),
            emailField,
            new Label("Password:"),
            passwordField,
            new Label("Confirm Password:"),
            confirmPasswordField,
            new Label("Full Name:"),
            fullNameField,
            new Label("Gender:"),
            genderComboBox,
            new Label("Phone:"),
            phoneField,
            new Label("Position:"),
            positionComboBox,
            new Label("Department:"),
            departmentComboBox,
            new HBox(10, registerButton, backButton)
        );
        
        // Add form to main container
        mainContainer.getChildren().add(formBox);
        
        // Set up the scene
        Scene scene = new Scene(mainContainer, 600, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void handleRegistration(Stage primaryStage) {
        try {
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String fullName = fullNameField.getText().trim();
            String gender = genderComboBox.getValue();
            String phone = phoneField.getText().trim();
            String position = positionComboBox.getValue();
            String department = departmentComboBox.getValue();
            
            // Validate input fields
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
                fullName.isEmpty() || gender == null || phone.isEmpty() || 
                position == null || department == null) {
                showError("Please fill in all fields!");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match!");
                return;
            }
            
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                showError("Please enter a valid email address!");
                return;
            }
            
            if (password.length() < 6) {
                showError("Password must be at least 6 characters long!");
                return;
            }
            
            if (!phone.matches("\\d{10}")) {
                showError("Please enter a valid 10-digit phone number!");
                return;
            }
            
            // Create new staff
            Staff staff = new Staff();
            staff.setEmail(email);
            staff.setPassword(password);
            staff.setFullName(fullName);
            staff.setGender(gender);
            staff.setPhone(phone);
            staff.setPosition(position);
            staff.setDepartment(department);
            staff.setStatus("PENDING");
            
            // Register staff
            StaffDAO.register(staff);
            
            showSuccess("Registration successful! Please login to continue.");
            new LoginUI().start(new Stage());
            primaryStage.close();
            
        } catch (SQLException ex) {
            if (ex.getMessage().contains("duplicate")) {
                showError("This email is already registered!");
            } else {
                showError("Error during registration: " + ex.getMessage());
            }
        } catch (Exception ex) {
            showError("Error during registration: " + ex.getMessage());
        }
    }
    
    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #cccccc; -fx-border-width: 1;");
        field.setPrefHeight(35);
        return field;
    }
    
    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #cccccc; -fx-border-width: 1;");
        field.setPrefHeight(35);
        return field;
    }
    
    private Button createStyledButton(String text, String type) {
        Button button = new Button(text);
        String baseStyle = "-fx-background-radius: 5; -fx-padding: 10 20; -fx-font-weight: bold; -fx-cursor: hand;";
        
        if (type.equals("register")) {
            button.setStyle(baseStyle + "-fx-background-color: #2196F3; -fx-text-fill: white;");
        } else {
            button.setStyle(baseStyle + "-fx-background-color: #757575; -fx-text-fill: white;");
        }
        
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.9;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.9;", "")));
        
        return button;
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