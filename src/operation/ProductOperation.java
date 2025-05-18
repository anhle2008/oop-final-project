package operation;

import model.Product;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProductOperation {
    private static ProductOperation instance;
    private List<Product> products;
    private static final String PRODUCTS_FILE = "data/products.txt";

    private ProductOperation() {
        products = new ArrayList<>();
        loadProductsFromFile();
    }

    public static ProductOperation getInstance() {
        if (instance == null) {
            instance = new ProductOperation();
        }
        return instance;
    }

    private void loadProductsFromFile() {
        File file = new File(PRODUCTS_FILE);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
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

    private Product parseProduct(String line) {
        try {
            // Remove surrounding braces if they exist
            line = line.trim();
            if (line.startsWith("{") && line.endsWith("}")) {
                line = line.substring(1, line.length() - 1);
            }

            Map<String, String> productMap = new HashMap<>();
            // Split by comma only if not inside quotes
            String[] pairs = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            for (String pair : pairs) {
                String[] kv = pair.split(":", 2);
                if (kv.length == 2) {
                    String key = kv[0].replaceAll("\"", "").trim();
                    String value = kv[1].replaceAll("\"", "").trim();
                    productMap.put(key, value);
                }
            }

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


    private void saveProductsToFile() {
        File file = new File(PRODUCTS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Product product : products) {
                writer.write(product.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    public ProductListResult getProductList(int pageNumber) {
        int pageSize = 10;
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

    public List<Product> getProductListByKeyword(String keyword) {
        return products.stream()
                .filter(p -> p.getProName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Product getProductById(String productId) {
        for (Product product : products) {
            if (product.getProId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

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

    public void deleteAllProducts() {
        products.clear();
        saveProductsToFile();
    }

    public void addProduct(Product product) {
        products.add(product);
        saveProductsToFile();
    }
}
