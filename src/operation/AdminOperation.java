    package operation;

    import model.*;


    import java.util.*;

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


}