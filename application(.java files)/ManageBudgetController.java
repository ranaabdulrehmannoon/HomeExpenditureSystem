package application;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ManageBudgetController {

    @FXML private Label currentBudgetLabel;
    @FXML private Label lastUpdatedLabel;
    @FXML private PieChart budgetPieChart;
    @FXML private TextField budgetAmountField;
    @FXML private ComboBox<Month> monthComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private TableView<BudgetEntry> budgetHistoryTable;
    @FXML private TableColumn<BudgetEntry, String> monthColumn;
    @FXML private TableColumn<BudgetEntry, Integer> yearColumn;
    @FXML private TableColumn<BudgetEntry, Double> amountColumn;
    @FXML private TableColumn<BudgetEntry, String> dateColumn;

    private double currentBudget = 0.0;
    private ObservableList<BudgetEntry> budgetHistory = FXCollections.observableArrayList();
    private Map<String, Double> budgetCategories = new HashMap<>();
    private int currentUserId = 1; // Default user ID, you can change this based on your auth system

    @FXML
    public void initialize() {
        try {
            // Load current budget from database
            loadCurrentBudget();
            
            // Initialize budget categories (would normally come from database)
            budgetCategories.put("Housing", 600.00);
            budgetCategories.put("Food", 300.00);
            budgetCategories.put("Transportation", 200.00);
            budgetCategories.put("Utilities", 150.00);
            budgetCategories.put("Entertainment", 100.00);
            budgetCategories.put("Other", 150.00);

            // Set up current budget display
            currentBudgetLabel.setText(String.format("$%.2f", currentBudget));
            lastUpdatedLabel.setText("Last updated: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

            // Set up pie chart
            updatePieChart();

            // Set up month and year combo boxes
            monthComboBox.setItems(FXCollections.observableArrayList(Month.values()));
            monthComboBox.getSelectionModel().select(LocalDate.now().getMonth());
            
            ObservableList<Integer> years = FXCollections.observableArrayList();
            int currentYear = Year.now().getValue();
            for (int year = currentYear - 5; year <= currentYear + 5; year++) {
                years.add(year);
            }
            yearComboBox.setItems(years);
            yearComboBox.getSelectionModel().select(Integer.valueOf(currentYear));

            // Set up budget history table
            monthColumn.setCellValueFactory(cellData -> cellData.getValue().monthProperty());
            yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
            amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
            dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

            // Load budget history from database
            loadBudgetHistory();

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load budget data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadCurrentBudget() throws SQLException {
        String query = "SELECT total_amount, date_updated FROM Budget WHERE user_id = ? " +
                       "ORDER BY year DESC, month DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                currentBudget = rs.getDouble("total_amount");
                LocalDate updatedDate = rs.getDate("date_updated").toLocalDate();
                lastUpdatedLabel.setText("Last updated: " + updatedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            } else {
                currentBudget = 0.0;
                lastUpdatedLabel.setText("No budget set yet");
            }
        }
    }

    private void loadBudgetHistory() throws SQLException {
        budgetHistory.clear();
        
        String query = "SELECT month, year, total_amount, date_updated FROM Budget " +
                      "WHERE user_id = ? ORDER BY year DESC, month DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String month = rs.getString("month");
                int year = rs.getInt("year");
                double amount = rs.getDouble("total_amount");
                LocalDate date = rs.getDate("date_updated").toLocalDate();
                
                budgetHistory.add(new BudgetEntry(
                    month, 
                    year, 
                    amount, 
                    date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                ));
            }
        }
        
        budgetHistoryTable.setItems(budgetHistory);
    }

    private void updatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        budgetCategories.forEach((category, amount) -> {
            pieChartData.add(new PieChart.Data(category + " ($" + String.format("%.2f", amount) + ")", amount));
        });
        budgetPieChart.setData(pieChartData);
        budgetPieChart.setTitle("Budget Allocation");
    }

    @FXML
    private void handleUpdateBudget() {
        try {
            double newBudget = Double.parseDouble(budgetAmountField.getText());
            if (newBudget <= 0) {
                showAlert("Invalid Amount", "Budget amount must be greater than 0");
                return;
            }

            Month month = monthComboBox.getValue();
            int year = yearComboBox.getValue();
            
            // Update database
            saveBudgetToDatabase(month.toString(), year, newBudget);
            
            // Update UI
            currentBudget = newBudget;
            currentBudgetLabel.setText(String.format("$%.2f", currentBudget));
            lastUpdatedLabel.setText("Last updated: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

            // Refresh history
            loadBudgetHistory();

            // Clear input field
            budgetAmountField.clear();

            showAlert("Success", "Budget updated successfully!");

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for the budget amount");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save budget: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveBudgetToDatabase(String month, int year, double amount) throws SQLException {
        String checkQuery = "SELECT budget_id FROM Budget WHERE user_id = ? AND month = ? AND year = ?";
        String insertQuery = "INSERT INTO Budget (user_id, month, year, total_amount, date_updated) " +
                            "VALUES (?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE Budget SET total_amount = ?, date_updated = ? " +
                            "WHERE user_id = ? AND month = ? AND year = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if budget entry already exists for this month/year
            boolean exists = false;
            int budgetId = -1;
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, currentUserId);
                checkStmt.setString(2, month);
                checkStmt.setInt(3, year);
                
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    exists = true;
                    budgetId = rs.getInt("budget_id");
                }
            }
            
            // Insert or update accordingly
            if (exists) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setDouble(1, amount);
                    updateStmt.setDate(2, Date.valueOf(LocalDate.now()));
                    updateStmt.setInt(3, currentUserId);
                    updateStmt.setString(4, month);
                    updateStmt.setInt(5, year);
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, currentUserId);
                    insertStmt.setString(2, month);
                    insertStmt.setInt(3, year);
                    insertStmt.setDouble(4, amount);
                    insertStmt.setDate(5, Date.valueOf(LocalDate.now()));
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for budget history entries
    public static class BudgetEntry {
        private final StringProperty month;
        private final IntegerProperty year;
        private final DoubleProperty amount;
        private final StringProperty date;

        public BudgetEntry(String month, int year, double amount, String date) {
            this.month = new SimpleStringProperty(month);
            this.year = new SimpleIntegerProperty(year);
            this.amount = new SimpleDoubleProperty(amount);
            this.date = new SimpleStringProperty(date);
        }

        // Property getters
        public StringProperty monthProperty() { return month; }
        public IntegerProperty yearProperty() { return year; }
        public DoubleProperty amountProperty() { return amount; }
        public StringProperty dateProperty() { return date; }

        // Regular getters
        public String getMonth() { return month.get(); }
        public int getYear() { return year.get(); }
        public double getAmount() { return amount.get(); }
        public String getDate() { return date.get(); }
    }
}