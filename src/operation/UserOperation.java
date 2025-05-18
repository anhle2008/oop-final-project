package src.operation;

import src.model.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class UserOperation {
    private static UserOperation instance;
    private final String userFile = "data/users.txt";
    private final Random random = new Random();

    private UserOperation() {}

    public static UserOperation getInstance() {
        if (instance == null) {
            instance = new UserOperation();
        }
        return instance;
    }

    public String generateUniqueUserId() {
        String id;
        do {
            id = "u_" + String.format("%010d", random.nextInt(1_000_000_000));
        } while (userIdExists(id));
        return id;
    }

    private boolean userIdExists(String userId) {
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"user_id\":\"" + userId + "\"")) return true;
            }
        } catch (IOException ignored) {}
        return false;
    }

    public String encryptPassword(String userPassword) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < userPassword.length() * 2; i++) {
            randomStr.append(chars.charAt(random.nextInt(chars.length())));
        }
        StringBuilder encrypted = new StringBuilder("^^");
        for (int i = 0; i < userPassword.length(); i++) {
            encrypted.append(randomStr.charAt(i * 2))
                    .append(randomStr.charAt(i * 2 + 1))
                    .append(userPassword.charAt(i));
        }
        encrypted.append("$$");
        return encrypted.toString();
    }

    public String decryptPassword(String encryptedPassword) {
        if (encryptedPassword == null || !encryptedPassword.startsWith("^^") || !encryptedPassword.endsWith("$$")) return null;
        String core = encryptedPassword.substring(2, encryptedPassword.length() - 2);
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < core.length(); i += 3) {
            password.append(core.charAt(i + 2));
        }
        return password.toString();
    }

    public boolean checkUsernameExist(String userName) {
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"user_name\":\"" + userName + "\"")) return true;
            }
        } catch (IOException ignored) {}
        return false;
    }

    public boolean validateUsername(String userName) {
        if (userName == null) return false;
        return userName.matches("[a-zA-Z_]{5,}");
    }

    public boolean validatePassword(String userPassword) {
        if (userPassword == null || userPassword.length() < 5) return false;
        boolean hasLetter = userPassword.matches(".*[a-zA-Z].*");
        boolean hasDigit = userPassword.matches(".*[0-9].*");
        return hasLetter && hasDigit;
    }

    public User login(String userName, String userPassword) {
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            String encryptedInputPassword = encryptPassword(userPassword);
            while ((line = br.readLine()) != null) {
                if (line.contains("\"user_name\":\"" + userName + "\"")) {
                    String storedPassword = extractField(line, "user_password");
                    if (storedPassword != null && storedPassword.equals(encryptPassword(userPassword))) {
                        String userId = extractField(line, "user_id");
                        String registerTime = extractField(line, "user_register_time");
                        String role = extractField(line, "user_role");
                        if ("customer".equals(role)) {
                            String email = extractField(line, "user_email");
                            String mobile = extractField(line, "user_mobile");
                            return new Customer(userId, userName, storedPassword, registerTime, role, email, mobile);
                        } else if ("admin".equals(role)) {
                            return new Admin(userId, userName, storedPassword, registerTime, role);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
