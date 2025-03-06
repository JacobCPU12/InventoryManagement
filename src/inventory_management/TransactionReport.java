package inventory_management;

import java.util.InputMismatchException;
// Import the Scanner class so we can get user input for the name, quantity in stock & price
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
// Import all the necessary classes for an SQL connection
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

////
// TODO finish this when get home
////
public class TransactionReport {
	
	// "throws InterruptedException" is included so we can refer back to other classes.
	// it is required, otherwise, an error is shown "Unhandled exception type"

	public static void main(String[] args) throws InterruptedException {
		// Transaction report class
		generateReport("text");
	}

	public static void generateReport(String type) throws InterruptedException {
		if (type == "text") {
			// This class does the following
			/*
			 * 1. Connects to the database and grabs all information from the transaction table.
			 * 2. Then outputs it as a table on the screen.
			 * (The information collected will be based on the current day's records).
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
	          String grabAllItems = "SELECT * FROM transactions WHERE date = ?";
	          PreparedStatement ps = connection.prepareStatement(grabAllItems);
	          
	          // https://stackoverflow.com/a/31138907
	          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	  		  String currentDate = LocalDate.now().format(formatter);
	          
	          ps.setString(1, currentDate);
	          ResultSet rs = ps.executeQuery();
	          // Print table header
	          System.out.printf("%-5s | %-20s | %-10s | %-10s | %-15s | %-15s | %-20s%n", 
	              "ID", "Description", "Qty Sold", "Amount", "Stock Remaining", "Transaction Type", "Date");
	          System.out.println("----------------------------------------------------------------------------------------------------------------");
	
	          // Print table rows of all transactions retrieved
	          while (rs.next()) {
	              System.out.printf("%-5d | %-20s | %-10s | £%-9s | %-15d | %-15s | %-20s%n",
	                  rs.getInt("id"),
	                  rs.getString("description"),
	                  rs.getString("qtySold"),
	                  rs.getString("amount"),  // Assuming amount is stored as TEXT
	                  rs.getInt("stockRemaining"),
	                  rs.getString("transactionType"),
	                  rs.getString("date"));
	          }
	
	          // Now the transaction table has been printed (for the current day's data)
	          // Simply ask the user if they want to go back to the main menu.
	          AskUserMainMenu.main(null);
	        }
	        catch(SQLException e)
	        {
	          // if the error message is "out of memory",
	          // it probably means no database file is found
	          e.printStackTrace(System.err);
	        }
		} else if (type == "graphical") {
			
		}
	}
	
	// This method takes the new quantity supplied by the user,
	// and multiples that number against the unit price already defined in the table.
	// This will return the new total price to put back into the table.
	public static double calculateTotalPrice(double a, double b) {
		return a*b;
	}
}
