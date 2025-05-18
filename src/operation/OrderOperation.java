package operation;

import model.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class OrderOperation {
    private static OrderOperation instance;
    private List<Order> orders;
    private static final String ORDERS_FILE = "data/orders.txt";

    private OrderOperation() {
        orders = new ArrayList<>();
        loadOrdersFromFile();
    }

    public static OrderOperation getInstance() {
        if (instance == null) {
            instance = new OrderOperation();
        }
        return instance;
    }

    private void loadOrdersFromFile() {
        File file = new File(ORDERS_FILE);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            boolean dirsCreated = parentDir.mkdirs();
            if (!dirsCreated) {
                System.err.println("Failed to create directories for: " + ORDERS_FILE);
                return;
            }
        }

        if (!file.exists()) {
            return;
        }

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

    private Order parseOrder(String orderString) {
        Map<String, String> map = new HashMap<>();
        try {
            String content = orderString.substring(1, orderString.length() - 1);
            String[] pairs = content.split("\",\\s*\"");

            for (String pair : pairs) {
                String[] keyValue = pair.split("\":\"?", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].replace("\"", "").trim();
                    String value = keyValue[1].replace("\"", "").trim();
                    map.put(key, value);
                }
            }

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

    private void saveOrdersToFile() {
        File file = new File(ORDERS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Order order : orders) {
                writer.write(order.toString()); // Assumes Order.toString() returns a JSON-like format
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    public String generateUniqueOrderId() {
        return "o_" + String.format("%05d", (int)(Math.random() * 100000));
    }

    public boolean createAnOrder(String customerId, String productId, String createTime) {
        String orderId = generateUniqueOrderId();
        String orderTime = createTime != null ? createTime : "01-01-2023_12:00:00"; // You can update this to current timestamp if needed

        Order order = new Order(orderId, customerId, productId, orderTime);
        orders.add(order);
        saveOrdersToFile();
        return true;
    }

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

    public OrderListResult getOrderList(String customerId, int pageNumber) {
        List<Order> customerOrders = orders.stream()
                .filter(o -> o.getUserId().equals(customerId))
                .collect(Collectors.toList());

        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) customerOrders.size() / pageSize);

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

    public void deleteAllOrders() {
        orders.clear();
        saveOrdersToFile();
    }

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
