package pkg1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

public class MainPage extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Flight Management System");
            
            
            VBox mainContainer = new VBox(0); 
            mainContainer.setStyle("-fx-background-color: white;");
            
            // Create header
            HBox header = new HBox();
            header.setStyle("-fx-background-color: #4361ee; -fx-padding: 20;");
            header.setPrefHeight(80);
            header.setAlignment(Pos.CENTER_LEFT);
            
            Label logoLabel = new Label("Flight Management System");
            logoLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
            logoLabel.setTextFill(Color.WHITE);
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            // Create login buttons with modern styling
            Button loginBtn = createStyledButton("Login", "login");
            Button signupBtn = createStyledButton("Sign Up", "register");
            
            // Add action handlers for login button
            loginBtn.setOnAction(e -> openNewWindow(primaryStage, new LoginUI()));
            
            // Create signup menu
            ContextMenu signupMenu = new ContextMenu();
            MenuItem registerUser = new MenuItem("Register as User");
            MenuItem registerStaff = new MenuItem("Register as Staff");
            
            registerUser.setOnAction(e -> {
                openNewWindow(primaryStage, new RegisterUI());
            });
            
            registerStaff.setOnAction(e -> {
                openNewWindow(primaryStage, new StaffRegisterUI());
            });
            
            signupMenu.getItems().addAll(registerUser, registerStaff);
            signupBtn.setOnAction(e -> signupMenu.show(signupBtn, Side.BOTTOM, 0, 0));
            
            header.getChildren().addAll(logoLabel, spacer, loginBtn, signupBtn);
            
            // Create title section
            VBox titleSection = new VBox(10);
            titleSection.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 40 20;");
            titleSection.setAlignment(Pos.CENTER);
            
            Label titleLabel = new Label("Welcome to Flight Management");
            titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
            titleLabel.setTextFill(Color.rgb(33, 37, 41));
            
            Label subtitleLabel = new Label("Your one-stop solution for flight management");
            subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
            subtitleLabel.setTextFill(Color.rgb(108, 117, 125));
            
            // Create image slider
            StackPane imageSlider = new StackPane();
            imageSlider.setPrefHeight(400);
            imageSlider.setPrefWidth(1200);
            imageSlider.setStyle("-fx-background-color: white;");
            
            ImageView image1 = new ImageView(new Image("https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=1200"));
            ImageView image2 = new ImageView(new Image("https://images.unsplash.com/photo-1464037866556-6812c9d1c72e?w=1200"));
            ImageView image3 = new ImageView(new Image("https://images.unsplash.com/photo-1556388158-158ea5ccacbd?w=1200"));
            
            // Set image properties
            image1.setFitWidth(1200);
            image1.setFitHeight(400);
            image1.setPreserveRatio(false);
            
            image2.setFitWidth(1200);
            image2.setFitHeight(400);
            image2.setPreserveRatio(false);
            
            image3.setFitWidth(1200);
            image3.setFitHeight(400);
            image3.setPreserveRatio(false);
            
            // Add images to slider
            imageSlider.getChildren().addAll(image1, image2, image3);
            
            // Set initial visibility
            image1.setVisible(true);
            image2.setVisible(false);
            image3.setVisible(false);
            
            // Create timeline for auto-sliding
            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            
            // Create fade transitions
            FadeTransition fadeOut1 = new FadeTransition(Duration.seconds(1), image1);
            fadeOut1.setFromValue(1.0);
            fadeOut1.setToValue(0.0);
            
            FadeTransition fadeIn2 = new FadeTransition(Duration.seconds(1), image2);
            fadeIn2.setFromValue(0.0);
            fadeIn2.setToValue(1.0);
            
            FadeTransition fadeOut2 = new FadeTransition(Duration.seconds(1), image2);
            fadeOut2.setFromValue(1.0);
            fadeOut2.setToValue(0.0);
            
            FadeTransition fadeIn3 = new FadeTransition(Duration.seconds(1), image3);
            fadeIn3.setFromValue(0.0);
            fadeIn3.setToValue(1.0);
            
            FadeTransition fadeOut3 = new FadeTransition(Duration.seconds(1), image3);
            fadeOut3.setFromValue(1.0);
            fadeOut3.setToValue(0.0);
            
            FadeTransition fadeIn1 = new FadeTransition(Duration.seconds(1), image1);
            fadeIn1.setFromValue(0.0);
            fadeIn1.setToValue(1.0);
            
            // Add keyframes to timeline
            timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, e -> {
                    image1.setVisible(true);
                    image2.setVisible(false);
                    image3.setVisible(false);
                }),
                new KeyFrame(Duration.seconds(5), e -> {
                    fadeOut1.play();
                    fadeIn2.play();
                    image2.setVisible(true);
                }),
                new KeyFrame(Duration.seconds(10), e -> {
                    fadeOut2.play();
                    fadeIn3.play();
                    image3.setVisible(true);
                }),
                new KeyFrame(Duration.seconds(15), e -> {
                    fadeOut3.play();
                    fadeIn1.play();
                    image1.setVisible(true);
                })
            );
            
            // Start the timeline
            timeline.play();
            
            titleSection.getChildren().addAll(titleLabel, subtitleLabel, imageSlider);
            
            // Create content container
            VBox contentBox = createStyledBox();
            contentBox.setMaxWidth(1000);
            contentBox.setPadding(new Insets(20));
            contentBox.setAlignment(Pos.CENTER);
            
            // Create footer
            HBox footer = new HBox();
            footer.setStyle("-fx-background-color: #212529; -fx-padding: 15;");
            footer.setPrefHeight(50);
            footer.setAlignment(Pos.CENTER);
            
            Label footerText = new Label("© 2024 Flight Management System. All rights reserved.");
            footerText.setTextFill(Color.WHITE);
            footerText.setFont(Font.font("Segoe UI", 14));
            
            footer.getChildren().add(footerText);
            
            // Add all sections to main container
            mainContainer.getChildren().addAll(header, titleSection, contentBox, footer);
            VBox.setVgrow(contentBox, Priority.ALWAYS);
            
            // Set up the scene
            Scene scene = new Scene(mainContainer, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            showError("Error starting application: " + e.getMessage());
        }
    }
    
    private void openNewWindow(Stage currentStage, Application app) {
        try {
            Stage newStage = new Stage();
            app.start(newStage);
            currentStage.close();
        } catch (Exception e) {
            showError("Error opening window: " + e.getMessage());
        }
    }
    
    private Button createStyledButton(String text, String type) {
        Button button = new Button(text);
        String baseStyle = "-fx-font-weight: bold; -fx-font-size: 14; -fx-cursor: hand;";
        
        switch (type) {
            case "header":
                button.setStyle(baseStyle + "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 20;");
                break;
            case "register":
                button.setStyle(baseStyle + "-fx-background-color: #4361ee; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
                break;
            case "exit":
                button.setStyle(baseStyle + "-fx-background-color: #dc3545; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
                break;
            case "login":
                button.setStyle(baseStyle + "-fx-background-color: #4361ee; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
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
        VBox box = new VBox(15);
        box.setStyle("-fx-background-color: white;");
        box.setPadding(new Insets(30));
        
        return box;
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 