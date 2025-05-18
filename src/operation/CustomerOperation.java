package operation;

import model.*;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CustomerOperation {
    private static CustomerOperation instance;
    private List<Customer> customers;

    private CustomerOperation() {
        customers = new ArrayList<>();
        // Load customers from UserOperation's loaded data
        List<User> allUsers = UserOperation.getInstance().getAllUsers();
        for (User user : allUsers) {
            if (user instanceof Customer) {
                customers.add((Customer) user);
            }
        }
    }

    public static CustomerOperation getInstance() {
        if (instance == null) {
            instance = new CustomerOperation();
        }
        return instance;
    }

    public boolean validateEmail(String userEmail) {
        // Basic email validation
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        return pattern.matcher(userEmail).matches();
    }

    public boolean validateMobile(String userMobile) {
        // Exactly 10 digits, starting with 04 or 03
        Pattern pattern = Pattern.compile("^(03|04)\\d{8}$");
        return pattern.matcher(userMobile).matches();
    }

    public boolean registerCustomer(String userName, String userPassword,
                                    String userEmail, String userMobile) {
        UserOperation userOp = UserOperation.getInstance();

        if (userOp.checkUsernameExist(userName)) {
            return false;
        }

        if (!userOp.validateUsername(userName) ||
                !userOp.validatePassword(userPassword) ||
                !validateEmail(userEmail) ||
                !validateMobile(userMobile)) {
            return false;
        }

        String userId = userOp.generateUniqueUserId();
        String registerTime = "01-01-2023_12:00:00"; // Should be current time in real implementation
        Customer customer = new Customer(userId, userName, userPassword,
                registerTime, "customer",
                userEmail, userMobile);
        customers.add(customer);
        userOp.addUser(customer); // Ensure it's saved to file
        return true;
    }

    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        switch (attributeName.toLowerCase()) {
            case "username":
                if (UserOperation.getInstance().validateUsername(value)) {
                    customerObject.setUserName(value);
                    return true;
                }
                break;
            case "password":
                if (UserOperation.getInstance().validatePassword(value)) {
                    customerObject.setUserPassword(value);
                    return true;
                }
                break;
            case "email":
                if (validateEmail(value)) {
                    customerObject.setUserEmail(value);
                    return true;
                }
                break;
            case "mobile":
                if (validateMobile(value)) {
                    customerObject.setUserMobile(value);
                    return true;
                }
                break;
        }
        return false;
    }

    public boolean deleteCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getUserId().equals(customerId)) {
                customers.remove(customer);
                // In a real implementation, you would update the file here
                return true;
            }
        }
        return false;
    }

    public CustomerListResult getCustomerList(int pageNumber) {
        // Simple pagination - in a real system you'd want more robust implementation
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) customers.size() / pageSize);

        if (pageNumber < 1 || pageNumber > totalPages) {
            return new CustomerListResult(new ArrayList<>(), 0, 0);
        }

        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, customers.size());

        return new CustomerListResult(
                customers.subList(fromIndex, toIndex),
                pageNumber,
                totalPages
        );
    }

    public void deleteAllCustomers() {
        customers.clear();
        // In a real implementation, you would clear the file here
    }

    // Helper method to add customer (for testing)
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
}
