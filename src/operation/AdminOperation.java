package src.operation;

import src.model.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

public class AdminOperation {
    private static AdminOperation instance;
    private final String userFile = "data/users.txt";

    private AdminOperation() {}

    /**
     * Returns the single instance of AdminOperation.
     * @return AdminOperation instance
     */
    public static AdminOperation getInstance() {
        if (instance == null) {
            instance = new AdminOperation();
        }
        return instance;
    }

    /**
     * Creates an admin account. This function should be called when
     * the system starts. The same admin account should not be
     * registered multiple times.
     */
    public void registerAdmin() {
        List<Admin> admins = getAllAdmins();
        if (!admins.isEmpty()) {
            // Admin(s) already exist, do nothing
            return;
        }

        String userId = "admin001";  // Could implement unique ID generator
        String userName = "admin";
        String userPassword = "admin123"; // Ideally encrypted in real case
        String registerTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
        String userRole = "admin";

        Admin admin = new Admin(userId, userName, userPassword, registerTime, userRole);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile, true))) {
            bw.write(admin.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"user_role\":\"admin\"")) {
                    admins.add(parseAdmin(line));
                }
            }
        } catch (IOException ignored) {}
        return admins;
    }

    private Admin parseAdmin(String line) {
        String userId = extractField(line, "user_id");
        String userName = extractField(line, "user_name");
        String userPassword = extractField(line, "user_password");
        String userRegisterTime = extractField(line, "user_register_time");
        String userRole = extractField(line, "user_role");
        return new Admin(userId, userName, userPassword, userRegisterTime, userRole);
    }

    private String extractField(String jsonLine, String field) {
        Pattern pattern = Pattern.compile("\"" + field + "\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonLine);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
