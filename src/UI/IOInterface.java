package UI;

import java.util.Scanner;
import java.util.List;

public class IOInterface {
    private static IOInterface instance;
    private Scanner scanner;

    private IOInterface() {
        scanner = new Scanner(System.in);
    }

    public static IOInterface getInstance() {
        if (instance == null) {
            instance = new IOInterface();
        }
        return instance;
    }

    public String[] getUserInput(String message, int numOfArgs) {
        System.out.print(message + ": ");
        String input = scanner.nextLine().trim();
        String[] parts = input.split("\\s+", numOfArgs);

        // Ensure we return exactly numOfArgs elements
        String[] result = new String[numOfArgs];
        System.arraycopy(parts, 0, result, 0, Math.min(parts.length, numOfArgs));

        // Fill any remaining with empty strings
        for (int i = parts.length; i < numOfArgs; i++) {
            result[i] = "";
        }

        return result;
    }

    public void mainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
    }

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

    public void customerMenu() {
        System.out.println("\nCustomer Menu:");
        System.out.println("1. Show profile");
        System.out.println("2. Update profile");
        System.out.println("3. Show products (use '3 keyword' to search)");
        System.out.println("4. Show history orders");
        System.out.println("5. Generate all consumption figures");
        System.out.println("6. Logout");
    }

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

    public void printErrorMessage(String errorSource, String errorMessage) {
        System.out.printf("\nERROR [%s]: %s\n", errorSource, errorMessage);
    }

    public void printMessage(String message) {
        System.out.println("\n" + message);
    }

    public void printObject(Object targetObject) {
        System.out.println("\n" + targetObject.toString());
    }
}