package src.model;

public abstract class User {
    private String userId;
    private String userName;
    private String userPassword;
    private String userRegisterTime;
    private String userRole;

    public User(String userId, String userName, String userPassword, String userRegisterTime, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRegisterTime = userRegisterTime;
        this.userRole = userRole;
    }

    public User() {

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}