package model;

/**
 * Represents a Product with its details such as ID, model, category, name, pricing, discount, and popularity.
 */
public class Product {
    private String proId;            // Unique product identifier
    private String proModel;         // Model number or name of the product
    private String proCategory;      // Category to which the product belongs
    private String proName;          // Name of the product
    private double proCurrentPrice;  // Current selling price of the product
    private double proRawPrice;      // Original price before discount
    private double proDiscount;      // Discount percentage or value applied to the product
    private int proLikesCount;       // Number of likes/popularity count for the product

    /**
     * Constructs a Product with all fields specified.
     *
     * @param proId           Unique product ID
     * @param proModel        Product model
     * @param proCategory     Category of the product
     * @param proName         Name of the product
     * @param proCurrentPrice Current price after discount
     * @param proRawPrice     Original price before discount
     * @param proDiscount     Discount value or percentage
     * @param proLikesCount   Number of likes/popularity
     */
    public Product(String proId, String proModel, String proCategory,
                   String proName, double proCurrentPrice, double proRawPrice,
                   double proDiscount, int proLikesCount) {
        this.proId = proId;
        this.proModel = proModel;
        this.proCategory = proCategory;
        this.proName = proName;
        this.proCurrentPrice = proCurrentPrice;
        this.proRawPrice = proRawPrice;
        this.proDiscount = proDiscount;
        this.proLikesCount = proLikesCount;
    }

    /**
     * Default constructor initializing all fields with default values.
     */
    public Product() {
        this("", "", "", "", 0.0, 0.0, 0.0, 0);
    }

    // Getters and Setters for all fields

    public String getProId() { return proId; }
    public void setProId(String proId) { this.proId = proId; }

    public String getProModel() { return proModel; }
    public void setProModel(String proModel) { this.proModel = proModel; }

    public String getProCategory() { return proCategory; }
    public void setProCategory(String proCategory) { this.proCategory = proCategory; }

    public String getProName() { return proName; }
    public void setProName(String proName) { this.proName = proName; }

    public double getProCurrentPrice() { return proCurrentPrice; }
    public void setProCurrentPrice(double proCurrentPrice) { this.proCurrentPrice = proCurrentPrice; }

    public double getProRawPrice() { return proRawPrice; }
    public void setProRawPrice(double proRawPrice) { this.proRawPrice = proRawPrice; }

    public double getProDiscount() { return proDiscount; }
    public void setProDiscount(double proDiscount) { this.proDiscount = proDiscount; }

    public int getProLikesCount() { return proLikesCount; }
    public void setProLikesCount(int proLikesCount) { this.proLikesCount = proLikesCount; }

    /**
     * Returns a JSON-like string representation of the product's properties.
     *
     * @return formatted string with product details
     */
    @Override
    public String toString() {
        return String.format("{\"pro_id\":\"%s\", \"pro_model\":\"%s\", \"pro_category\":\"%s\", " +
                        "\"pro_name\":\"%s\", \"pro_current_price\":%.2f, \"pro_raw_price\":%.2f, " +
                        "\"pro_discount\":%.2f, \"pro_likes_count\":%d}",
                proId, proModel, proCategory, proName,
                proCurrentPrice, proRawPrice, proDiscount, proLikesCount);
    }
}
