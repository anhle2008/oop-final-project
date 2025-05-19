package model;

/**
 * Represents an Admin user with elevated permissions.
 */
public class Admin extends User {

    /**
     * Full constructor to initialize all fields.
     */
    public Admin(String userId, String userName, String userPassword,
                 String userRegisterTime, String userRole) {
        super(userId, userName, userPassword, userRegisterTime, userRole);
    }

    /**
     * Default constructor setting the userRole to "admin".
     */
    public Admin() {
        super();
        this.userRole = "admin";
    }

    /**
     * Converts admin user data to a JSON-like string for file saving.
     */
    @Override
    public String toString() {
        return String.format("{\"user_id\":\"%s\",\"user_name\":\"%s\",\"user_password\":\"%s\"," +
                        "\"user_register_time\":\"%s\",\"user_role\":\"%s\"}",
                userId, userName, userPassword, userRegisterTime, userRole);
    }
}
