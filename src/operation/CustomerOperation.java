package src.operation;

import src.model.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

public class CustomerOperation {
    private static CustomerOperation instance;
    private final String userFile = "data/users.txt";

    private CustomerOperation() {}

    /**
     * Returns the single instance of CustomerOperation.
     * @return CustomerOperation instance
     */
    public static CustomerOperation getInstance() {
        if (instance == null) {
            instance = new CustomerOperation();
        }
        return instance;
    }

    /**
     * Validate the provided email address format. An email address
     * consists of username@domain.extension format.
     * @param userEmail The email to validate
     * @return true if valid, false otherwise
     */
    public boolean validateEmail(String userEmail) {
        if (userEmail == null) return false;
        return userEmail.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Validate the provided mobile number format. The mobile number
     * should be exactly 10 digits long, consisting only of numbers,
     * and starting with either '04' or '03'.
     * @param userMobile The mobile number to validate
     * @return true if valid, false otherwise
     */
    public boolean validateMobile(String userMobile) {
        if (userMobile == null) return false;
        return userMobile.matches("^(04|03)\\d{8}$");
    }

    /**
     * Save the information of the new customer into the data/users.txt file.
     * @param userName Customer's username
     * @param userPassword Customer's password
     * @param userEmail Customer's email
     * @param userMobile Customer's mobile number
     * @return true if success, false if failure
     */
    public boolean registerCustomer(String userName, String userPassword, String userEmail, String userMobile) {
        UserOperation userOp = UserOperation.getInstance();
        if (!userOp.validateUsername(userName)) return false;
        if (!userOp.validatePassword(userPassword)) return false;
        if (!validateEmail(userEmail)) return false;
        if (!validateMobile(userMobile)) return false;
        if (userOp.checkUsernameExist(userName)) return false;

        String userId = userOp.generateUniqueUserId();
        String encryptedPassword = userOp.encryptPassword(userPassword);
        String registerTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
        Customer customer = new Customer(userId, userName, encryptedPassword, registerTime, "customer", userEmail, userMobile);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile, true))) {
            bw.write(customer.toString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Update the given customer object's attribute value. According to
     * different attributes, perform appropriate validations.
     * @param attributeName The attribute to update
     * @param value The new value
     * @param customerObject The customer object to update
     * @return true if updated, false if failed
     */
    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        if (customerObject == null) return false;
        UserOperation userOp = UserOperation.getInstance();
        switch (attributeName) {
            case "user_name":
                if (!userOp.validateUsername(value)) return false;
                if (userOp.checkUsernameExist(value)) return false;
                customerObject.setUserName(value);
                break;
            case "user_password":
                if (!userOp.validatePassword(value)) return false;
                customerObject.setUserPassword(userOp.encryptPassword(value));
                break;
            case "user_email":
                if (!validateEmail(value)) return false;
                customerObject.setUserEmail(value);
                break;
            case "user_mobile":
                if (!validateMobile(value)) return false;
                customerObject.setUserMobile(value);
                break;
            default:
                return false;
        }
        // Save all customers again with update
        List<Customer> customers = getAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUserId().equals(customerObject.getUserId())) {
                customers.set(i, customerObject);
                break;
            }
        }
        return saveAllCustomers(customers);
    }

    /**
     * Delete the customer from the data/users.txt file based on the
     * provided customer_id.
     * @param customerId The ID of the customer to delete
     * @return true if deleted, false if failed
     */
    public boolean deleteCustomer(String customerId) {
        List<Customer> customers = getAllCustomers();
        boolean found = false;
        Iterator<Customer> iter = customers.iterator();
        while (iter.hasNext()) {
            Customer c = iter.next();
            if (c.getUserId().equals(customerId)) {
                iter.remove();
                found = true;
                break;
            }
        }
        if (found) {
            return saveAllCustomers(customers);
        }
        return false;
    }

    /**
     * Retrieve one page of customers from the data/users.txt.
     * One page contains a maximum of 10 customers.
     * @param pageNumber The page number to retrieve
     * @return A List of Customer objects, the current page number, and total pages
     */
    public CustomerListResult getCustomerList(int pageNumber) {
        List<Customer> allCustomers = getAllCustomers();
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) allCustomers.size() / pageSize);
        if (totalPages == 0) totalPages = 1;

        if (pageNumber < 1) pageNumber = 1;
        if (pageNumber > totalPages) pageNumber = totalPages;

        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, allCustomers.size());

        List<Customer> pageList = allCustomers.subList(start, end);

        return new CustomerListResult(pageList, pageNumber, totalPages);
    }

    /**
     * Removes all the customers from the data/users.txt file.
     */
    public void deleteAllCustomers() {
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains("\"user_role\":\"customer\"")) {
                    lines.add(line);
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper class to hold paginated customer list results
    public static class CustomerListResult {
        private List<Customer> customers;
        private int currentPage;
        private int totalPages;

        public CustomerListResult(List<Customer> customers, int currentPage, int totalPages) {
            this.customers = customers;
            this.currentPage = currentPage;
            this.totalPages = totalPages;
        }

        public List<Customer> getCustomers() { return customers; }
        public int getCurrentPage() { return currentPage; }
        public int getTotalPages() { return totalPages; }
    }

    // Private helpers

    private List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"user_role\":\"customer\"")) {
                    customers.add(parseCustomer(line));
                }
            }
        } catch (IOException ignored) {}
        return customers;
    }

    private Customer parseCustomer(String line) {
        String userId = extractField(line, "user_id");
        String userName = extractField(line, "user_name");
        String userPassword = extractField(line, "user_password");
        String userRegisterTime = extractField(line, "user_register_time");
        String userRole = extractField(line, "user_role");
        String userEmail = extractField(line, "user_email");
        String userMobile = extractField(line, "user_mobile");
        return new Customer(userId, userName, userPassword, userRegisterTime, userRole, userEmail, userMobile);
    }

    private boolean saveAllCustomers(List<Customer> customers) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
            for (Customer c : customers) {
                bw.write(c.toString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String extractField(String jsonLine, String field) {
        Pattern pattern = Pattern.compile("\"" + field + "\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonLine);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
