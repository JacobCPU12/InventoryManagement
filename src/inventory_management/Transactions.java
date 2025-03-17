package inventory_management;

//import java.util.concurrent.TimeUnit;
// Import all the necessary classes for an SQL connection
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transactions {

	public static void main(String[] args) throws InterruptedException {
		
	}
	
	public static void newItem(String type, String productName, double price, int quantity) throws InterruptedException {
		// This class does the following
		/*
		 * 1. Connects to the database.
		 * 2. A query is executed to insert a new row to produce a new transaction update.
		 * 3. The connection is closed for security.
		 */
		
		// The following try/catch statement is purely for connecting to the local DB file
        try
        (
          // This utilises a custom SQLite plugin to connect to a locally stored database file.
          Connection connection = DriverManager.getConnection("jdbc:sqlite:InvtMgmt.db");
          Statement statement = connection.createStatement();
        )
        {
          statement.setQueryTimeout(30);  // set timeout to 30 sec.
          
          // This statement inserts a new row inside of the transaction table
          // so we can keep an update of everything that happens inside of the system.
          String grabAllItems = "INSERT INTO transactions (description, stockRemaining, transactionType, date) VALUES (?, ?, ?, ?)";
          PreparedStatement ps = connection.prepareStatement(grabAllItems);
          ps.setString(1, productName);
          //ps.setDouble(2, price);
          ps.setInt(2, quantity);
          ps.setString(3, type);
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  		  String currentDate = LocalDate.now().format(formatter);
          
  		  ps.setString(4, currentDate);
          ps.executeUpdate();
          
          System.out.println();
          System.out.println("Transaction has also been recorded!");
        }
        catch(SQLException e)
        {
          // if the error message is "out of memory",
          // it probably means no database file is found
          e.printStackTrace(System.err);
        }
	}
	
	public static void updateQuantity(String productName, double price, int quantity, int oldQuantity, boolean sale) throws InterruptedException {
		// This class does the following
		/*
		 * 1. Connects to the database.
		 * 2. A query is executed to insert a new row to produce a new transaction update.
		 * 3. The connection is closed for security.
		 */
		
		// The following try/catch statement is purely for connecting to the local DB file
        try
        (
          // This utilises a custom SQLite plugin to connect to a locally stored database file.
          Connection connection = DriverManager.getConnection("jdbc:sqlite:InvtMgmt.db");
          Statement statement = connection.createStatement();
        )
        {
          statement.setQueryTimeout(30);  // set timeout to 30 sec.
          // Variable for transaction type
          String type = "updated";
          
          // This statement inserts a new row inside of the transaction table
          // so we can keep an update of everything that happens inside of the system.
          
          // However the statement will change slightly to whether or not it is a sale.
          if (sale == true) {
	          String sqlStmt = "INSERT INTO transactions (description, qtySold, amount, stockRemaining, transactionType, date) VALUES (?, ?, ?, ?, ?, ?)";
	          PreparedStatement ps = connection.prepareStatement(sqlStmt);
	          
	          // Before assigning the appropriate values, first,
	          // we need to initialise and workout the price for the amount "sold",
	          // and how many sold in the first place.
	          
	          int amountSold = 0;
	          double totalPrice = 0;
	          
	          if (quantity > oldQuantity) {
	        	  amountSold = quantity - oldQuantity;
	          } else {
	        	  amountSold = oldQuantity - quantity;
	          }
	          
	          totalPrice = price * quantity;
	          
	          // Now we finish the prepared statement, by assigning each column with a piece of data.	          
	          ps.setString(1, productName);
	          ps.setInt(2, amountSold);
	          ps.setDouble(3, totalPrice);
	          ps.setInt(4, quantity);
	          ps.setString(5, type);
	          
	          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	  		  String currentDate = LocalDate.now().format(formatter);
	          
	  		  ps.setString(6, currentDate);
	          ps.executeUpdate();
          } else if (sale == false) {
	          String sqlStmt = "INSERT INTO transactions (description, stockRemaining, transactionType, date) VALUES (?, ?, ?, ?)";
	          PreparedStatement ps = connection.prepareStatement(sqlStmt);
	          ps.setString(1, productName);
	          //ps.setDouble(2, price);
	          ps.setInt(2, quantity);
	          ps.setString(3, type);
	          
	          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	  		  String currentDate = LocalDate.now().format(formatter);
	          
	  		  ps.setString(4, currentDate);
	          ps.executeUpdate();
          }
          
          System.out.println();
          System.out.println("Transaction has been logged.");
          System.out.println();
        }
        catch(SQLException e)
        {
          // if the error message is "out of memory",
          // it probably means no database file is found
          e.printStackTrace(System.err);
        }
	}
	
	public static void deleteItem(int itemID, String itemName) throws InterruptedException {
		// This class does the following
		/*
		 * 1. Connects to the database.
		 * 2. A query is executed to insert a new row to produce a new transaction update.
		 * 3. The connection is closed for security.
		 */
		
		// The following try/catch statement is purely for connecting to the local DB file
        try
        (
          // This utilises a custom SQLite plugin to connect to a locally stored database file.
          Connection connection = DriverManager.getConnection("jdbc:sqlite:InvtMgmt.db");
          Statement statement = connection.createStatement();
        )
        {
          statement.setQueryTimeout(30);  // set timeout to 30 sec.
          
          // This statement inserts a new row inside of the transaction table
          // so we can keep an update of everything that happens inside of the system.
          String grabAllItems = "INSERT INTO transactions (description, transactionType, date) VALUES (?, ?, ?)";
          PreparedStatement ps = connection.prepareStatement(grabAllItems);
          ps.setString(1, itemName);
          ps.setString(2, "deleted");
          
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  		  String currentDate = LocalDate.now().format(formatter);
          
  		  ps.setString(3, currentDate);
          ps.executeUpdate();
          
          System.out.println();
          System.out.println("Transaction has also been recorded!");
        }
        catch(SQLException e)
        {
          // if the error message is "out of memory",
          // it probably means no database file is found
          e.printStackTrace(System.err);
        }
	}

}
