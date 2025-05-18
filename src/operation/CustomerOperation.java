package operation;

import model.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import java.time.*;
import java.time.format.DateTimeFormatter;

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
        // Improved email validation: no trailing spaces, common pattern
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        return pattern.matcher(userEmail).matches();
    }

    public boolean validateMobile(String userMobile) {
        // Exactly 10 digits, starting with 03 or 04
        Pattern pattern = Pattern.compile("^(03|04)\\d{8}$");
        return pattern.matcher(userMobile).matches();
    }

    public boolean registerCustomer(String userName, String userPassword,
                                    String userEmail, String userMobile) {
        UserOperation userOp = UserOperation.getInstance();

        // Trim inputs
        userName = userName.trim();
        userPassword = userPassword.trim();
        userEmail = userEmail.trim();
        userMobile = userMobile.trim();

        if (userOp.checkUsernameExist(userName)) {
            System.out.println("DEBUG: Username already exists: " + userName);
            return false;
        }

        if (!userOp.validateUsername(userName)) {
            System.out.println("DEBUG: Invalid username: " + userName);
            return false;
        }
        if (!userOp.validatePassword(userPassword)) {
            System.out.println("DEBUG: Invalid password.");
            return false;
        }
        if (!validateEmail(userEmail)) {
            System.out.println("DEBUG: Invalid email: " + userEmail);
            return false;
        }
        if (!validateMobile(userMobile)) {
            System.out.println("DEBUG: Invalid mobile: " + userMobile);
            return false;
        }

        String userId = userOp.generateUniqueUserId();

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy_hh:mm:ss");
        String registerTime = currentTime.format(timeFormat);

        Customer customer = new Customer(userId, userName, userPassword,
                registerTime, "customer",
                userEmail, userMobile);
        customers.add(customer);
        userOp.addUser(customer); // Ensure it's saved to file
        System.out.println("DEBUG: Customer registered successfully: " + userName);
        return true;
    }

    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        value = value.trim();
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
        Iterator<Customer> iterator = customers.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (customer.getUserId().equals(customerId)) {
                iterator.remove();
                // In a real implementation, you would update the file here
                return true;
            }
        }
        return false;
    }

    public CustomerListResult getCustomerList(int pageNumber) {
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
