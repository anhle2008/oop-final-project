package UI;

import java.util.Scanner;
import java.util.List;

// This class handles input and output operations for the user interface.
public class IOInterface {
    // Singleton instance of IOInterface
    private static IOInterface instance;
    private Scanner scanner;

    // Private constructor to prevent direct instantiation (singleton pattern)
    private IOInterface() {
        scanner = new Scanner(System.in);
    }

    // Returns the single instance of IOInterface, creating it if necessary
    public static IOInterface getInstance() {
        if (instance == null) {
            instance = new IOInterface();
        }
        return instance;
    }

    /**
     * Prompts the user for input with a given message and returns the input as an array of strings.
     * @param message Message to prompt the user
     * @param numOfArgs Expected number of arguments in the input
     * @return Array of strings containing user input arguments
     */
    public String[] getUserInput(String message, int numOfArgs) {
        System.out.print(message + ": ");
        String input = scanner.nextLine().trim();
        String[] parts = input.split("\\s+", numOfArgs); // Split input by whitespace

        // Ensure the array has exactly numOfArgs elements
        String[] result = new String[numOfArgs];
        System.arraycopy(parts, 0, result, 0, Math.min(parts.length, numOfArgs));

        // Fill remaining elements with empty strings if input is incomplete
        for (int i = parts.length; i < numOfArgs; i++) {
            result[i] = "";
        }

        return result;
    }

    // Displays the main menu options for all users
    public void mainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
    }

    // Displays the menu options available to admin users
    public void adminMenu() {
        System.out.println("\nAdmin Menu:");
        System.out.println("1. Show products");
        System.out.println("2. Add customers");
        System.out.println("3. Show customers");
        System.out.println("4. Show orders");
        System.out.println("5. Generate test data");
        System.out.println("6. Generate all statistical figures");
        System.out.println("7. Delete all data");
        System.out.println("8. Logout");
    }

    // Displays the menu options available to customer users
    public void customerMenu() {
        System.out.println("\nCustomer Menu:");
        System.out.println("1. Show profile");
        System.out.println("2. Update profile");
        System.out.println("3. Show products (use '3 keyword' to search)");
        System.out.println("4. Show history orders");
        System.out.println("5. Generate all consumption figures");
        System.out.println("6. Logout");
    }

    /**
     * Displays a list of items with pagination support.
     * @param userRole Role of the user viewing the list
     * @param listType Type of list (e.g., Products, Orders)
     * @param objectList List of objects to display
     * @param pageNumber Current page number
     * @param totalPages Total number of pages
     */
    public void showList(String userRole, String listType, List<?> objectList,
                         int pageNumber, int totalPages) {
        System.out.printf("\n%s List (Page %d of %d):\n", listType, pageNumber, totalPages);
        System.out.println("------------------------------------------------");

        int row = 1;
        for (Object obj : objectList) {
            System.out.printf("%d. %s\n", row++, obj.toString());
        }

        System.out.println("------------------------------------------------");
    }

    /**
     * Displays an error message with context of the source.
     * @param errorSource The component or method that caused the error
     * @param errorMessage The descriptive error message
     */
    public void printErrorMessage(String errorSource, String errorMessage) {
        System.out.printf("\nERROR [%s]: %s\n", errorSource, errorMessage);
    }

    /**
     * Prints a simple informational message.
     * @param message The message to display
     */
    public void printMessage(String message) {
        System.out.println("\n" + message);
    }

    /**
     * Prints the string representation of a given object.
     * @param targetObject Object to print
     */
    public void printObject(Object targetObject) {
        System.out.println("\n" + targetObject.toString());
    }
}
