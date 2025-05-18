package operation;

import model.*;




import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminOperation {
    private static AdminOperation instance;
    private List<Admin> admins;

    private AdminOperation() {
        admins = new ArrayList<>();
        // In a real implementation, you would load admins from file here
    }

    public static AdminOperation getInstance() {
        if (instance == null) {
            instance = new AdminOperation();
        }
        return instance;
    }

    public void registerAdmin() {
        // Check if admin already exists
        if (!admins.isEmpty()) {
            return;
        }

        UserOperation userOp = UserOperation.getInstance();
        String adminId = userOp.generateUniqueUserId();
        Admin admin = new Admin(adminId, "admin", "admin123",
                "01-01-2023_00:00:00", "admin");
        admins.add(admin);
        // In a real implementation, you would save to file here
    }

    public void showOrders(Scanner scanner) {
        OrderOperation orderOp = OrderOperation.getInstance();
        int page = 1;
        while (true) {
            OrderListResult result = orderOp.getAllOrders(page);
            List<Order> orders = result.getOrders();

            if (orders.isEmpty()) {
                System.out.println("No orders found.");
                break;
            }

            System.out.println("\n=== Orders Page " + result.getCurrentPage() + " of " + result.getTotalPages() + " ===");
            for (Order order : orders) {
                System.out.println(order.toString());
            }

            System.out.println("\n[N] Next page | [P] Previous page | [E] Exit");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("n")) {
                if (page < result.getTotalPages()) {
                    page++;
                } else {
                    System.out.println("You're on the last page.");
                }
            } else if (choice.equals("p")) {
                if (page > 1) {
                    page--;
                } else {
                    System.out.println("You're on the first page.");
                }
            } else if (choice.equals("e")) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
