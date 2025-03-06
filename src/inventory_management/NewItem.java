package inventory_management;

// Import the Scanner class so we can get user input for the name, quantity in stock & price
import java.util.Scanner;

// Import all the necessary classes for an SQL connection
import java.sql.*;

public class NewItem {

	public static void main(String[] args) throws InterruptedException {
		// New Item class
		/*
		 * If "text" is called with the createNewItem method,
		 * then the user is using the command line interface.
		 * 
		 * If "graphical" is called instead,
		 * then the user is using the graphical user interface.
		 */
		
		// Since this class normally will be using CLI mode,
		// zeroes and blank text are provided to prevent errors.
		createNewItem("text", "", 0, 0);
	}

	// desc, price and quantity are only being used
	// for if "graphical" is being called, because the data is being passed through.
	public static void createNewItem(String type, String desc, double price, int quantity) throws InterruptedException {
		
		// If the CLI is being used:
		if (type == "text") {
			// This class does the following
			/*
			 * 1. Initialises Scanner with a variable named "input"
			 * 2. Ask the user multiple questions (enter product name, price, how much is in stock?)
			 * 3. Accesses the local database file, to we can add a new row for an item
			 * 4. Insert that data back into the database, for later use.
			 */
			Scanner input = new Scanner(System.in);
			
			System.out.println("This section allows you to enter a new item - you'll be asked a few questions.");
			System.out.print("Enter a product name: ");
			String newProductName = input.nextLine();
			
			System.out.print("Enter price of product (the unit price - two decimal places - don't include £): ");
			double newPrice = input.nextDouble();
			
			System.out.print("Enter total quantity of product: ");
			int prodQuantity = input.nextInt();
			
			// This is based on how much of that product is in stock * the amount of each
			double totalPrice = newPrice * prodQuantity;
			
			// The following try/catch statement is purely for connecting to the local DB file
	        try
	        (
	          // This utilises a custom SQLite plugin to connect to a locally stored database file.
	          Connection connection = DriverManager.getConnection("jdbc:sqlite:InvtMgmt.db");
	          Statement statement = connection.createStatement();
	        )
	        {
	          statement.setQueryTimeout(30);  // set timeout to 30 sec.
	          
	          // A variable named insertNewItem purely to store the beginning of the SQL query
	          String insertNewItem = "INSERT INTO items (description, unitPrice, qtyInStock, totalPrice) VALUES (?, ?, ?, ?)";
	          PreparedStatement prepStmt = connection.prepareStatement(insertNewItem);
	          // The four below lines assigns the columns with each variable of data provided by the user.
	          prepStmt.setString(1, newProductName);
	          prepStmt.setDouble(2, newPrice);
	          prepStmt.setInt(3, prodQuantity);
	          prepStmt.setDouble(4, totalPrice);
	          
	          // The amount of rows added to the table - if more than 0 (or equal to 1 in this case - query succeeded)
	          int insertNewRecord = prepStmt.executeUpdate();
	          
	          if (insertNewRecord > 0) {
	        	  System.out.println("Product has been added successfully!");
	        	  connection.close();
	        	  
	        	  // Even though the item has been added,
	        	  // we now need to update the transactions table, to show that a change has been made.
	        	  Transactions.newItem("created", newProductName, newPrice, prodQuantity);
	        	  
	        	  // Now that the transactions has been logged, finally ask the user,
	        	  // if they want to go back to the main menu.
	        	  AskUserMainMenu.main(null);
	          } else {
	        	  System.out.println("Product has not been added, an error has occurred");
	          }
	        }
	        catch(SQLException e)
	        {
	          // if the error message is "out of memory",
	          // it probably means no database file is found
	          e.printStackTrace(System.err);
	        }
	        
	        // Closing the input for Scanner for security purposes.
	        input.close();
        
		} else if (type == "graphical") {
			//double totalPrice = price * quantity;
			double totalPrice = UpdateQuantity.calculateTotalPrice(price, quantity);
			
			// The following try/catch statement is purely for connecting to the local DB file
	        try
	        (
	          // This utilises a custom SQLite plugin to connect to a locally stored database file.
	          Connection connection = DriverManager.getConnection("jdbc:sqlite:InvtMgmt.db");
	          Statement statement = connection.createStatement();
	        )
	        {
	          statement.setQueryTimeout(30);  // set timeout to 30 sec.
	          
	          // A variable named insertNewItem purely to store the beginning of the SQL query
	          String insertNewItem = "INSERT INTO items (description, unitPrice, qtyInStock, totalPrice) VALUES (?, ?, ?, ?)";
	          PreparedStatement prepStmt = connection.prepareStatement(insertNewItem);
	          // The four below lines assigns the columns with each variable of data provided by the user.
	          prepStmt.setString(1, desc);
	          prepStmt.setDouble(2, price);
	          prepStmt.setInt(3, quantity);
	          prepStmt.setDouble(4, totalPrice);
	          
	          // The amount of rows added to the table - if more than 0 (or equal to 1 in this case - query succeeded)
	          int insertNewRecord = prepStmt.executeUpdate();
	          
	          if (insertNewRecord > 0) {
	        	  System.out.println("Product has been added successfully!");
	        	  connection.close();
	        	  
	        	  // Even though the item has been added,
	        	  // we now need to update the transactions table, to show that a change has been made.
	        	  Transactions.newItem("created", desc, price, quantity);
	        	  
	        	  // Now that the transactions has been logged,
	        	  // a message box is shown to the user, saying their action
	        	  // was successful.
	        	  InvMgmt_GUIController.resultMsgBox(1);
	          } else {
	        	  System.out.println("Product has not been added, an error has occurred");
	          }
	        }
	        catch(SQLException e)
	        {
	          // if the error message is "out of memory",
	          // it probably means no database file is found
	          e.printStackTrace(System.err);
	        }
		}
	}
}
