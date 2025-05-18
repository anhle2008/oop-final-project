

    import UI.IOInterface;
    import operation.*;
    import model.*;

    import java.util.List;


    public class Main {
        public static void main(String[] args) {
            // Initialize operations
            UserOperation userOp = UserOperation.getInstance();
            CustomerOperation customerOp = CustomerOperation.getInstance();
            AdminOperation adminOp = AdminOperation.getInstance();
            ProductOperation productOp = ProductOperation.getInstance();
            OrderOperation orderOp = OrderOperation.getInstance();
            IOInterface io = IOInterface.getInstance();

            // Register admin if not exists
            adminOp.registerAdmin();

            boolean running = true;
            User currentUser = null;

            while (running) {
                if (currentUser == null) {
                    // Show main menu
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
                        case "2": // Register
                            String[] regData = io.getUserInput("Enter username, password, email, mobile", 4);
                            boolean success = customerOp.registerCustomer(
                                    regData[0], regData[1], regData[2], regData[3]
                            );
                            if (success) {
                                io.printMessage("Registration successful! Please login.");
                            } else {
                                io.printErrorMessage("Registration", "Failed to register. Check your input.");
                            }
                            break;
                        case "3": // Quit
                            running = false;
                            break;
                        default:
                            io.printErrorMessage("Main Menu", "Invalid choice");
                    }
                } else if (currentUser.getUserRole().equals("admin")) {
                    // Admin menu
                    io.adminMenu();
                    String[] input = io.getUserInput("Enter your choice", 1);
                    String choice = input[0];

                    switch (choice) {
                        case "1": // Show products
                            ProductListResult result = productOp.getProductList(1);
                            io.showList("admin", "Product", result.getProducts(),
                                    result.getCurrentPage(), result.getTotalPages());
                            break;
                        case "2": // Add customers
                            String[] customerData = io.getUserInput("Enter username, password, email, mobile", 4);
                            boolean success = customerOp.registerCustomer(
                                    customerData[0], customerData[1], customerData[2], customerData[3]
                            );
                            if (success) {
                                io.printMessage("Customer added successfully!");
                            } else {
                                io.printErrorMessage("Add Customer", "Failed to add customer. Check your input.");
                            }
                            break;
                        case "3": // Show customers
                            CustomerListResult custResult = customerOp.getCustomerList(1);
                            io.showList("admin", "Customer", custResult.getCustomers(),
                                    custResult.getCurrentPage(), custResult.getTotalPages());
                            break;
                        case "4": // Show orders
                            // In a real implementation, you might want to show all orders
                            io.printMessage("Order list functionality not fully implemented");
                            break;
                        case "5": // Generate test data
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
                        case "7": // Delete all data
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
                    // Customer menu
                    io.customerMenu();
                    String[] input = io.getUserInput("Enter your choice", 2);
                    String choice = input[0];
                    String keyword = input.length > 1 ? input[1] : "";

                    switch (choice) {
                        case "1": // Show profile
                            io.printObject(currentUser);
                            break;
                        case "2": // Update profile
                            String[] updateData = io.getUserInput("Enter attribute (username/password/email/mobile) and new value", 2);
                            boolean success = customerOp.updateProfile(
                                    updateData[0], updateData[1], (Customer) currentUser
                            );
                            if (success) {
                                io.printMessage("Profile updated successfully!");
                            } else {
                                io.printErrorMessage("Update Profile", "Failed to update profile. Check your input.");
                            }
                            break;
                        case "3": // Show products
                            if (!keyword.isEmpty()) {
                                List<Product> products = productOp.getProductListByKeyword(keyword);
                                io.showList("customer", "Product Search Results", products, 1, 1);
                            } else {
                                ProductListResult prodResult = productOp.getProductList(1);
                                io.showList("customer", "Product", prodResult.getProducts(),
                                        prodResult.getCurrentPage(), prodResult.getTotalPages());
                            }
                            break;
                        case "4": // Show history orders
                            OrderListResult orderResult = orderOp.getOrderList(currentUser.getUserId(), 1);
                            io.showList("customer", "Order", orderResult.getOrders(),
                                    orderResult.getCurrentPage(), orderResult.getTotalPages());
                            break;
                        case "5": // Generate all consumption figures
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

            io.printMessage("Thank you for using our system. Goodbye!");
        }
    }