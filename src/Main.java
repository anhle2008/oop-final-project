import UI.IOInterface;
import operation.*;
import model.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize operation instances using singleton pattern
        UserOperation userOp = UserOperation.getInstance();
        CustomerOperation customerOp = CustomerOperation.getInstance();
        AdminOperation adminOp = AdminOperation.getInstance();
        ProductOperation productOp = ProductOperation.getInstance();
        OrderOperation orderOp = OrderOperation.getInstance();
        IOInterface io = IOInterface.getInstance();

        // Ensure that an admin user exists in the system
        adminOp.registerAdmin();

        boolean running = true; // Controls the main program loop
        User currentUser = null; // Stores the currently logged-in user

        while (running) {
            if (currentUser == null) {
                // Show main menu when no user is logged in
                io.mainMenu();
                String[] input = io.getUserInput("Enter your choice", 1);
                String choice = input[0];

                switch (choice) {
                    case "1": // Login
                        String[] credentials = io.getUserInput("Enter username and password", 2);
                        currentUser = userOp.login(credentials[0], credentials[1]);
                        if (currentUser == null) {
                            io.printErrorMessage("Login", "Invalid username or password");
                        }
                        break;
                    case "2": // Register new customer
                        String[] regData = io.getUserInput("Enter username, password, email, mobile", 4);
                        boolean successReg = customerOp.registerCustomer(
                                regData[0], regData[1], regData[2], regData[3]
                        );
                        if (successReg) {
                            io.printMessage("Registration successful! Please login.");
                        } else {
                            io.printErrorMessage("Registration", "Failed to register. Check your input.");
                        }
                        break;
                    case "3": // Quit program
                        running = false;
                        break;
                    default:
                        io.printErrorMessage("Main Menu", "Invalid choice");
                }
            } else if (currentUser.getUserRole().equals("admin")) {
                // Admin-specific menu
                io.adminMenu();
                String[] input = io.getUserInput("Enter your choice", 1);
                String choice = input[0];

                switch (choice) {
                    case "1": // Show product list
                        ProductListResult prodResult = productOp.getProductList(1);
                        io.showList("admin", "Product", prodResult.getProducts(),
                                prodResult.getCurrentPage(), prodResult.getTotalPages());
                        break;
                    case "2": // Add a new customer
                        String[] customerData = io.getUserInput("Enter username, password, email, mobile", 4);
                        boolean successAddCust = customerOp.registerCustomer(
                                customerData[0], customerData[1], customerData[2], customerData[3]
                        );
                        if (successAddCust) {
                            io.printMessage("Customer added successfully!");
                        } else {
                            io.printErrorMessage("Add Customer", "Failed to add customer. Check your input.");
                        }
                        break;
                    case "3": // Show customer list
                        CustomerListResult custResult = customerOp.getCustomerList(1);
                        io.showList("admin", "Customer", custResult.getCustomers(),
                                custResult.getCurrentPage(), custResult.getTotalPages());
                        break;
                    case "4": // Show orders using admin operation
                        adminOp.showOrders(scanner);
                        break;
                    case "5": // Generate test orders
                        orderOp.generateTestOrderData();
                        io.printMessage("Test data generated");
                        break;
                    case "6": // Generate all statistical figures
                        productOp.generateCategoryFigure();
                        productOp.generateDiscountFigure();
                        productOp.generateLikesCountFigure();
                        productOp.generateDiscountLikesCountFigure();
                        orderOp.generateAllCustomersConsumptionFigure();
                        orderOp.generateAllTop10BestSellersFigure();
                        io.printMessage("All statistical figures generated");
                        break;
                    case "7": // Delete all data from system
                        customerOp.deleteAllCustomers();
                        productOp.deleteAllProducts();
                        orderOp.deleteAllOrders();
                        io.printMessage("All data deleted");
                        break;
                    case "8": // Logout
                        currentUser = null;
                        break;
                    default:
                        io.printErrorMessage("Admin Menu", "Invalid choice");
                }
            } else {
                // Customer-specific menu
                io.customerMenu();
                String[] input = io.getUserInput("Enter your choice", 2);
                String choice = input[0];
                String keyword = input.length > 1 ? input[1] : "";

                switch (choice) {
                    case "1": // Show current user's profile
                        io.printObject(currentUser);
                        break;
                    case "2": // Update profile information
                        String[] updateData = io.getUserInput("Enter attribute (username/password/email/mobile) and new value", 2);
                        boolean successUpdate = customerOp.updateProfile(
                                updateData[0], updateData[1], (Customer) currentUser
                        );
                        if (successUpdate) {
                            io.printMessage("Profile updated successfully!");
                        } else {
                            io.printErrorMessage("Update Profile", "Failed to update profile. Check your input.");
                        }
                        break;
                    case "3": // Show products, optionally filter by keyword
                        if (!keyword.isEmpty()) {
                            List<Product> products = productOp.getProductListByKeyword(keyword);
                            io.showList("customer", "Product Search Results", products, 1, 1);
                        } else {
                            ProductListResult prodResultCust = productOp.getProductList(1);
                            io.showList("customer", "Product", prodResultCust.getProducts(),
                                    prodResultCust.getCurrentPage(), prodResultCust.getTotalPages());
                        }
                        break;
                    case "4": // Show all orders made by the customer
                        OrderListResult allOrdersResult = orderOp.getAllOrders(1);
                        io.showList("admin", "All Orders", allOrdersResult.getOrders(),
                                allOrdersResult.getCurrentPage(), allOrdersResult.getTotalPages());
                        break;
                    case "5": // Generate consumption figure for this customer
                        orderOp.generateSingleCustomerConsumptionFigure(currentUser.getUserId());
                        io.printMessage("Consumption figures generated");
                        break;
                    case "6": // Logout
                        currentUser = null;
                        break;
                    default:
                        io.printErrorMessage("Customer Menu", "Invalid choice");
                }
            }
        }

        // Final message on exit
        io.printMessage("Thank you for using our system. Goodbye!");
        scanner.close();
    }
}
