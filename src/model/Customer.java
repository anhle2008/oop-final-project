package model;

/**
 * Represents a customer user with email and mobile contact information.
 */
public class Customer extends User {
    private String userEmail;
    private String userMobile;

    /**
     * Full constructor for Customer with all fields.
     */
    public Customer(String userId, String userName, String userPassword,
                    String userRegisterTime, String userRole,
                    String userEmail, String userMobile) {
        super(userId, userName, userPassword, userRegisterTime, userRole);
        this.userEmail = userEmail;
        this.userMobile = userMobile;
    }

    /**
     * Default constructor for Customer with empty fields.
     */
    public Customer() {
        super();
        this.userEmail = "";
        this.userMobile = "";
    }

    // Getters and setters
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserMobile() { return userMobile; }
    public void setUserMobile(String userMobile) { this.userMobile = userMobile; }

    /**
     * Converts customer data to a JSON-like string for file saving.
     */
    @Override
    public String toString() {
        return String.format("{\"user_id\":\"%s\",\"user_name\":\"%s\",\"user_password\":\"%s\"," +
                        "\"user_register_time\":\"%s\",\"user_role\":\"%s\"," +
                        "\"user_email\":\"%s\",\"user_mobile\":\"%s\"}",
                userId, userName, userPassword, userRegisterTime, userRole,
                userEmail, userMobile);
    }
}
