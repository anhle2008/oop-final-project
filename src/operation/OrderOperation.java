package operation;

import model.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton class responsible for managing Order objects.
 * Supports loading and saving orders from/to a file,
 * creating, deleting, and retrieving orders with pagination.
 */
public class OrderOperation {

    // Singleton instance
    private static OrderOperation instance;

    // In-memory list of all orders
    private List<Order> orders;

    // File path where orders are saved/loaded
    private static final String ORDERS_FILE = "data/orders.txt";

    /**
     * Private constructor to prevent external instantiation.
     * Initializes the orders list and loads existing orders from file.
     */
    private OrderOperation() {
        orders = new ArrayList<>();
        loadOrdersFromFile();
    }

    /**
     * Returns the singleton instance of OrderOperation.
     * Creates the instance if it does not exist.

     */
    public static OrderOperation getInstance() {
        if (instance == null) {
            instance = new OrderOperation();
        }
        return instance;
    }

    /**
     * Loads orders from the specified orders file.

     */
    private void loadOrdersFromFile() {
        File file = new File(ORDERS_FILE);
        File parentDir = file.getParentFile();

        // Create directories if they do not exist
        if (parentDir != null && !parentDir.exists()) {
            boolean dirsCreated = parentDir.mkdirs();
            if (!dirsCreated) {
                System.err.println("Failed to create directories for: " + ORDERS_FILE);
                return;
            }
        }

        // If file doesn't exist, no orders to load
        if (!file.exists()) {
            return;
        }

        // Read file line by line, parse each order and add to list
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("{") && line.endsWith("}")) {
                    Order order = parseOrder(line);
                    if (order != null) {
                        orders.add(order);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
    }

    /**
     * Parses a single order from a JSON-like string representation.
     *

     */
    private Order parseOrder(String orderString) {
        Map<String, String> map = new HashMap<>();
        try {
            // Remove curly braces and split by key-value pairs
            String content = orderString.substring(1, orderString.length() - 1);
            String[] pairs = content.split("\",\\s*\"");

            // Process each key-value pair
            for (String pair : pairs) {
                String[] keyValue = pair.split("\":\"?", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].replace("\"", "").trim();
                    String value = keyValue[1].replace("\"", "").trim();
                    map.put(key, value);
                }
            }

            // Create and return Order using extracted fields
            return new Order(
                    map.get("order_id"),
                    map.get("user_id"),
                    map.get("pro_id"),
                    map.get("order_time")
            );
        } catch (Exception e) {
            System.err.println("Error parsing order: " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves all current orders to the orders file.

     */
    private void saveOrdersToFile() {
        File file = new File(ORDERS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Order order : orders) {
                writer.write(order.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    /**
     * Generates a unique order ID using a prefix and a zero-padded random number.
     */
    public String generateUniqueOrderId() {
        return "o_" + String.format("%05d", (int)(Math.random() * 100000));
    }

    /**
     * Creates a new order and saves it.
     */
    public boolean createAnOrder(String customerId, String productId, String createTime) {
        String orderId = generateUniqueOrderId();
        // Use provided creation time or default fixed time (consider updating to current timestamp)
        String orderTime = createTime != null ? createTime : "01-01-2023_12:00:00";

        Order order = new Order(orderId, customerId, productId, orderTime);
        orders.add(order);
        saveOrdersToFile();
        return true;
    }

    /**
     * Deletes an order by order ID.
     */
    public boolean deleteOrder(String orderId) {
        Iterator<Order> iterator = orders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getOrderId().equals(orderId)) {
                iterator.remove();
                saveOrdersToFile();
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a paginated list of orders for a specific customer.
     */
    public OrderListResult getOrderList(String customerId, int pageNumber) {
        List<Order> customerOrders = orders.stream()
                .filter(o -> o.getUserId().equals(customerId))
                .collect(Collectors.toList());

        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) customerOrders.size() / pageSize);

        // Return empty result if page number is invalid
        if (pageNumber < 1 || pageNumber > totalPages) {
            return new OrderListResult(new ArrayList<>(), 0, 0);
        }

        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, customerOrders.size());

        return new OrderListResult(
                customerOrders.subList(fromIndex, toIndex),
                pageNumber,
                totalPages
        );
    }

    /**
     * Retrieves a paginated list of all orders (for admin use).
     */
    public OrderListResult getAllOrders(int pageNumber) {
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) orders.size() / pageSize);

        // Return empty result if page number is invalid
        if (pageNumber < 1 || pageNumber > totalPages) {
            return new OrderListResult(new ArrayList<>(), 0, 0);
        }

        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, orders.size());

        return new OrderListResult(
                orders.subList(fromIndex, toIndex),
                pageNumber,
                totalPages
        );
    }

    /**
     * Deletes all orders and clears the orders file.
     */
    public void deleteAllOrders() {
        orders.clear();
        saveOrdersToFile();
    }

    // Placeholder methods for generating test data and consumption figures


    public void generateTestOrderData() {
        // Implementation would create test data
    }


    public void generateSingleCustomerConsumptionFigure(String customerId) {
        // Implementation would use a charting library
    }


    public void generateAllCustomersConsumptionFigure() {
        // Implementation would use a charting library
    }


    public void generateAllTop10BestSellersFigure() {
        // Implementation would use a charting library
    }
}
