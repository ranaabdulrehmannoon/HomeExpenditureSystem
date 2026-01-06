package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ManageExpensesController {

    @FXML private VBox formBox;
    @FXML private Label selectedCategoryLabel;
    @FXML private TextField expenseAmountField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private DatePicker expenseDatePicker;

    private String selectedCategory = "";
    private int currentUserId = 1; // Default user ID, adjust based on your auth system
    
    // Map to store category-type relationships (could be loaded from database)
    private final Map<String, String[]> categoryTypes = new HashMap<>();

    @FXML
    public void initialize() {
        // Initialize category-type relationships
        initializeCategoryTypes();
        
        // Set default date to today
        expenseDatePicker.setValue(LocalDate.now());
    }

    private void initializeCategoryTypes() {
        categoryTypes.put("Insurance", new String[]{"Health", "Vehicle", "Life"});
        categoryTypes.put("Utility", new String[]{"Electricity", "Gas", "Water", "Internet"});
        categoryTypes.put("Education", new String[]{"School", "College", "University"});
        categoryTypes.put("Medical", new String[]{"Consultation", "Surgery", "Medication"});
        categoryTypes.put("Transport", new String[]{"Bus", "Taxi", "Fuel", "Train"});
        categoryTypes.put("Clothing", new String[]{"Formal", "Casual", "Seasonal"});
        categoryTypes.put("Entertainment", new String[]{"Movie", "Games", "Outdoor Activity"});
        categoryTypes.put("Miscellaneous", new String[]{"Gifts", "Donations", "Unexpected"});
        categoryTypes.put("Rent", new String[]{"Home", "Office", "Shop"});
    }

    @FXML
    private void handleCategorySelection(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        selectedCategory = clickedButton.getText();
        selectedCategoryLabel.setText("Selected Category: " + selectedCategory);

        // Show the form
        formBox.setVisible(true);
        formBox.setManaged(true);

        // Populate type options based on category
        typeComboBox.getItems().clear();
        if (categoryTypes.containsKey(selectedCategory)) {
            typeComboBox.getItems().addAll(categoryTypes.get(selectedCategory));
        }
    }

    @FXML
    private void handleAddExpense() {
        try {
            String amountText = expenseAmountField.getText();
            String description = descriptionField.getText();
            LocalDate date = expenseDatePicker.getValue();
            String type = typeComboBox.getValue();

            // Validate inputs
            if (amountText.isEmpty() || description.isEmpty() || date == null || type == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    showAlert("Error", "Amount must be greater than 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid amount.");
                return;
            }

            // Save to database
            if (saveExpenseToDatabase(selectedCategory, type, amount, description, date)) {
                showAlert("Success", "Expense added successfully!");
                clearFields();
            } else {
                showAlert("Error", "Failed to save expense. Please try again.");
            }

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save expense: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean saveExpenseToDatabase(String category, String type, double amount, 
                                       String description, LocalDate date) throws SQLException {
        String query = "INSERT INTO Expenses (user_id, category, type, amount, description, expense_date) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, currentUserId);
            stmt.setString(2, category);
            stmt.setString(3, type);
            stmt.setDouble(4, amount);
            stmt.setString(5, description);
            stmt.setDate(6, Date.valueOf(date));
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private void clearFields() {
        expenseAmountField.clear();
        descriptionField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        expenseDatePicker.setValue(LocalDate.now());
        formBox.setVisible(false);
        formBox.setManaged(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}