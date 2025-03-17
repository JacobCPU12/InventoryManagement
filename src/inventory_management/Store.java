package inventory_management;

// Import the Scanner class to get user's input
import java.util.Scanner;
// Import the TimeUnit class so the programme can sleep for X number of seconds/minutes
import java.util.concurrent.TimeUnit;
// Import the necessary classes for JavaFX & GUIs
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Store extends Application {
	
	// This code is for the GUI element of the programme.
	public void start(Stage primaryStage) {
		try {
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("InvMgmt_GUI.fxml"));
		    Parent root = loader.load(); // Don't pass any root manually!
		    
		    Scene scene = new Scene(root);
		    primaryStage.setScene(scene);
		    primaryStage.setTitle("Inventory Management System");
		    primaryStage.show();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// Start the main menu
		MainMenu();
	}

	// the "throws InterruptedException" part is required for the sleep() class.
	public static void MainMenu() throws InterruptedException {
		// This method shows the main menu of the programme
		// Regardless of what option the user enters, it refers back to a different class & method
		System.out.println("I N V E N T O R Y    M A N A G E M E N T    S Y S T E M");
		System.out.println("-----------------------------------------------");
		System.out.println("1. ADD NEW ITEM");
		System.out.println("2. SEARCH FOR ITEM");
		System.out.println("3. UPDATE QUANTITY OF EXISTING ITEM");
		System.out.println("4. REMOVE ITEM");
		System.out.println("5. VIEW DAILY TRANSACTION REPORT");
		System.out.println("6: ACCESS GRAPHICAL INTERFACE");
		System.out.println("---------------------------------");
		System.out.println("7. Exit");
		
		// Scanner is initialised with the variable name "input"
		// This is used to get input from the user.
		Scanner input = new Scanner(System.in);
		
		System.out.print("\n Enter a choice and Press ENTER to continue [1-7]: ");
		int userinput = input.nextInt();
		
		// A switch statement has been used instead of multiple if statements, purely for simplicity
		// and code appearance.
		switch (userinput) {
			case 1:
				// Refer to the NewItem class.
				NewItem.main(null);
				break;
			case 2:
                // Refer to the new SearchItem class, this command goes to the other class file
                // and executes all code in that file
                SearchItem.main(null);
				break;
			case 3:
				// Same as above but instead the UpdateQuantity class.
				UpdateQuantity.main(null);
				break;
			case 4:
				// DeleteItem class
				DeleteItem.main(null);
				break;
			case 5:
				// TransactionReport class
				TransactionReport.main(null);
				break;
			case 6:
				System.out.println("Of course, opening in one second!");
				TimeUnit.SECONDS.sleep(1);
				// launch() is the keyword used for opening imported interfaces.
				// This directly links to the above class for opening the stage.
				launch();
				break;
			case 7:
				System.out.print("\n Goodbye!");
				break;
			default:
				System.out.print("\n " + userinput + " is not a valid option! Main menu will be relaunched in two seconds");
				// After two seconds, the main menu is displayed back to the user.
				TimeUnit.SECONDS.sleep(2);
                // This will get this method to "re-run"
				MainMenu();
		}
		
		// For security purposes, close the input variable
		input.close();
	}
}
