package src.operation;

import src.model.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ProductOperation {
    private static ProductOperation instance;
    private final String productFile = "data/products.txt";
    private final Random random = new Random();

    private ProductOperation() {}

    public static ProductOperation getInstance() {
        if (instance == null) {
            instance = new ProductOperation();
        }
        return instance;
    }

    public String generateUniqueProductId() {
        String id;
        do {
            id = "p_" + String.format("%010d", random.nextInt(1_000_000_000));
        } while (productIdExists(id));
        return id;
    }

    private boolean productIdExists(String productId) {
        try (BufferedReader br = new BufferedReader(new FileReader(productFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"product_id\":\"" + productId + "\"")) return true;
            }
        } catch (IOException ignored) {}
        return false;
    }

    public boolean addProduct(Product product) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(productFile, true))) {
            bw.write(product.toString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        List<Product> products = getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId().equals(product.getProductId())) {
                products.set(i, product);
                return saveAllProducts(products);
            }
        }
        return false;
    }

    public boolean deleteProduct(String productId) {
        List<Product> products = getAllProducts();
        boolean found = false;
        Iterator<Product> iter = products.iterator();
        while (iter.hasNext()) {
            Product p = iter.next();
            if (p.getProductId().equals(productId)) {
                iter.remove();
                found = true;
                break;
            }
        }
        if (found) {
            return saveAllProducts(products);
        }
        return false;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(productFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                products.add(parseProduct(line));
            }
        } catch (IOException ignored) {}
        return products;
    }

    private Product parseProduct(String line) {
        String proId = extractField(line, "pro_id");
        String proModel = extractField(line, "pro_model");
        String proCategory = extractField(line, "pro_category");
        String proName = extractField(line, "pro_name");
        double proCurrentPrice = Double.parseDouble(extractField(line, "pro_current_price"));
        double proRawPrice = Double.parseDouble(extractField(line, "pro_raw_price"));
        double proDiscount = Double.parseDouble(extractField(line, "pro_discount"));
        int proLikesCount = Integer.parseInt(extractField(line, "pro_likes_count"));

        return new Product(proId, proModel, proCategory, proName, proCurrentPrice, proRawPrice, proDiscount, proLikesCount);
    }

    private boolean saveAllProducts(List<Product> products) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(productFile))) {
            for (Product p : products) {
                bw.write(p.toString());
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
