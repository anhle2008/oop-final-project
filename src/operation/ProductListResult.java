package operation;

import model.Product;
import java.util.List;

/**
 * Encapsulates a paginated result of products.
 * Useful for displaying product lists with pagination support.
 */
public class ProductListResult {

    // List of products for the current page
    private List<Product> products;

    // The current page number in the pagination
    private int currentPage;

    // The total number of pages available
    private int totalPages;

    /**
     * Constructor to initialize the paginated product list result.
     *
     * @param products    List of products for the current page
     * @param currentPage Current page number
     * @param totalPages  Total number of pages
     */
    public ProductListResult(List<Product> products, int currentPage, int totalPages) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    // Getter for the list of products
    public List<Product> getProducts() { return products; }

    // Getter for the current page number
    public int getCurrentPage() { return currentPage; }

    // Getter for the total number of pages
    public int getTotalPages() { return totalPages; }
}
