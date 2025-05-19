package operation;

import model.Customer;
import java.util.List;

/**
 * A utility class that encapsulates paginated results for a list of customers.
 * This is useful when displaying customer data in pages (e.g., in a CLI or UI).
 */
public class CustomerListResult {

    // List of customers on the current page
    private List<Customer> customers;

    // The current page number in the pagination
    private int currentPage;

    // The total number of pages available
    private int totalPages;

    /**
     * Constructor to initialize the paginated customer list result.
     *
     * @param customers   List of customers for the current page
     * @param currentPage Current page number
     * @param totalPages  Total number of pages
     */
    public CustomerListResult(List<Customer> customers, int currentPage, int totalPages) {
        this.customers = customers;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    // Getter for the list of customers
    public List<Customer> getCustomers() { return customers; }

    // Getter for the current page number
    public int getCurrentPage() { return currentPage; }

    // Getter for the total number of pages
    public int getTotalPages() { return totalPages; }
}
