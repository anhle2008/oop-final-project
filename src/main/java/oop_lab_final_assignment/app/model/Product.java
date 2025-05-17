package oop_lab_final_assignment.app.model;

import org.json.JSONObject;

public class Product {
    private String proId;
    private String proModel;
    private String proCategory;
    private String proName;
    private double proCurrentPrice;
    private double proRawPrice;
    private double proDiscount;
    private int proLikesCount;

    public Product(String proId, String proModel, String proCategory,
                String proName, double proCurrentPrice, double proRawPrice,
                double proDiscount, int proLikesCount)
    {
        this.proId = proId;
        this.proModel = proModel;
        this.proCategory = proCategory;
        this.proName = proName;
        this.proCurrentPrice = proCurrentPrice;
        this.proRawPrice = proRawPrice;
        this.proDiscount = proDiscount;
        this.proLikesCount = proLikesCount;
    }

    public Product() {

    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();

        json.put("pro_id", proId);
        json.put("pro_model", proModel);
        json.put("pro_category", proCategory);
        json.put("pro_name", proName);
        json.put("pro_current_price", proCurrentPrice);
        json.put("pro_raw_price", proRawPrice);
        json.put("pro_discount", proDiscount);
        json.put("pro_likes_count", proLikesCount);

        return json.toString();
    }
}
