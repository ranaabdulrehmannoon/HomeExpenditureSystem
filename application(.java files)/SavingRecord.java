package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SavingRecord {
    private final StringProperty date;
    private final StringProperty amount;

    public SavingRecord(String date, String amount) {
        this.date = new SimpleStringProperty(date);
        this.amount = new SimpleStringProperty(amount);
    }

    public String getDate() {
        return date.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty amountProperty() {
        return amount;
    }
}