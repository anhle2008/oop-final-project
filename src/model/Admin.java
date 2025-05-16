package src.model;

public class Admin extends User {
    public Admin(String userId, String userName, String userPassword, String userRegisterTime, String userRole) {
        super(userId, userName, userPassword, userRegisterTime, userRole);
    }

    public Admin() {

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}
