package inventory_management;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class InvMgmt_GUIController {
	@FXML
	private AnchorPane transactionTable;
	@FXML
	private TableView<GUI_Items> itemsTable;
	@FXML
	private TableColumn<GUI_Items, Integer> itemsItemIDCol;
	@FXML
	private TableColumn<GUI_Items, String> itemsDescCol;
	@FXML
	private TableColumn<GUI_Items, Double> itemsUnitPriceCol;
	@FXML
	private TableColumn<GUI_Items, Integer> itemsQtyCol;
	@FXML
	private TableColumn<GUI_Items, Double> itemsTotalPriceCol;
	@FXML
    private TableView<GUI_Transaction> transactionsTable;  // Set type to GUI_Transaction
    @FXML
    private TableColumn<GUI_Transaction, Integer> transactionsIDCol;  // Set type to GUI_Transaction
    @FXML
    private TableColumn<GUI_Transaction, String> transactionsDescCol;
    @FXML
    private TableColumn<GUI_Transaction, Integer> transactionsQtySoldCol;
    @FXML
    private TableColumn<GUI_Transaction, Double> transactionsAmountCol;
    @FXML
    private TableColumn<GUI_Transaction, Integer> transactionsStockCol;
    @FXML
    private TableColumn<GUI_Transaction, String> transactionsTypeCol;
    @FXML
    private TableColumn<GUI_Transaction, String> transactionsDateCol;
	@FXML
	private Label updateLbl;
	@FXML
	private Label newLbl;
	@FXML
	private TextField newDescTxt;
	@FXML
	private Label newDescLbl;
	@FXML
	private Label newPriceLbl;
	@FXML
	private Label newQtyLbl;
	@FXML
	private TextField newPriceTxt;
	@FXML
	private TextField newQtyTxt;
	@FXML
	private Label currencyLbl;
	@FXML
	private Button newItemBtn;
	@FXML
	private TextField updItemIDTxt;
	@FXML
	private Label updDescLbl;
	@FXML
	private Label updQtyLbl;
	@FXML
	private TextField updQtyTxt;
	@FXML
	private Button updItemBtn;
	@FXML
	private Label deleteItemLbl;
	@FXML
	private TextField delItemIDTxt;
	@FXML
	private Label delItemIDLbl;
	@FXML
	private Button delItemBtn;
	@FXML
	private Label searchLbl;
	@FXML
	private TextField searchBox;
	@FXML
	private Button searchBtn;
	@FXML
	private Label msgLabel;
	
	// User's selection set as a variable, must be declared first
	static boolean saleSelection;
	// Variable set as to whether or not the user selects Yes or No
	static boolean delSelection;

	// Event Listener on Button[#updItemBtn].onAction
	@FXML
	public void updItemAction(ActionEvent event) throws InterruptedException {
		// Define the variables we'll need for the SQL statement
		int itemID = Integer.parseInt(updItemIDTxt.getText());
		int newQuantity = Integer.parseInt(updQtyTxt.getText());
		
		// Before any SQL takes place, we next need to ask the user
		// if this transaction was part of a sale or not?
		
		// This has already been written below in the "resultMsgBox" class.
		resultMsgBox(2);
		
		// Now we've established if a sale is occurring or not:
		// we can now update both the items and transactions table
		if (saleSelection == true) {
			UpdateQuantity.updateQuantity("graphical", itemID, newQuantity, true);
		} else {
			UpdateQuantity.updateQuantity("graphical", itemID, newQuantity, false);
		}
		
		// This refers to the below class that refreshes the Transactions table inside of the GUI
		// and also the items table.
		reloadTransactionsTableData();
		reloadItemsTableData();
	}
	// Event Listener on Button[#delItemBtn].onAction
	@FXML
	public void delItemAction(ActionEvent event) throws InterruptedException {
		// Declare variables that we'll need for SQL
		int delItemID = Integer.parseInt(delItemIDTxt.getText());
		
		// Before deleting the item from the database,
		// we will first confirm with the user if they want to delete the item?
		// as this action is permanent.
		resultMsgBox(4);
		
		// if delSelection is true (the user has chosen to delete the item):
		// the database will receive a query to delete it.
		// If not, then the message box will close with no action taken.
		if (delSelection == true) {
			DeleteItem.deleteItem("graphical", delItemID);
		} else {
			// a message box is shown here, simply telling the user the item wasn't deleted.
			resultMsgBox(5);
		}
		
		// This refers to the below class that refreshes the Transactions table inside of the GUI
		// and also the items table.
		reloadTransactionsTableData();
		reloadItemsTableData();
	}
	
	// Event Listener on Button[#newItemBtn].onAction
	@FXML
	public void newItemAction(ActionEvent event) throws InterruptedException {
		// Define variables we'll need for SQL
		String productName = newDescTxt.getText();
		double unitPrice = Double.parseDouble(newPriceTxt.getText());
		int itemQuantity = Integer.parseInt(newQtyTxt.getText());
		
		// Now we've defined all the variables we'll need,
		// call to the NewItem class, but define "graphical",
		// so the code knows that the name, quantity, price and total price are already defined.
		NewItem.createNewItem("graphical", productName, unitPrice, itemQuantity);
		
		// This refers to the below class that refreshes the Transactions table inside of the GUI
		// and also the items table.
		reloadTransactionsTableData();
		reloadItemsTableData();
	}
	
	// Event Listener on Button[#searchBtn].onAction
	@FXML
	public void searchAction(ActionEvent event) {
	    // Define the variables we'll need for the SQL statement
	    String searchQuery = searchBox.getText();
	    System.out.println("[INFO] Search triggered with query: '" + searchQuery + "'");

	    // If there is nothing inside the search box
	    // clear the table and show all items instead.
	    if (searchQuery.isEmpty()) {
	        System.out.println("[INFO] Search box empty. Reloading all items.");
	        reloadItemsTableData();  // Reload all items from the database
	    } else {
	        itemsData.clear();  // Clear the existing data before adding new data from the search
	        String url = "jdbc:sqlite:InvtMgmt.db";

	        try (Connection connection = DriverManager.getConnection(url)) {
	            String query = "SELECT id, description, unitPrice, qtyInStock, totalPrice FROM items WHERE description LIKE ?";
	            
	            PreparedStatement ps = connection.prepareStatement(query);
	            ps.setString(1, "%" + searchQuery + "%");
	            System.out.println("[INFO] Executing query: " + query.replace("?", "'%" + searchQuery + "%'"));

	            ResultSet resultSet = ps.executeQuery();

	            int count = 0;
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String description = resultSet.getString("description");
	                double unitPrice = resultSet.getDouble("unitPrice");
	                int qtyInStock = resultSet.getInt("qtyInStock");
	                double totalPrice = resultSet.getDouble("totalPrice");

	                itemsData.add(new GUI_Items(id, description, unitPrice, qtyInStock, totalPrice));
	                System.out.println("[INFO] Found Item -> ID: " + id + ", Description: " + description + 
	                                   ", Unit Price: " + unitPrice + ", Quantity: " + qtyInStock + ", Total Price: " + totalPrice);
	                count++;
	            }

	            if (count == 0) {
	                System.out.println("[INFO] No items found matching query: '" + searchQuery + "'");
	            } else {
	                System.out.println("[INFO] Total " + count + " item(s) found matching query.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        itemsTable.setItems(itemsData);  // Update the TableView with the filtered data
	    }
	    
	    // Reload the transactions table to ensure it's also updated
	    reloadTransactionsTableData();
	}

	
	// This method controls when the message dialogue boxes appear
	// These indicate to the user using the GUI, whether or not their action
	// was successful.
	public static void resultMsgBox(int selection) {
		switch (selection) {
			// This is displayed when a item/product has been added.
			case 1:
				Alert addedAlert = new Alert(AlertType.INFORMATION);
				addedAlert.setTitle("Product added");
				addedAlert.setHeaderText("Product Added");
				addedAlert.setContentText("The product has successfully been added!");
				addedAlert.showAndWait().ifPresent(rs -> {
				    if (rs == ButtonType.OK) {
				        System.out.println("Pressed OK.");
				    }
				});
				break;
			// This is displayed when the quantity has been updated.
			// It has been written differently, as we need to confirm whether or not
			// this was updated due to a sale being commenced.
			case 2:
				Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
				confirmationAlert.setTitle("Confirm Update");
				confirmationAlert.setHeaderText("Update Product Quantity");
				confirmationAlert.setContentText("Is this being updated because of a sale?");
				
				// Set custom button types
				ButtonType yesButton = new ButtonType("Yes");
				ButtonType noButton = new ButtonType("No");

				confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

				// Show alert and wait for response
				Optional<ButtonType> result = confirmationAlert.showAndWait();

				if (result.isPresent() && result.get() == yesButton) {
					// println purely for debugging,
					// the result is passed through to a variable
				    System.out.println("User chose YES.");
				    saleSelection = true;
				} else {
				    System.out.println("User chose NO or closed the dialog.");
				    saleSelection = false;
				}
				break;
			// This is displayed when the item has been deleted.
			case 3:
				Alert deletedAlert = new Alert(AlertType.INFORMATION);
				deletedAlert.setTitle("Product deleted");
				deletedAlert.setHeaderText("Product deleted");
				deletedAlert.setContentText("The product/item has been deleted!");
				deletedAlert.showAndWait().ifPresent(rs -> {
				    if (rs == ButtonType.OK) {
				        System.out.println("Pressed OK.");
				    }
				});
			case 4:
				Alert deleteConfirmAlert = new Alert(AlertType.WARNING);
				deleteConfirmAlert.setTitle("Product deleted");
				deleteConfirmAlert.setHeaderText("Deletion Confirmation");
				deleteConfirmAlert.setContentText("Are you sure you want to delete this item?");
				// Set custom button types
				ButtonType delYesButton = new ButtonType("Yes");
				ButtonType delNoButton = new ButtonType("No");

				deleteConfirmAlert.getButtonTypes().setAll(delYesButton, delNoButton);

				// Show alert and wait for response
				Optional<ButtonType> result2 = deleteConfirmAlert.showAndWait();

				if (result2.isPresent() && result2.get() == delYesButton) {
					// println purely for debugging,
					// the result is passed through to a variable
				    System.out.println("User chose YES.");
				    delSelection = true;
				} else {
				    System.out.println("User chose NO or closed the dialogue.");
				    delSelection = false;
				}
			// This shouldn't need to be here since a number is always provided when called
			// but is here just in case a number isn't provided.
			default:
				System.out.println();
				break;
		}
	}
	
    private ObservableList<GUI_Transaction> transactionsData;
    private ObservableList<GUI_Items> itemsData;

    public void initialize() {
        // Initialise the columns
        transactionsIDCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        transactionsDescCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        transactionsQtySoldCol.setCellValueFactory(cellData -> cellData.getValue().qtySoldProperty().asObject());
        transactionsAmountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        transactionsStockCol.setCellValueFactory(cellData -> cellData.getValue().stockRemainingProperty().asObject());
        transactionsTypeCol.setCellValueFactory(cellData -> cellData.getValue().transactionTypeProperty());
        transactionsDateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        
        // Initialise items table columns
        itemsItemIDCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        itemsDescCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        itemsUnitPriceCol.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty().asObject());
        itemsQtyCol.setCellValueFactory(cellData -> cellData.getValue().qtyInStockProperty().asObject());
        itemsTotalPriceCol.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());

        // Load data from SQLite database
        loadTransactionsFromDatabase();
        loadItemsFromDatabase();
    }

    private void loadTransactionsFromDatabase() {
        transactionsData = FXCollections.observableArrayList();

        // SQLite database connection
        String url = "jdbc:sqlite:InvtMgmt.db";  // Replace with your actual SQLite DB path

        try (Connection connection = DriverManager.getConnection(url)) {
            String query = "SELECT id, description, qtySold, amount, stockRemaining, transactionType, date FROM transactions";  // Adjust the table name and query as needed

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String description = resultSet.getString("description");
                    int qtySold = resultSet.getInt("qtySold");
                    double amount = resultSet.getDouble("amount");
                    int stockRemaining = resultSet.getInt("stockRemaining");
                    String transactionType = resultSet.getString("transactionType");
                    String dateString = resultSet.getString("date");  // Get the date as a String

                    // Check if the dateString is null or empty
                    @SuppressWarnings("unused")
					java.util.Date date = null;
                    if (dateString != null && !dateString.isEmpty()) {
                        // Manually parse the date String into a java.util.Date object
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Adjust the format to match your stored date format
                        try {
                            date = sdf.parse(dateString);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();  // Handle invalid date formats (log the error)
                        }
                    }

                    // Add to observable list
                    transactionsData.add(new GUI_Transaction(id, description, qtySold, amount, stockRemaining, transactionType, dateString));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the items to the TableView
        transactionsTable.setItems(transactionsData);
    }
    
    private void loadItemsFromDatabase() {
        itemsData = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:InvtMgmt.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            String query = "SELECT id, description, unitPrice, qtyInStock, totalPrice FROM items";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String description = resultSet.getString("description");
                    double unitPrice = resultSet.getDouble("unitPrice");
                    int qtyInStock = resultSet.getInt("qtyInStock");
                    double totalPrice = resultSet.getDouble("totalPrice");

                    itemsData.add(new GUI_Items(id, description, unitPrice, qtyInStock, totalPrice));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        itemsTable.setItems(itemsData);
    }
	
    // The below methods "refresh" the graphical Items and Transactions table on the screen.
    // These are called at the end of every action for the user, whether thats inserting a new item
    // updating the quantity of an old one, or simply deleting it.
    public void reloadTransactionsTableData() {
        transactionsData.clear();  // Clear the existing data in the TableView
        loadTransactionsFromDatabase();  // Reload the data from the database
        transactionsTable.refresh();  // Refresh the TableView to show updated data
    }
    
    public void reloadItemsTableData() {
        itemsData.clear();  // Clear the existing data in the TableView
        loadItemsFromDatabase();  // Reload the data from the database
        itemsTable.refresh();  // Refresh the TableView to show updated data
    }
}
