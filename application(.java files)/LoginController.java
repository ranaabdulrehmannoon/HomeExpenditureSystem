package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label signupLabel;
    @FXML private Label forgetPasswordLabel;

    @FXML
    public void initialize() {
        // Set up click handlers for the labels
        forgetPasswordLabel.setOnMouseClicked(event -> handleForgotPassword());
        signupLabel.setOnMouseClicked(event -> handleSignUp());
    }

    @FXML
    private void login(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM User WHERE Email = ? AND Password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Login successful
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
                Scene dashboardScene = new Scene(loader.load());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.setTitle("Dashboard");
                stage.show();
            } else {
                showAlert("Error", "Invalid email or password.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Login error: " + e.getMessage());
        }
    }

    private void handleForgotPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForgetPassword.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Password Recovery");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open password recovery window: " + e.getMessage());
        }
    }

 // Add this method to your existing LoginController
    private void handleSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up");
            stage.show();
            
            // Close current login window if desired
            // ((Stage) signupLabel.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open sign up window: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}