package inventory_management;

import java.util.InputMismatchException;
// Import the Scanner class so we can get user input for the name, quantity in stock & price
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
// Import all the necessary classes for an SQL connection
import java.sql.*;

public class UpdateQuantity {
	
	// "throws InterruptedException" is included so we can refer back to other classes.
	// it is required, otherwise, an error is shown "Unhandled exception type"

	public static void main(String[] args) throws InterruptedException {
		// Update class
		/*
		 * If "text" is called with the createNewItem method,
		 * then the user is using the command line interface.
		 * 
		 * If "graphical" is called instead,
		 * then the user is using the graphical user interface.
		 */
		
		// Since this class normally will be using CLI mode,
		// zeroes and blank text are provided to prevent errors.
		updateQuantity("text", 0, 0, false);
	}

	public static void updateQuantity(String type, int itemID, int quantity, boolean sale) throws InterruptedException {
		if (type == "text") {
			// This class does the following
			/*
			 * 1. Use a try/catch block to get any errors, then connect to the DB file
			 * and output all items/products.
			 * 2. Get the user to enter the ID of the product they wish to edit.
			 * 3. Enter a new quantity.
			 * 4. Update the record.
			 */
			Scanner input = new Scanner(System.in);
			
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
	          String grabAllItems = "SELECT * FROM items";
	          PreparedStatement ps = connection.prepareStatement(grabAllItems);
	          ResultSet rs = ps.executeQuery();
	          // Print table header
	          System.out.printf("%-5s | %-20s | %-10s | %-10s | %-10s%n", "ID", "Description", "Unit Price", "Qty", "Total Price");
	          System.out.println("---------------------------------------------------------------------");
	
	          // Print table rows of all items retrieved
	          while (rs.next()) {
	              System.out.printf("%-5d | %-20s | £%-9.2f | %-10d | £%-9.2f%n",
	              rs.getInt("id"),
	              rs.getString("description"),
	              rs.getDouble("unitPrice"),
	              rs.getInt("qtyInStock"),
	              rs.getDouble("totalPrice"));
	          }
	          
	          // Now that all current products have been output, the user needs to enter the ID,
	          // of the product they wish to change the quantity for.
	          System.out.print("\nEnter the ID of the product you wish to change: ");
	          int productID = input.nextInt();
	          
	          // SQL query to get the selected product details
	          String selectProduct = "SELECT description, unitPrice, totalPrice, qtyInStock FROM items WHERE id = ?";
	          PreparedStatement psProduct = connection.prepareStatement(selectProduct);
	          // Set the ID of product we need to retrieve to the one the user entered.
	          psProduct.setInt(1, productID);
	          ResultSet rsProduct = psProduct.executeQuery();
	
	          // Declare and initialise variables to store the unit and total prices.
	          double unitPrice = 0;
	          double totalPrice = 0;
	          String productName = "";
	          int currentQuantity = 0;
	
	          // Check if product exists and retrieve values
	          // This also prints out the current values to the user, for simplicity.
	          if (rsProduct.next()) {
	              unitPrice = rsProduct.getDouble("unitPrice");
	              totalPrice = rsProduct.getDouble("totalPrice");
	              productName = rsProduct.getString("description");
	              currentQuantity = rsProduct.getInt("qtyInStock");
	
	              System.out.printf("\nSelected Product ID: %d%n", productID);
	              System.out.printf("Unit Price: £%.2f%n", unitPrice);
	              System.out.printf("Total Price: £%.2f%n", totalPrice);
	          } else {
	              System.out.println("\nProduct not found.");
	          }
	          
	          // Once the ID has been selected by the user,
	          // Ask to enter the number that is now in stock.
	          System.out.print("\nEnter the updated quantity of the item selected: ");
	          int newQuantity = input.nextInt();
	          
	          // Once the new quantity has been inserted,
	          // we need to calculate the new total price of all in stock.
	          // This method (code available at the end of this file) - calculates the necessary value.
	          double newTotalPrice = calculateTotalPrice(unitPrice, newQuantity);
	          
	          // We now have all the information needed to execute the UPDATE query
	          // The product ID and the new quantity amount.
	          // A variable named insertNewItem purely to store the beginning of the SQL query
	          String updateQuantitySQL = "UPDATE items SET qtyInStock = ?, totalPrice = ? WHERE id = ?";
	          PreparedStatement prepStmt = connection.prepareStatement(updateQuantitySQL);
	          // The four below lines assigns the columns with each variable of data provided by the user.
	          prepStmt.setInt(1, newQuantity);
	          prepStmt.setDouble(2, newTotalPrice);
	          prepStmt.setInt(3, productID);
	          
	          // The amount of rows added to the table - if more than 0 (or equal to 1 in this case - query succeeded)
	          int updateQuantity = prepStmt.executeUpdate();
	          
	          if (updateQuantity > 0) {
	        	  System.out.println("Product quantity has been updated successfully!");
	        	  connection.close();
	        	  
	        	  // Before the transaction is recorded, we need to ask the user
	        	  // whether or not this was part of a sale
	        	  System.out.print("Are you updating the quantity for a sale? (1 for yes, 2 for no): ");
	      		try {
		   			 int selection = input.nextInt();
		   			 // Simple switch statement, replacement for an if, else if and else statement.
		   			 // This has been put inside of a try/catch statement so we can catch InputMismatch errors
		   			 // If the user does enter in a letter instead of a number
		   			 switch (selection) {
		   			  	  	case 1:
		   		        	  // Now the quantity has been updated, we still need to add a new row in the transactions table
		   		        	  // to show that an action has taken place.
		   		        	  Transactions.updateQuantity(productName, unitPrice, newQuantity, currentQuantity, true);
		   			  	  	case 2:
		   		        	  Transactions.updateQuantity(productName, unitPrice, newQuantity, 0, false);
		   			  	  	  break;
		   			  	  	default:
	   			  	  		  System.out.println("\nYou have not entered a valid option, going back to main menu!");
		   			  	  	  TimeUnit.SECONDS.sleep(2);
				  	  		  Store.MainMenu();
	   			  	  		  break;
		   			  	  }
		   		}
		   		// If the user enters letters instead of a number, the exception is caught
		   		// instead of the programme crashing altogether.
		   		catch (InputMismatchException e) {
		   			 e.printStackTrace(System.err);
		   			 System.out.println("\n An error occurred, letter(s) were entered instead of a number. Going back to main menu.");
		   			 Store.MainMenu();
		   		}
	        	  
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
	        
	        // Finally close the input variable for security purposes
	        input.close();
		} else if (type == "graphical") {
			try
	        (
	          // This utilises a custom SQLite plugin to connect to a locally stored database file.
	          Connection connection = DriverManager.getConnection("jdbc:sqlite:InvtMgmt.db");
	          Statement statement = connection.createStatement();
	        )
	        {
	          statement.setQueryTimeout(30);  // set timeout to 30 sec.
	          
	          // Even though we're now also using a graphical user interface
	          // We still need to grab all the current information related to the item
	          // SQL query to get the selected product details
	          String selectProduct = "SELECT description, unitPrice, totalPrice, qtyInStock FROM items WHERE id = ?";
	          PreparedStatement psProduct = connection.prepareStatement(selectProduct);
	          // Set the ID of product we need to retrieve to the one the user entered.
	          psProduct.setInt(1, itemID);
	          ResultSet rsProduct = psProduct.executeQuery();
	
	          // Declare and initialise variables to store the unit and total prices.
	          double unitPrice = 0;
	          double totalPrice = 0;
	          int oldQuantity = 0;
	          String productName = "";
	
	          // Check if product exists and retrieve values
	          // This also prints out the current values to the user, for simplicity.
	          if (rsProduct.next()) {
	              unitPrice = rsProduct.getDouble("unitPrice");
	              totalPrice = rsProduct.getDouble("totalPrice");
	              productName = rsProduct.getString("description");
	              oldQuantity = rsProduct.getInt("qtyInStock");
	          } else {
	              System.out.println("\nProduct not found.");
	          }
	          
	          // totalPrice needs to be amended due to the change in stock
	          totalPrice = calculateTotalPrice(unitPrice, quantity);
	          
	          // We now have all the information needed to execute the UPDATE query
	          // The product ID and the new quantity amount.
	          // A variable named updateQuantitySQL purely to store the beginning of the SQL query
	          String updateQuantitySQL = "UPDATE items SET qtyInStock = ?, totalPrice = ? WHERE id = ?";
	          PreparedStatement prepStmt = connection.prepareStatement(updateQuantitySQL);
	          // The four below lines assigns the columns with each variable of data provided by the user.
	          prepStmt.setInt(1, quantity);
	          prepStmt.setDouble(2, totalPrice);
	          prepStmt.setInt(3, itemID);
	          
	          // The amount of rows added to the table - if more than 0 (or equal to 1 in this case - query succeeded)
	          int updateQuantity = prepStmt.executeUpdate();
	          
	          if (updateQuantity > 0) {
	        	  System.out.println("Product quantity has been updated successfully!");
	        	  connection.close();
	        	  
	        	  // Now the quantity has been updated, we still need to add a new row in the transactions table
	        	  // to show that an action has taken place.
	        	  Transactions.updateQuantity(productName, unitPrice, quantity, oldQuantity, true);
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
	
	// This method takes the new quantity supplied by the user,
	// and multiples that number against the unit price already defined in the table.
	// This will return the new total price to put back into the table.
	public static double calculateTotalPrice(double a, double b) {
		return a*b;
	}
}
