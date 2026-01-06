package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.*;

public class ManageProfileController {

    @FXML private Label nameLabel, emailLabel, phoneLabel;
    @FXML private ImageView profileImage;
    @FXML private TextField nameField, emailField, phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Button saveProfileButton;

    private int userId = 1; // You can make this dynamic later after login

    @FXML
    private void initialize() {
        loadUserProfile();
        profileImage.setImage(new Image(getClass().getResourceAsStream("/avatar.png")));
    }

    private void loadUserProfile() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM User WHERE UserID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");

                nameLabel.setText(name);
                emailLabel.setText(email);
                phoneLabel.setText(phone);

                nameField.setText(name);
                emailField.setText(email);
                phoneField.setText(phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load user profile.");
        }
    }

    @FXML
    private void handleSaveProfile() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Name, email, and password cannot be empty.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE User SET Name = ?, Email = ?, Phone = ?, Password = ? WHERE UserID = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, password); // Ideally hash it
            stmt.setInt(5, userId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                nameLabel.setText(name);
                emailLabel.setText(email);
                phoneLabel.setText(phone);
                showAlert("Profile Updated", "Your profile has been updated successfully.");
            } else {
                showAlert("Update Failed", "No changes were made.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update profile.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
