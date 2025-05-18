package src.model;

public class Product {
    private String proId;
    private String proModel;
    private String proCategory;
    private String proName;
    private double proCurrentPrice;
    private double proRawPrice;
    private double proDiscount;
    private int proLikesCount;

    // Constructor with 8 arguments
    public Product(String proId, String proModel, String proCategory, String proName,
                   double proCurrentPrice, double proRawPrice, double proDiscount, int proLikesCount) {
        this.proId = proId;
        this.proModel = proModel;
        this.proCategory = proCategory;
        this.proName = proName;
        this.proCurrentPrice = proCurrentPrice;
        this.proRawPrice = proRawPrice;
        this.proDiscount = proDiscount;
        this.proLikesCount = proLikesCount;
    }

    // Default constructor
    public Product() {
        this.proId = "p_00000";
        this.proModel = "default_model";
        this.proCategory = "general";
        this.proName = "default_product";
        this.proCurrentPrice = 0.0;
        this.proRawPrice = 0.0;
        this.proDiscount = 0.0;
        this.proLikesCount = 0;
    }


    // Getters
    public String getProductId() {
        return proId;
    }

    public String getProModel() {
        return proModel;
    }

    public String getProCategory() {
        return proCategory;
    }

    public String getProName() {
        return proName;
    }

    public double getProCurrentPrice() {
        return proCurrentPrice;
    }

    public double getProRawPrice() {
        return proRawPrice;
    }

    public double getProDiscount() {
        return proDiscount;
    }

    public int getProLikesCount() {
        return proLikesCount;
    }

    // Setters
    public void setProductId(String proId) {
        this.proId = proId;
    }

    public void setProModel(String proModel) {
        this.proModel = proModel;
    }

    public void setProCategory(String proCategory) {
        this.proCategory = proCategory;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public void setProCurrentPrice(double proCurrentPrice) {
        this.proCurrentPrice = proCurrentPrice;
    }

    public void setProRawPrice(double proRawPrice) {
        this.proRawPrice = proRawPrice;
    }

    public void setProDiscount(double proDiscount) {
        this.proDiscount = proDiscount;
    }

    public void setProLikesCount(int proLikesCount) {
        this.proLikesCount = proLikesCount;
    }

    // toString method (JSON format)
    @Override
    public String toString() {
        return String.format(
                "{\"pro_id\":\"%s\", \"pro_model\":\"%s\", \"pro_category\":\"%s\", \"pro_name\":\"%s\", " +
                        "\"pro_current_price\":\"%.2f\", \"pro_raw_price\":\"%.2f\", \"pro_discount\":\"%.2f\", \"pro_likes_count\":\"%d\"}",
                proId, proModel, proCategory, proName, proCurrentPrice, proRawPrice, proDiscount, proLikesCount
        );
    }
}
