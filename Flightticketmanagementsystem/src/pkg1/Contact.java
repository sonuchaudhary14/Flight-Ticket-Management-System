package pkg1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Contact extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("This is the Contact Page.");
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Contact Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
