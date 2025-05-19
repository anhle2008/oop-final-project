package model;

import java.util.Objects;

/**
 * Abstract base class representing a user in the system.
 * Subclasses include Customer and Admin.
 */
public abstract class User {
    protected String userId;
    protected String userName;
    protected String userPassword;
    protected String userRegisterTime;
    protected String userRole;

    /**
     * Constructor to initialize all fields.
     */
    public User(String userId, String userName, String userPassword,
                String userRegisterTime, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRegisterTime = userRegisterTime;
        this.userRole = userRole;
    }

    /**
     * Default constructor - assigns default role as "customer".
     */
    public User() {
        this("", "", "", "", "customer");
    }

    // Getters and setters
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

    /**
     * Converts user data to a JSON-like string for file saving.
     */
    @Override
    public String toString() {
        return String.format("{\"user_id\":\"%s\",\"user_name\":\"%s\",\"user_password\":\"%s\"," +
                        "\"user_register_time\":\"%s\",\"user_role\":\"%s\"}",
                userId, userName, userPassword, userRegisterTime, userRole);
    }

    /**
     * Checks equality based on all user fields.
     */
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

    /**
     * Used for storing/finding users in hash-based collections.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userPassword, userRegisterTime, userRole);
    }

    /**
     * Returns true if user is an admin.
     */
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(userRole);
    }

    /**
     * Returns true if user is a customer.
     */
    public boolean isCustomer() {
        return "customer".equalsIgnoreCase(userRole);
    }
}
