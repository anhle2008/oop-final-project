package oop_lab_final_assignment.app.model;

import org.json.JSONObject;

public class Order {
    private String orderId;
    private String userId;
    private String proId;
    private String orderTime;

    public Order(String orderId, String userId, String proId, String orderTime)
    {
        this.orderId = orderId;
        this.userId = userId;
        this.proId = proId;
        this.orderTime = orderTime;
    }

    public Order() {

    }

    @Override
    public String toString()
    {
        JSONObject json = new JSONObject();

        json.put("order_id", orderId);
        json.put("user_id", userId);
        json.put("pro_id", proId);
        json.put("order_time", orderTime);

        return json.toString();
    }
}
