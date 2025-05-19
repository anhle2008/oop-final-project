package operation;

import model.Product;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton class to manage product operations such as loading, saving,
 * searching, paginating, adding, and deleting products.
 */
public class ProductOperation {
    private static ProductOperation instance;  // Singleton instance
    private List<Product> products;            // In-memory list of products
    private static final String PRODUCTS_FILE = "data/products.txt";  // File to persist products

    /**
     * Private constructor to initialize the products list and load from file.
     */
    private ProductOperation() {
        products = new ArrayList<>();
        loadProductsFromFile();
    }

    /**
     * Get the singleton instance of ProductOperation.
     *
     * @return single instance of ProductOperation
     */
    public static ProductOperation getInstance() {
        if (instance == null) {
            instance = new ProductOperation();
        }
        return instance;
    }

    /**
     * Load products from the file into the in-memory list.
     * Creates parent directories if they don't exist.
     */
    private void loadProductsFromFile() {
        File file = new File(PRODUCTS_FILE);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            return;  // No file yet, so nothing to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Read each line and parse it into a Product object
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Product product = parseProduct(line);
                    if (product != null) {
                        products.add(product);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    /**
     * Parse a single line from the file into a Product object.
     * The line is expected to be a JSON-like string representing a product.
     *
     * @param line input string line
     * @return Product object or null if parsing fails
     */
    private Product parseProduct(String line) {
        try {
            // Remove surrounding braces if present
            line = line.trim();
            if (line.startsWith("{") && line.endsWith("}")) {
                line = line.substring(1, line.length() - 1);
            }

            Map<String, String> productMap = new HashMap<>();
            // Split by commas that are not inside quotes to separate key-value pairs
            String[] pairs = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            // Parse each key-value pair and populate the map
            for (String pair : pairs) {
                String[] kv = pair.split(":", 2);
                if (kv.length == 2) {
                    String key = kv[0].replaceAll("\"", "").trim();
                    String value = kv[1].replaceAll("\"", "").trim();
                    productMap.put(key, value);
                }
            }

            // Create a Product object using the parsed values
            return new Product(
                    productMap.get("pro_id"),
                    productMap.get("pro_model"),
                    productMap.get("pro_category"),
                    productMap.get("pro_name"),
                    Double.parseDouble(productMap.get("pro_current_price")),
                    Double.parseDouble(productMap.get("pro_raw_price")),
                    Double.parseDouble(productMap.get("pro_discount")),
                    Integer.parseInt(productMap.get("pro_likes_count"))
            );
        } catch (Exception e) {
            System.err.println("Error parsing product line: " + line);
            System.err.println("Exception: " + e.getMessage());
            return null;
        }
    }

    /**
     * Save the current list of products to the file.
     */
    private void saveProductsToFile() {
        File file = new File(PRODUCTS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Product product : products) {
                // Assumes Product.toString() returns a JSON-like representation
                writer.write(product.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    /**
     * Get a paginated list of products.
     *
     * @param pageNumber the page number to retrieve (1-based)
     * @return ProductListResult containing products and pagination info
     */
    public ProductListResult getProductList(int pageNumber) {
        int pageSize = 10;  // Number of products per page
        int totalPages = (int) Math.ceil((double) products.size() / pageSize);

        if (pageNumber < 1 || pageNumber > totalPages) {
            return new ProductListResult(new ArrayList<>(), 0, 0);
        }

        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, products.size());

        return new ProductListResult(
                products.subList(fromIndex, toIndex),
                pageNumber,
                totalPages
        );
    }

    /**
     * Delete a product by its ID.
     *
     * @param productId the ID of the product to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteProduct(String productId) {
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProId().equals(productId)) {
                iterator.remove();
                saveProductsToFile();
                return true;
            }
        }
        return false;
    }

    /**
     * Search products by a keyword in their name (case-insensitive).
     *
     * @param keyword the search keyword
     * @return list of matching products
     */
    public List<Product> getProductListByKeyword(String keyword) {
        return products.stream()
                .filter(p -> p.getProName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Find a product by its ID.
     *
     * @param productId the product ID to search
     * @return Product if found, otherwise null
     */
    public Product getProductById(String productId) {
        for (Product product : products) {
            if (product.getProId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    // Placeholder methods for generating various charts based on product data
    public void generateCategoryFigure() {
        // Charting logic placeholder
    }

    public void generateDiscountFigure() {
        // Charting logic placeholder
    }

    public void generateLikesCountFigure() {
        // Charting logic placeholder
    }

    public void generateDiscountLikesCountFigure() {
        // Charting logic placeholder
    }

    /**
     * Delete all products and save the empty list to the file.
     */
    public void deleteAllProducts() {
        products.clear();
        saveProductsToFile();
    }

    /**
     * Add a new product and save the updated list to the file.
     *
     * @param product the Product object to add
     */
    public void addProduct(Product product) {
        products.add(product);
        saveProductsToFile();
    }
}
