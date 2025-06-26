package application;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DashboardController {
    @FXML private StackPane contentPane;
    @FXML private Label monthlyBudgetLabel;
    @FXML private Label monthlyExpenseLabel;
    @FXML private Label totalSavingsLabel;
    @FXML private Pane dashboardHeader; // New reference to the header pane

    // Store the initial dashboard content nodes so we can restore them on Dashboard button press
    private Node[] originalDashboardContent;

    @FXML
    public void initialize() {
        // Verify all FXML injections worked
        if (monthlyBudgetLabel == null || monthlyExpenseLabel == null || totalSavingsLabel == null || dashboardHeader == null || contentPane == null) {
            showAlert("Initialization Error", "FXML injection failed");
            return;
        }

        // Save the original dashboard content nodes for reuse
        originalDashboardContent = contentPane.getChildren().toArray(new Node[0]);

        loadStatistics();
    }

    private void loadStatistics() {
        monthlyBudgetLabel.setText("$1,500.00");
        monthlyExpenseLabel.setText("$1,200.00");
        totalSavingsLabel.setText("$300.00");
    }

    @FXML
    private void handleManageBudget() {
        dashboardHeader.setVisible(false); // Hide header for other views
        loadContent("/ManageBudget.fxml");
    }

    @FXML
    private void handleManageExpenses() {
        dashboardHeader.setVisible(false);
        loadContent("/ManageExpenses.fxml");
    }

    @FXML
    private void handleSavings() {
        dashboardHeader.setVisible(false);
        loadContent("/Savings.fxml");
    }

    @FXML
    private void handleManageProfile() {
        dashboardHeader.setVisible(false);
        loadContent("/ManageProfile.fxml");
    }

    @FXML
    private void handleExpenseReports() {
        dashboardHeader.setVisible(false);
        loadContent("/ExpenseReports.fxml");
    }

    @FXML
    private void handleGenerateReport() {
        dashboardHeader.setVisible(false);
        loadContent("/GenerateReport.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
        } catch (IOException e) {
            showAlert("Error", "Cannot logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // New method for Dashboard button to restore original content
    @FXML
    private void handleDashboard() {
        dashboardHeader.setVisible(true);  // Show the header

        // Clear current content
        contentPane.getChildren().clear();

        // Restore original dashboard content nodes
        contentPane.getChildren().addAll(originalDashboardContent);
    }

    private void loadContent(String fxmlPath) {
        try {
            if (contentPane == null) {
                throw new IllegalStateException("Content pane is not initialized");
            }

            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                throw new IOException("Cannot find resource: " + fxmlPath);
            }

            Parent content = FXMLLoader.load(resource);
            contentPane.getChildren().clear();
            contentPane.getChildren().add(content);
        } catch (IOException e) {
            showAlert("Error", "Cannot load content: " + e.getMessage());
            e.printStackTrace();
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
