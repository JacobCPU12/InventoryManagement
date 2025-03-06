package inventory_management;

import javafx.beans.property.*;

public class GUI_Items {
    private final IntegerProperty id;
    private final StringProperty description;
    private final DoubleProperty unitPrice;
    private final IntegerProperty qtyInStock;
    private final DoubleProperty totalPrice;

    public GUI_Items(int id, String description, double unitPrice, int qtyInStock, double totalPrice) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.qtyInStock = new SimpleIntegerProperty(qtyInStock);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
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

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public int getQtyInStock() {
        return qtyInStock.get();
    }

    public void setQtyInStock(int qtyInStock) {
        this.qtyInStock.set(qtyInStock);
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice.set(totalPrice);
    }
    
    // Property methods for binding
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public IntegerProperty qtyInStockProperty() {
        return qtyInStock;
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }
}

