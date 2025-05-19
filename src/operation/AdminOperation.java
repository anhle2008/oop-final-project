package operation;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdminOperation {
    // Singleton instance to ensure only one AdminOperation object exists
    private static AdminOperation instance;

    // List to hold all admin users loaded from file or created in runtime
    private List<Admin> admins;

    // Private constructor to prevent creating multiple instances
    private AdminOperation() {
        admins = new ArrayList<>();
        loadAdminsFromFile();  // Load admin data from file when object is instantiated
    }

    // Public method to get the singleton instance of AdminOperation
    public static AdminOperation getInstance() {
        if (instance == null) {
            instance = new AdminOperation(); // Create instance if it does not exist yet
        }
        return instance;
    }

    /**
     * Loads admins from a  file.
     * Each line in the file should contain:
     * userId,userName,password,registerTime,role
     * Example line: u_0000000001,admin,admin123,01-01-2023_00:00:00,admin
     */
    private void loadAdminsFromFile() {
        String filePath = "admins.txt"; // Path to the admins data file

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read the file line by line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");  // Split line into components by comma
                if (parts.length == 5) {           // Check if line format is valid
                    // Create Admin object with trimmed strings to avoid whitespace issues
                    Admin admin = new Admin(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            parts[3].trim(), parts[4].trim());
                    admins.add(admin);  // Add loaded admin to the admins list
                } else {
                    System.out.println("Skipping invalid admin line: " + line);  // Notify on bad format
                }
            }
        } catch (IOException e) {
            // If file is missing or unreadable, print error message but allow app to continue
            System.out.println("Failed to load admins from file: " + e.getMessage());
        }
    }

    /**
     * Registers a default admin if none exist.
     * This is useful for initial setup or first run of the application.
     */
    public void registerAdmin() {
        // If admins list is already populated, do not register again
        if (!admins.isEmpty()) {
            return;
        }

        // Get UserOperation singleton to generate a unique user ID for the admin
        UserOperation userOp = UserOperation.getInstance();
        String adminId = userOp.generateUniqueUserId();

        // Create a default admin user with preset values
        Admin admin = new Admin(adminId, "admin", "admin123",
                "01-01-2023_00:00:00", "admin");

        admins.add(admin);  // Add the new admin to the list


    }

    /**
     * Displays orders with simple pagination support.
     * Allows the admin user to navigate through pages of orders.
     * @param scanner Scanner object to read user input for pagination control.
     */
    public void showOrders(Scanner scanner) {
        OrderOperation orderOp = OrderOperation.getInstance();  // Get orders manager instance
        int page = 1;  // Start at page 1

        while (true) {
            // Retrieve paginated orders for current page
            OrderListResult result = orderOp.getAllOrders(page);
            List<Order> orders = result.getOrders();

            if (orders.isEmpty()) {
                System.out.println("No orders found.");
                break;  // Exit if no orders on this page
            }

            // Print header showing current page info
            System.out.println("\n=== Orders Page " + result.getCurrentPage() + " of " + result.getTotalPages() + " ===");
            for (Order order : orders) {
                System.out.println(order.toString());  // Display each order
            }

            // Prompt user for pagination control
            System.out.println("\n[N] Next page | [P] Previous page | [E] Exit");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine().trim().toLowerCase();

            // Handle user input to navigate pages or exit
            if (choice.equals("n")) {
                if (page < result.getTotalPages()) {
                    page++;  // Go to next page if available
                } else {
                    System.out.println("You're on the last page.");
                }
            } else if (choice.equals("p")) {
                if (page > 1) {
                    page--;  // Go to previous page if available
                } else {
                    System.out.println("You're on the first page.");
                }
            } else if (choice.equals("e")) {
                break;  // Exit pagination loop
            } else {
                System.out.println("Invalid choice.");  // Handle invalid input gracefully
            }
        }
    }


}
