package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the login screen first
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Scene scene = new Scene(root);
            Image appIcon = new Image(getClass().getResourceAsStream("/logo.jpg"));
            primaryStage.getIcons().add(appIcon);
            // Set up the primary stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Home Expenditure System");
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}