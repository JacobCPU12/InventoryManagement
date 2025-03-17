package inventory_management;

import java.util.InputMismatchException;
// Import the Scanner class so we can get user input for the name, quantity in stock & price
import java.util.Scanner;
// Import all the necessary classes for an SQL connection
import java.sql.*;

public class DeleteItem {
	
	// "throws InterruptedException" is included so we can refer back to other classes.
	// it is required, otherwise, an error is shown "Unhandled exception type"

	public static void main(String[] args) throws InterruptedException {
		// Delete item class
		/*
		 * If "text" is called with the deleteItem method,
		 * then the user is using the command line interface.
		 * 
		 * If "graphical" is called instead,
		 * then the user is using the graphical user interface.
		 */
		
		// Since this class normally will be using CLI mode,
		// zeroes and blank text are provided to prevent errors.
		deleteItem("text", 0);
	}

	public static void deleteItem(String type, int itemID) throws InterruptedException {
		if (type == "text") {
			// This class does the following
			/*
			 * 1. Use a try/catch block to get any errors, then connect to the DB file
			 * and output all items/products.
			 * 2. Get the user to enter the ID of the product they wish to delete.
			 * 3. The user is asked to confirm, if they are sure they want to delete that item?
			 * 4. Delete the record.
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
	          // of the product they wish to delete from the table3.
	          System.out.print("\nEnter the ID of the product you wish to delete: ");
	          int productID = input.nextInt();
	          
	          // SQL query to get the selected product details
	          String selectProduct = "SELECT description, unitPrice, totalPrice FROM items WHERE id = ?";
	          PreparedStatement psProduct = connection.prepareStatement(selectProduct);
	          // Set the ID of product we need to retrieve to the one the user entered.
	          psProduct.setInt(1, productID);
	          ResultSet rsProduct = psProduct.executeQuery();
	
	          // Declare and initialise variables to store the unit and total prices.
	          double unitPrice = 0;
	          double totalPrice = 0;
	          String productName = "";
	
	          // Check if product exists and retrieve values
	          // This also prints out the current values to the user, for simplicity.
	          if (rsProduct.next()) {
	              unitPrice = rsProduct.getDouble("unitPrice");
	              totalPrice = rsProduct.getDouble("totalPrice");
	              productName = rsProduct.getString("description");
	
	              System.out.printf("\nSelected Product ID: %d%n", productID);
	              System.out.printf("Unit Price: £%.2f%n", unitPrice);
	              System.out.printf("Total Price: £%.2f%n", totalPrice);
	          } else {
	              System.out.println("\nProduct not found.");
	          }
	          
	          // Once the ID has been selected by the user,
	          // Ask the user to confirm they wish to delete the item.
	          System.out.print("\nAre you sure you want to delete this item? 1 for yes, 2 for no: ");
	          int userDeleteSelection = input.nextInt();
	          
			  try {
				    switch (userDeleteSelection) {
			        case 1:
			            // DELETE query execution
			            String deleteItemSQL = "DELETE FROM items WHERE id = ?";
			            PreparedStatement prepStmt = connection.prepareStatement(deleteItemSQL);
			            prepStmt.setInt(1, productID);
			
			            int deleteItem = prepStmt.executeUpdate();
			
			            if (deleteItem > 0) {
			                System.out.println("Product has been deleted successfully!");
			                connection.close();
			
			                // Log transaction
			                Transactions.deleteItem(itemID, productName);
			
			                // Ask if the user wants to return to the main menu
			                AskUserMainMenu.main(null);
			            } else {
			                System.out.println("Product has not been deleted, an error has occurred.");
			            }
			            break;
			        case 2:
			        	// The user selected no, they are asked if they want to go back the main menu.
			        	System.out.println("No problem, we won't delete the item!");
			        	
			        	AskUserMainMenu.main(null);
			        	break;
			
			        default:
			            System.out.println("Invalid selection. Please enter a valid option.");
			            break;
				    }
				} catch (InputMismatchException e) {
				    System.err.println("Invalid input! Please enter a valid number.");
				    e.printStackTrace();
				}

	        } catch(SQLException e)
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
	          
	       // DELETE query execution
	            String deleteItemSQL = "DELETE FROM items WHERE id = ?";
	            PreparedStatement prepStmt = connection.prepareStatement(deleteItemSQL);
	            prepStmt.setInt(1, itemID);
	            
	            // Before deleting the item, we need to get the description
		          // SQL query to get the selected product details
		          String selectProduct = "SELECT description FROM items WHERE id = ?";
		          PreparedStatement psProduct = connection.prepareStatement(selectProduct);
		          // Set the ID of product we need to retrieve to the one the user entered.
		          psProduct.setInt(1, itemID);
		          ResultSet rsProduct = psProduct.executeQuery();
		          String productName = "";
		
		          // Check if product exists and retrieve values
		          // This also prints out the current values to the user, for simplicity.
		          if (rsProduct.next()) {
		              productName = rsProduct.getString("description");
		          } else {
		              System.out.println("\nProduct not found.");
		          }
	
	            int deleteItem = prepStmt.executeUpdate();
	
	            if (deleteItem > 0) {
	                System.out.println("Product has been deleted successfully!");
	                connection.close();
	
	                // Log transaction
	                Transactions.deleteItem(itemID, productName);
	            } else {
	                System.out.println("Product has not been deleted, an error has occurred.");
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
