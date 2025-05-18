package src.operation;

import src.model.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class OrderOperation {
    private static OrderOperation instance;
    private final String orderFile = "data/orders.txt";
    private final Random random = new Random();

    private OrderOperation() {}

    public static OrderOperation getInstance() {
        if (instance == null) {
            instance = new OrderOperation();
        }
        return instance;
    }

    public String generateUniqueOrderId() {
        String id;
        do {
            id = "o_" + String.format("%010d", random.nextInt(1_000_000_000));
        } while (orderIdExists(id));
        return id;
    }

    private boolean orderIdExists(String orderId) {
        try (BufferedReader br = new BufferedReader(new FileReader(orderFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"order_id\":\"" + orderId + "\"")) return true;
            }
        } catch (IOException ignored) {}
        return false;
    }

    public boolean addOrder(Order order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(orderFile, true))) {
            bw.write(order.toString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean updateOrder(Order order) {
        List<Order> orders = getAllOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId().equals(order.getOrderId())) {
                orders.set(i, order);
                return saveAllOrders(orders);
            }
        }
        return false;
    }

    public boolean deleteOrder(String orderId) {
        List<Order> orders = getAllOrders();
        boolean found = false;
        Iterator<Order> iter = orders.iterator();
        while (iter.hasNext()) {
            Order o = iter.next();
            if (o.getOrderId().equals(orderId)) {
                iter.remove();
                found = true;
                break;
            }
        }
        if (found) {
            return saveAllOrders(orders);
        }
        return false;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(orderFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                orders.add(parseOrder(line));
            }
        } catch (IOException ignored) {}
        return orders;
    }

    private Order parseOrder(String line) {
        String orderId = extractField(line, "order_id");
        String userId = extractField(line, "user_id");
        String proId = extractField(line, "pro_id");
        String orderTime = extractField(line, "order_time");
        return new Order(orderId, userId, proId, orderTime);

    }

    private boolean saveAllOrders(List<Order> orders) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(orderFile))) {
            for (Order o : orders) {
                bw.write(o.toString());
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
