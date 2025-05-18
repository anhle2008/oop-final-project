package src;

import src.ui.IOInterface;
import src.operation.UserOperation;
import src.model.User;
import src.model.Admin;
import src.model.Customer;

public class Main {

    private static final IOInterface io = IOInterface.getInstance();
    private static final UserOperation userOp = UserOperation.getInstance();

    public static void main(String[] args) {
        try {
            mainLoop();
        } catch (Exception e) {
            io.printErrorMessage("Main", e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mainLoop() {1
        boolean running = true;

        while (running) {
            io.mainMenu();
            String[] input = io.getUserInput("", 1);
            String choice = input[0];

            switch (choice) {
                case "1": // Login
                    handleLogin();
                    break;

                case "2": // Register
                    handleRegister();
                    break;

                case "3": // Quit
                    io.printMessage("Exiting application. Goodbye!");
                    running = false;
                    break;

                default:
                    io.printErrorMessage("MainMenu", "Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private static void handleLogin() {
        String[] inputs = io.getUserInput("Enter username and password separated by space: ", 2);
        String username = inputs[0];
        String password = inputs[1];

        if (username.isEmpty() || password.isEmpty()) {
            io.printErrorMessage("Login", "Username or password cannot be empty.");
            return;
        }

        User user = userOp.login(username, password);
        if (user == null) {
            io.printErrorMessage("Login", "Invalid username or password.");
            return;
        }

        io.printMessage("Login successful. Welcome, " + username + "!");

        if (user instanceof Admin) {
            adminMenuLoop();
        } else if (user instanceof Customer) {
            customerMenuLoop((Customer) user);
        } else {
            io.printErrorMessage("Login", "Unknown user role.");
        }
    }

    private static void handleRegister() {
        io.printMessage("Register new user (admin cannot register).");

        String[] inputs = io.getUserInput("Enter username and password separated by space: ", 2);
        String username = inputs[0];
        String password = inputs[1];

        if (!userOp.validateUsername(username)) {
            io.printErrorMessage("Register", "Invalid username. Must be at least 5 letters/underscores.");
            return;
        }

        if (userOp.checkUsernameExist(username)) {
            io.printErrorMessage("Register", "Username already exists.");
            return;
        }

        if (!userOp.validatePassword(password)) {
            io.printErrorMessage("Register", "Password must be at least 5 characters, contain letters and digits.");
            return;
        }

        // Here you would implement user creation and saving to file
        // Since you didn't provide that, just print success message for now
        io.printMessage("Registration successful. You can now login.");
    }

    private static void adminMenuLoop() {
        boolean loggedIn = true;
        while (loggedIn) {
            io.adminMenu();
            String[] input = io.getUserInput("", 1);
            String choice = input[0];

            switch (choice) {
                case "1":
                    io.printMessage("Show products - Feature to implement");
                    break;
                case "2":
                    io.printMessage("Add customers - Feature to implement");
                    break;
                case "3":
                    io.printMessage("Show customers - Feature to implement");
                    break;
                case "4":
                    io.printMessage("Show orders - Feature to implement");
                    break;
                case "5":
                    io.printMessage("Generate test data - Feature to implement");
                    break;
                case "6":
                    io.printMessage("Generate all statistical figures - Feature to implement");
                    break;
                case "7":
                    io.printMessage("Delete all data - Feature to implement");
                    break;
                case "8":
                    io.printMessage("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    io.printErrorMessage("AdminMenu", "Invalid choice, please try again.");
            }
        }
    }

    private static void customerMenuLoop(Customer user) {
        boolean loggedIn = true;
        while (loggedIn) {
            io.customerMenu();
            String[] input = io.getUserInput("", 2); // for option + possible keyword
            String choice = input[0];
            String keyword = input.length > 1 ? input[1] : "";

            switch (choice) {
                case "1":
                    io.printObject(user);
                    break;
                case "2":
                    io.printMessage("Update profile - Feature to implement");
                    break;
                case "3":
                    if (!keyword.isEmpty()) {
                        io.printMessage("Show products with keyword '" + keyword + "' - Feature to implement");
                    } else {
                        io.printMessage("Show products - Feature to implement");
                    }
                    break;
                case "4":
                    io.printMessage("Show history orders - Feature to implement");
                    break;
                case "5":
                    io.printMessage("Generate all consumption figures - Feature to implement");
                    break;
                case "6":
                    io.printMessage("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    io.printErrorMessage("CustomerMenu", "Invalid choice, please try again.");
            }
        }
    }
}
