package operation;

import model.Order;
import java.util.List;

/**
 * Encapsulates a paginated result of orders.
 * Useful for displaying a list of orders with pagination support.
 */
public class OrderListResult {

    // List of orders on the current page
    private List<Order> orders;

    // The current page number in the pagination
    private int currentPage;

    // The total number of pages available
    private int totalPages;

    /**
     * Constructor to initialize the paginated order list result.
     *
     * @param orders      List of orders for the current page
     * @param currentPage Current page number
     * @param totalPages  Total number of pages
     */
    public OrderListResult(List<Order> orders, int currentPage, int totalPages) {
        this.orders = orders;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    // Getter for the list of orders
    public List<Order> getOrders() { return orders; }

    // Getter for the current page number
    public int getCurrentPage() { return currentPage; }

    // Getter for the total number of pages
    public int getTotalPages() { return totalPages; }
}
