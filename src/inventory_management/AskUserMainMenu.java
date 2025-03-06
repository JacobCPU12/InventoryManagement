package inventory_management;

// The following classes are required for this.
import java.util.InputMismatchException;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class AskUserMainMenu {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		// First initialise Scanner with input
		Scanner input = new Scanner(System.in);
		
		// Once that has been executed, the user is asked if they want to go back to main menu.
		System.out.println("\nWould you like to go back to the home page?");
		System.out.print("Enter 1 for Yes, 2 for No: ");
		  
		try {
			 int selection = input.nextInt();
			 // Simple switch statement, replacement for an if, else if and else statement.
			 // The user enters 1 to go back to the main menu, enters 2 and the programme will exit
			 // This has been put inside of a try/catch statement so we can catch InputMismatch errors
			 // If the user does enter in a letter instead of a number
			 switch (selection) {
			  	  	case 1:
			  	  		// After two seconds has passed by, the user is redirected back to
			  	  		// the main menu.
			  	  		System.out.println("\nNo problem! Redirecting back home in two seconds!");
			  	  		TimeUnit.SECONDS.sleep(2);
			  	  		Store.MainMenu();
			  	  	case 2:
			  	  		System.out.println("\nThank you for using the system, goodbye!");
			  	  		break;
			  	  	default:
			  	  		System.out.println("\nYou have not selected a valid option, goodbye!");
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
		
		// Close the input for security purposes
		input.close();
	}
}
