package oop_lab_final_assignment.app.model;

import org.json.JSONObject;

public abstract class User
{
    private String userId;
    private String userName;
    private String userPassword;
    private String userRegisterTime;
    private String userRole;

    public User(String userId, String userName, String userPassword, String userRegisterTime)
    {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRegisterTime = userRegisterTime;
        this.userRole = "customer";
    }

    public User(String userId, String userName, String userPassword, String userRegisterTime, String userRole)
    {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRegisterTime = userRegisterTime;
        this.userRole = userRole;
    }

    public User()
    {

    }

    protected JSONObject toJSONObject()
    {
        JSONObject json = new JSONObject();

        json.put("user_id", userId);
        json.put("user_name", userName);
        json.put("user_password", userPassword);
        json.put("user_register_name", userRegisterTime);
        json.put("user_role", userRole);

        return json;
    }

    @Override
    public String toString()
    {
        return toJSONObject().toString();
    }
}