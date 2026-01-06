package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ForgetPasswordController {

    @FXML private TextField emailField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button resetPasswordButton;

    @FXML
    private void handleResetPassword() {
        String email = emailField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate inputs
        if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match");
            return;
        }

        // Verify email exists
        if (!emailExists(email)) {
            showAlert("Error", "Email not found in our system");
            return;
        }

        // Update password in database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE User SET Password = ? WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showAlert("Success", "Password reset successfully");
                closeWindow();
            } else {
                showAlert("Error", "Failed to reset password");
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

    private void closeWindow() {
        Stage stage = (Stage) resetPasswordButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}