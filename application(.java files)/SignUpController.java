package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;
    @FXML private Label signInLabel;

    @FXML
    public void initialize() {
        // Set up click handler for sign in label
        signInLabel.setOnMouseClicked(event -> handleSignIn());
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }

        if (password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters");
            return;
        }

        // Check if email already exists
        if (emailExists(email)) {
            showAlert("Error", "Email already registered");
            return;
        }

        // Create new user
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO User (Name, Email, Phone, Password) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, password);

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showAlert("Success", "Account created successfully");
                handleSignIn(); // Redirect to login after successful registration
            } else {
                showAlert("Error", "Failed to create account");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private boolean emailExists(String email) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT Email FROM User WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleSignIn() {
        try {
            // Close current window
            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
            
            // Open login window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open login window: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}