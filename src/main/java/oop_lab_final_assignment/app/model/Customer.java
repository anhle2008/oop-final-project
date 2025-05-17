package oop_lab_final_assignment.app.model;

import org.json.JSONObject;

public class Customer extends User {
    private String userEmail;
    private String userMobile;

    public Customer(String userId, String userName, String userPassword,
                    String userRegisterTime, String userRole,
                    String userEmail, String userMobile) {
        super(userId, userName, userPassword, userRegisterTime, userRole);
        this.userEmail = userEmail;
        this.userMobile = userMobile;
    }

    public Customer() {

    }

    @Override
    protected JSONObject toJSONObject()
    {
        JSONObject json = super.toJSONObject();

        json.put("user_email", userEmail);
        json.put("user_mobile", userMobile);

        return json;
    }

    @Override
    public String toString()
    {
        return toJSONObject().toString();
    }
}
