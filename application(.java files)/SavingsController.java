package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import java.sql.*;
import java.time.LocalDate;

public class SavingsController {

    @FXML private TextField savingsAmountField;
    @FXML private TableView<SavingRecord> savingsTable;
    @FXML private TableColumn<SavingRecord, String> dateColumn;
    @FXML private TableColumn<SavingRecord, String> amountColumn;

    private ObservableList<SavingRecord> savingsList = FXCollections.observableArrayList();
    private int currentUserId = 1; // Default user ID, adjust based on your auth system

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());
        amountColumn.setCellValueFactory(data -> data.getValue().amountProperty());
        savingsTable.setItems(savingsList);
        
        // Load existing savings from database
        loadSavingsFromDatabase();
    }

    @FXML
    public void handleAddSaving() {
        String amountText = savingsAmountField.getText();
        
        if (amountText.isEmpty()) {
            showAlert("Error", "Please enter an amount");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert("Error", "Amount must be greater than 0");
                return;
            }
            
            LocalDate currentDate = LocalDate.now();
            
            // Save to database
            if (saveToDatabase(amount, currentDate)) {
                // Add to table if save was successful
                savingsList.add(new SavingRecord(
                    currentDate.toString(), 
                    String.format("$%.2f", amount)
                ));
                savingsAmountField.clear();
            } else {
                showAlert("Error", "Failed to save the record");
            }
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean saveToDatabase(double amount, LocalDate date) throws SQLException {
        String query = "INSERT INTO Savings (user_id, amount, saving_date) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, currentUserId);
            stmt.setDouble(2, amount);
            stmt.setDate(3, Date.valueOf(date));
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private void loadSavingsFromDatabase() {
        String query = "SELECT amount, saving_date FROM Savings WHERE user_id = ? ORDER BY saving_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                double amount = rs.getDouble("amount");
                LocalDate date = rs.getDate("saving_date").toLocalDate();
                
                savingsList.add(new SavingRecord(
                    date.toString(),
                    String.format("$%.2f", amount)
                ));
            }
            
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load savings: " + e.getMessage());
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