package src.model;

public class Customer extends User {
    private String userEmail;
    private String userMobile;

    // Constructor
    public Customer(String userId, String userName, String userPassword,
                    String userRegisterTime, String userRole,
                    String userEmail, String userMobile) {
        super(userId, userName, userPassword, userRegisterTime, userRole);
        this.userEmail = userEmail;
        this.userMobile = userMobile;
    }


    // Default constructor
    public Customer() {
        super();
        this.userEmail = "default@example.com";
        this.userMobile = "0400000000";
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }


    @Override
    public String toString() {
        return String.format(
                "{\"user_id\":\"%s\", \"user_name\":\"%s\", \"user_password\":\"%s\", \"user_register_time\":\"%s\", \"user_role\":\"%s\", \"user_email\":\"%s\", \"user_mobile\":\"%s\"}",
                userId, userName, userPassword, userRegisterTime, userRole, userEmail, userMobile
        );
    }
}
