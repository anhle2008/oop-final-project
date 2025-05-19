package operation;

import model.*;

import java.io.*;
import java.util.*;
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
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        return pattern.matcher(userEmail).matches();
    }

    public boolean validateMobile(String userMobile) {
        Pattern pattern = Pattern.compile("^(03|04)\\d{8}$");
        return pattern.matcher(userMobile).matches();
    }

    public boolean registerCustomer(String userName, String userPassword,
                                    String userEmail, String userMobile) {
        UserOperation userOp = UserOperation.getInstance();

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

        Customer customer = new Customer(userId, userName, userOp.encryptPassword(userPassword),
                registerTime, "customer", userEmail, userMobile);
        customers.add(customer);
        userOp.addUser(customer);
        saveAllUsers(userOp.getAllUsers());

        System.out.println("DEBUG: Customer registered successfully: " + userName);
        return true;
    }

    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        value = value.trim();
        boolean updated = false;
        UserOperation userOp = UserOperation.getInstance();

        switch (attributeName.toLowerCase()) {
            case "username":
                if (userOp.validateUsername(value)) {
                    customerObject.setUserName(value);
                    updated = true;
                }
                break;
            case "password":
                if (userOp.validatePassword(value)) {
                    customerObject.setUserPassword(userOp.encryptPassword(value));
                    updated = true;
                }
                break;
            case "email":
                if (validateEmail(value)) {
                    customerObject.setUserEmail(value);
                    updated = true;
                }
                break;
            case "mobile":
                if (validateMobile(value)) {
                    customerObject.setUserMobile(value);
                    updated = true;
                }
                break;
        }

        if (updated) {
            saveAllUsers(userOp.getAllUsers());
        }
        return updated;
    }

    public boolean deleteCustomer(String customerId) {
        Iterator<Customer> iterator = customers.iterator();
        boolean removed = false;

        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (customer.getUserId().equals(customerId)) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (removed) {
            List<User> allUsers = UserOperation.getInstance().getAllUsers();
            allUsers.removeIf(u -> u.getUserId().equals(customerId));
            saveAllUsers(allUsers);
            return true;
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
        List<User> remainingUsers = new ArrayList<>();
        for (User user : UserOperation.getInstance().getAllUsers()) {
            if (!(user instanceof Customer)) {
                remainingUsers.add(user);
            }
        }
        saveAllUsers(remainingUsers);
    }

    // Helper method to add customer (for testing)
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    // Helper method to save all users (admin + customers) to file
    private void saveAllUsers(List<User> userList) {
        File file = new File("data/users.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (User user : userList) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
}
