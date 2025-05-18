package src.model;

public class Admin extends User {

    // Constructor
    public Admin(String userId, String userName, String userPassword,
                 String userRegisterTime, String userRole) {
        super(userId, userName, userPassword, userRegisterTime, userRole);
    }


    // Default constructor
    public Admin() {
        super();
        this.userRole = "admin";
    }

    @Override
    public String toString() {
        return String.format(
                "{\"user_id\":\"%s\", \"user_name\":\"%s\", \"user_password\":\"%s\", \"user_register_time\":\"%s\", \"user_role\":\"%s\"}",
                userId, userName, userPassword, userRegisterTime, userRole
        );
    }
}
