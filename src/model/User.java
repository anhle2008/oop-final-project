package model;

import java.util.Objects;

public abstract class User {
    protected String userId;
    protected String userName;
    protected String userPassword;
    protected String userRegisterTime;
    protected String userRole;

    //constructor
    public User(String userId, String userName, String userPassword,
                String userRegisterTime, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRegisterTime = userRegisterTime;
        this.userRole = userRole;
    }

    //default constructor (default role is customer)
    public User() {
        this("", "", "", "", "customer");
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }
    public String getUserRegisterTime() { return userRegisterTime; }
    public void setUserRegisterTime(String userRegisterTime) { this.userRegisterTime = userRegisterTime; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }


    //return json string
    @Override
    public String toString() {
        return String.format("{\"user_id\":\"%s\", \"user_name\":\"%s\", \"user_password\":\"%s\", " +
                        "\"user_register_time\":\"%s\", \"user_role\":\"%s\"}",
                userId, userName, userPassword, userRegisterTime, userRole);
    }

    //checking if input match all field
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(userPassword, user.userPassword) &&
                Objects.equals(userRegisterTime, user.userRegisterTime) &&
                Objects.equals(userRole, user.userRole);
    }

    //Store objects and Find objects in txt file
    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userPassword, userRegisterTime, userRole);
    }
}
