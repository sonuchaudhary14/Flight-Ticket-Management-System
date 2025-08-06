package pkg1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class About extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Title
        Label title = new Label("✈ About the Flight Ticket Management System");
        title.setFont(new Font("Arial", 30));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow());

        // Content Text
        Label content = new Label("The Flight Ticket Management System allows users to book flights with ease and comfort. "
                + "This system provides the following features:\n\n"
                + "- Search for available flights\n"
                + "- Book tickets securely\n"
                + "- Manage booking details\n"
                + "- View past bookings\n"
                + "- Easy-to-use interface\n\n"
                + "Our mission is to simplify flight booking and ensure a seamless travel experience for all users.");
        content.setFont(new Font("Arial", 16));
        content.setTextFill(Color.LIGHTGRAY);
        content.setWrapText(true);
        content.setMaxWidth(600);

        // Layout for the page
        VBox layout = new VBox(20, title, content);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 20; -fx-background-radius: 15;");

        // Scene and Stage setup
        StackPane root = new StackPane(layout);
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("About - Flight Ticket Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
