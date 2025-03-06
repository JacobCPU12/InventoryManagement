package inventory_management;

import javafx.beans.property.*;
import java.sql.Date;

public class GUI_Transaction {
    private final IntegerProperty id;
    private final StringProperty description;
    private final IntegerProperty qtySold;
    private final DoubleProperty amount;
    private final IntegerProperty stockRemaining;
    private final StringProperty transactionType;
    private final StringProperty date;  // Now store date as String

    public GUI_Transaction(int id, String description, int qtySold, double amount, int stockRemaining, String transactionType, String dateString) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.qtySold = new SimpleIntegerProperty(qtySold);
        this.amount = new SimpleDoubleProperty(amount);
        this.stockRemaining = new SimpleIntegerProperty(stockRemaining);
        this.transactionType = new SimpleStringProperty(transactionType);
        this.date = new SimpleStringProperty(dateString);
    }

    // Getter and setter methods
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public int getQtySold() {
        return qtySold.get();
    }

    public void setQtySold(int qtySold) {
        this.qtySold.set(qtySold);
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public int getStockRemaining() {
        return stockRemaining.get();
    }

    public void setStockRemaining(int stockRemaining) {
        this.stockRemaining.set(stockRemaining);
    }

    public String getTransactionType() {
        return transactionType.get();
    }

    public void setTransactionType(String transactionType) {
        this.transactionType.set(transactionType);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    // Property methods for binding
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public IntegerProperty qtySoldProperty() {
        return qtySold;
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public IntegerProperty stockRemainingProperty() {
        return stockRemaining;
    }

    public StringProperty transactionTypeProperty() {
        return transactionType;
    }

    public StringProperty dateProperty() {
        return date;
    }
}

