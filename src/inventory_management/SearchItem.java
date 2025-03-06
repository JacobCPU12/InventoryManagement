package inventory_management;

import java.util.InputMismatchException;
// Import the Scanner class so we can get user input for the name, quantity in stock & price
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
// Import all the necessary classes for an SQL connection
import java.sql.*;

public class SearchItem {

	public static void main(String[] args) throws InterruptedException {
		// New Item class
		searchItem();
	}

	public static void searchItem() throws InterruptedException {
		// This class does the following
		/*
		 * 1. Initialises Scanner with a variable named "input"
		 * 2. Get the user to enter in a product name.
		 * 3. Searches through the database with the name supplied by the user.
		 * 4. A table is shown with all products matching to that name.
		 */
		Scanner input = new Scanner(System.in);
		
		System.out.println("Search for an existing item:");
		System.out.print("Enter a product name: ");
		String productName = input.nextLine();
		
		// The following try/catch statement is purely for connecting to the local DB file
        try
        (
          // This utilises a custom SQLite plugin to connect to a locally stored database file.
          Connection connection = DriverManager.getConnection("jdbc:sqlite:InvtMgmt.db");
          Statement statement = connection.createStatement();
        )
        {
          statement.setQueryTimeout(30);  // set timeout to 30 sec.

          //statement.executeUpdate("drop table if exists person");
          //statement.executeUpdate("create table person (id integer, name string)");
          
          //statement.executeUpdate("insert into person values('" + name + "')");
          
          /*String grabAllItems = "INSERT INTO items (description, unitPrice, qtyInStock, totalPrice) VALUES ('" + newProductName + "', '" + newPrice + "', '" + prodQuantity + "', '" + totalPrice + "')";
          System.out.println(grabAllItems);
          PreparedStatement ps = connection.prepareStatement(grabAllItems);
          ResultSet Result = ps.executeQuery();*/
          
          // This statement grabs all existing items from the database that links
          // to the input the user has supplied.
          String grabAllItems = "SELECT * FROM items WHERE description LIKE ?";
          PreparedStatement ps = connection.prepareStatement(grabAllItems);
          ps.setString(1, "%" + productName + "%");
          ResultSet rs = ps.executeQuery();
          while(rs.next())
          {
            // read the result set
        	System.out.println();
            System.out.println(" Product ID = " + rs.getInt("id"));
            System.out.println(" Description = " + rs.getString("description"));
            System.out.println(" Price = £" + rs.getDouble("unitPrice"));
            System.out.println(" Quantity in Stock = " + rs.getInt("qtyInStock"));
            System.out.println(" Total Value = £" + rs.getDouble("totalPrice"));
            // Print a blank line to separate each found product.
            System.out.println();
            
            
            // read the result set
            /*System.out.println("id = " + rs.getInt("id"));
            System.out.println("description = " + rs.getString("description"));
            System.out.println("unitPrice = " + rs.getInt("unitPrice"));
            System.out.println("qtyInStock = " + rs.getInt("qtyInStock"));
            System.out.println("totalPrice = " + rs.getInt("totalPrice"));*/
          }
          
          // Now that a product has been found,
          // the user will be asked if they want to return to the main menu
          System.out.println("Would you like to go back to the main menu? 1 for yes, 2 for no: ");
          
          try {
	          int userSelection = input.nextInt();
	          
	          switch (userSelection) {
		          case 1:
		        	  System.out.println("Going back to the main menu in two seconds.");
		        	  TimeUnit.SECONDS.sleep(2);
		        	  Store.MainMenu();
		        	  break;
		          case 2:
		        	  System.out.println("No problem, thanks for using this software, goodbye!");
		        	  break;
		          default:
		        	  System.out.println("You have not entered a valid option, restart the programme.");
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
        }
        catch(SQLException e)
        {
          // if the error message is "out of memory",
          // it probably means no database file is found
          e.printStackTrace(System.err);
        }
        
        // Closing the input for Scanner for security purposes.
        input.close();
	}
}
