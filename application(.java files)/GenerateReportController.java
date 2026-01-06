package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class GenerateReportController {

    @FXML private ComboBox<String> reportTypeComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;
    @FXML private TableView<ReportEntry> reportTable;
    @FXML private TableColumn<ReportEntry, String> categoryColumn;
    @FXML private TableColumn<ReportEntry, String> amountColumn;
    @FXML private Label reportSummaryLabel;

    private int currentUserId = 1; // Default user ID, adjust based on your auth system

    @FXML
    public void initialize() {
        // Initialize report type options
        reportTypeComboBox.getItems().addAll(
            "Expense by Category",
            "Monthly Budget vs Actual",
            "Savings Progress",
            "Yearly Income & Expenses"
        );
        
        // Set default date range (last 30 days)
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(30));
        
        // Initialize table columns
        categoryColumn.setCellValueFactory(data -> data.getValue().categoryProperty());
        amountColumn.setCellValueFactory(data -> data.getValue().amountProperty());
        
        // Initialize chart axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Categories");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount ($)");
        barChart.setTitle("Financial Overview");
    }

    @FXML
    private void handleGenerateReport() {
        String reportType = reportTypeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
        if (reportType == null) {
            showAlert("Error", "Please select a report type");
            return;
        }
        
        if (startDate == null || endDate == null) {
            showAlert("Error", "Please select a date range");
            return;
        }
        
        if (startDate.isAfter(endDate)) {
            showAlert("Error", "Start date must be before end date");
            return;
        }
        
        try {
            switch (reportType) {
                case "Expense by Category":
                    generateExpenseByCategoryReport(startDate, endDate);
                    break;
                case "Monthly Budget vs Actual":
                    generateBudgetVsActualReport(startDate, endDate);
                    break;
                case "Savings Progress":
                    generateSavingsProgressReport(startDate, endDate);
                    break;
                case "Yearly Income & Expenses":
                    generateYearlyIncomeExpenseReport(startDate, endDate);
                    break;
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to generate report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateExpenseByCategoryReport(LocalDate startDate, LocalDate endDate) throws SQLException {
        String query = "SELECT category, SUM(amount) as total " +
                      "FROM Expenses " +
                      "WHERE user_id = ? AND expense_date BETWEEN ? AND ? " +
                      "GROUP BY category " +
                      "ORDER BY total DESC";
        
        ObservableList<ReportEntry> entries = FXCollections.observableArrayList();
        double totalExpenses = 0;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, currentUserId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String category = rs.getString("category");
                double amount = rs.getDouble("total");
                totalExpenses += amount;
                
                entries.add(new ReportEntry(
                    category,
                    String.format("$%.2f", amount)
                ));
            }
        }
        
        updateReportUI(entries, "Expense by Category", 
                      String.format("Total Expenses: $%.2f", totalExpenses));
    }

    private void generateBudgetVsActualReport(LocalDate startDate, LocalDate endDate) throws SQLException {
        String query = "SELECT b.month, b.year, b.total_amount as budget, " +
                      "COALESCE(SUM(e.amount), 0) as expenses " +
                      "FROM Budget b " +
                      "LEFT JOIN Expenses e ON b.user_id = e.user_id " +
                      "AND MONTH(e.expense_date) = MONTH(STR_TO_DATE(CONCAT(b.year, '-', b.month, '-01'), '%Y-%M-%d')) " +
                      "AND e.expense_date BETWEEN ? AND ? " +
                      "WHERE b.user_id = ? " +
                      "AND STR_TO_DATE(CONCAT(b.year, '-', b.month, '-01'), '%Y-%M-%d') BETWEEN ? AND ? " +
                      "GROUP BY b.month, b.year, b.total_amount " +
                      "ORDER BY b.year, MONTH(STR_TO_DATE(b.month, '%M'))";
        
        ObservableList<ReportEntry> entries = FXCollections.observableArrayList();
        double totalBudget = 0;
        double totalExpenses = 0;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setInt(3, currentUserId);
            stmt.setDate(4, Date.valueOf(startDate));
            stmt.setDate(5, Date.valueOf(endDate));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String monthYear = rs.getString("month") + " " + rs.getString("year");
                double budget = rs.getDouble("budget");
                double expenses = rs.getDouble("expenses");
                
                totalBudget += budget;
                totalExpenses += expenses;
                
                entries.add(new ReportEntry(
                    monthYear,
                    String.format("Budget: $%.2f | Actual: $%.2f", budget, expenses)
                ));
            }
        }
        
        updateReportUI(entries, "Budget vs Actual", 
                      String.format("Total Budget: $%.2f | Total Expenses: $%.2f", totalBudget, totalExpenses));
    }

    private void generateSavingsProgressReport(LocalDate startDate, LocalDate endDate) throws SQLException {
        String query = "SELECT saving_date, SUM(amount) as total " +
                      "FROM Savings " +
                      "WHERE user_id = ? AND saving_date BETWEEN ? AND ? " +
                      "GROUP BY saving_date " +
                      "ORDER BY saving_date";
        
        ObservableList<ReportEntry> entries = FXCollections.observableArrayList();
        double totalSavings = 0;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, currentUserId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String date = rs.getDate("saving_date").toLocalDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
                double amount = rs.getDouble("total");
                totalSavings += amount;
                
                entries.add(new ReportEntry(
                    date,
                    String.format("$%.2f", amount)
                ));
            }
        }
        
        updateReportUI(entries, "Savings Progress", 
                      String.format("Total Savings: $%.2f", totalSavings));
    }

    private void generateYearlyIncomeExpenseReport(LocalDate startDate, LocalDate endDate) throws SQLException {
        String expenseQuery = "SELECT YEAR(expense_date) as year, SUM(amount) as expenses " +
                            "FROM Expenses " +
                            "WHERE user_id = ? AND expense_date BETWEEN ? AND ? " +
                            "GROUP BY YEAR(expense_date) " +
                            "ORDER BY year";
        
        String savingsQuery = "SELECT YEAR(saving_date) as year, SUM(amount) as savings " +
                            "FROM Savings " +
                            "WHERE user_id = ? AND saving_date BETWEEN ? AND ? " +
                            "GROUP BY YEAR(saving_date) " +
                            "ORDER BY year";
        
        ObservableList<ReportEntry> entries = FXCollections.observableArrayList();
        Map<Integer, YearlyData> yearlyData = new HashMap<>();
        
        // Get expenses
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(expenseQuery)) {
            
            stmt.setInt(1, currentUserId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int year = rs.getInt("year");
                YearlyData data = yearlyData.getOrDefault(year, new YearlyData(year));
                data.expenses = rs.getDouble("expenses");
                yearlyData.put(year, data);
            }
        }
        
        // Get savings (as income)
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(savingsQuery)) {
            
            stmt.setInt(1, currentUserId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int year = rs.getInt("year");
                YearlyData data = yearlyData.getOrDefault(year, new YearlyData(year));
                data.savings = rs.getDouble("savings");
                yearlyData.put(year, data);
            }
        }
        
        // Prepare entries
        double totalIncome = 0;
        double totalExpenses = 0;
        
        for (YearlyData data : yearlyData.values()) {
            entries.add(new ReportEntry(
                String.valueOf(data.year),
                String.format("Income: $%.2f | Expenses: $%.2f", data.savings, data.expenses)
            ));
            
            totalIncome += data.savings;
            totalExpenses += data.expenses;
        }
        
        updateReportUI(entries, "Yearly Income & Expenses", 
                      String.format("Total Income: $%.2f | Total Expenses: $%.2f", totalIncome, totalExpenses));
    }

    private void updateReportUI(ObservableList<ReportEntry> entries, String title, String summary) {
        // Update table
        reportTable.setItems(entries);
        
        // Update summary
        reportSummaryLabel.setText(summary);
        
        // Update bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(title);
        
        for (ReportEntry entry : entries) {
            String amountStr = entry.getAmount().replaceAll("[^\\d.]", "");
            try {
                double amount = Double.parseDouble(amountStr);
                series.getData().add(new XYChart.Data<>(entry.getCategory(), amount));
            } catch (NumberFormatException e) {
                // Skip entries with complex amount strings
            }
        }
        
        barChart.getData().clear();
        barChart.getData().add(series);
        barChart.setTitle(title);
        
        // Update pie chart (for single-value entries)
        if (entries.stream().allMatch(e -> e.getAmount().startsWith("$"))) {
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            for (ReportEntry entry : entries) {
                String amountStr = entry.getAmount().replace("$", "");
                double amount = Double.parseDouble(amountStr);
                pieData.add(new PieChart.Data(entry.getCategory(), amount));
            }
            pieChart.setData(pieData);
            pieChart.setTitle(title + " Distribution");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class ReportEntry {
        private final StringProperty category;
        private final StringProperty amount;

        public ReportEntry(String category, String amount) {
            this.category = new SimpleStringProperty(category);
            this.amount = new SimpleStringProperty(amount);
        }

        public String getCategory() { return category.get(); }
        public String getAmount() { return amount.get(); }
        public StringProperty categoryProperty() { return category; }
        public StringProperty amountProperty() { return amount; }
    }
    
    private static class YearlyData {
        int year;
        double savings;
        double expenses;
        
        YearlyData(int year) {
            this.year = year;
        }
    }
}