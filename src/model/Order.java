package model;

/**
 * Represents an Order entity with order ID, user ID, product ID, and order time.
 */
public class Order {
    private String orderId;   // Unique identifier for the order
    private String userId;    // ID of the user who placed the order
    private String proId;     // ID of the product ordered
    private String orderTime; // Timestamp of when the order was placed

    /**
     * Constructor to create an Order with all fields specified.
     *
     * @param orderId   the order ID
     * @param userId    the user ID
     * @param proId     the product ID
     * @param orderTime the order timestamp
     */
    public Order(String orderId, String userId, String proId, String orderTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.proId = proId;
        this.orderTime = orderTime;
    }

    /**
     * Default constructor initializing all fields to empty strings.
     */
    public Order() {
        this("", "", "", "");
    }

    // Getters and Setters for encapsulation
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getProId() { return proId; }
    public void setProId(String proId) { this.proId = proId; }
    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }

    /**
     * Returns a JSON-like string representation of the Order.
     *
     * @return formatted string with order details
     */
    @Override
    public String toString() {
        return String.format("{\"order_id\":\"%s\", \"user_id\":\"%s\", \"pro_id\":\"%s\", \"order_time\":\"%s\"}",
                orderId, userId, proId, orderTime);
    }
}
