package operation;

import model.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class UserOperation {
    private static UserOperation instance;
    private final List<User> users;
    private static final String USERS_FILE = "data/users.txt";

    private UserOperation() {
        users = new ArrayList<>();
        loadUsersFromFile();
    }

    public static UserOperation getInstance() {
        if (instance == null) {
            instance = new UserOperation();
        }
        return instance;
    }

    private void loadUsersFromFile() {
        File file = new File(USERS_FILE);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            boolean dirsCreated = parentDir.mkdirs();
            if (!dirsCreated) {
                System.err.println("Failed to create directories for: " + USERS_FILE);
                return;
            }
        }

        if (!file.exists()) {
            return; // No users file yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("{") && line.endsWith("}")) {
                    User user = parseUser(line);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private User parseUser(String line) {
        // Skip non-user records
        if (line.contains("order_id") || line.contains("pro_id")) {
            System.err.println("Skipping non-user record in users file: " + line);
            return null;
        }

        Map<String, String> userMap = createUserMap(line);
        if (userMap.isEmpty()) {
            return null;
        }

        try {
            String userId = userMap.get("user_id");
            String userName = userMap.get("user_name");
            String password = userMap.get("user_password");
            String registerTime = userMap.get("user_register_time");
            String role = userMap.get("user_role");

            if ("admin".equalsIgnoreCase(role)) {
                return new Admin(userId, userName, password, registerTime, role);
            } else {
                String email = userMap.get("user_email");
                String mobile = userMap.get("user_mobile");
                return new Customer(userId, userName, password, registerTime, role, email, mobile);
            }
        } catch (Exception e) {
            System.err.println("Error parsing user: " + e.getMessage());
            return null;
        }
    }

    private Map<String, String> createUserMap(String userString) {
        Map<String, String> userMap = new HashMap<>();
        try {
            String content = userString.substring(1, userString.length() - 1);
            String[] pairs = content.split("\",\\s*\"");

            for (String pair : pairs) {
                String[] keyValue = pair.split("\":\"?", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].replace("\"", "").trim();
                    String value = keyValue[1].replace("\"", "").trim();
                    userMap.put(key, value);
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating user map: " + e.getMessage());
        }
        return userMap;
    }

    private void saveUsersToFile() {
        File file = new File(USERS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public String generateUniqueUserId() {
        return "u_" + System.currentTimeMillis() + "_" + ThreadLocalRandom.current().nextInt(1000);
    }

    public String encryptPassword(String userPassword) {
        String allChars = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";

        // Encrypted password with initial marker
        String encrypted = "^^";

        String randString = "";

        int encryptedLength = userPassword.length() * 2;

        Random random = new Random();

        // Generate a random string
        for (int i = 0; i < encryptedLength; i++) {
            int index = random.nextInt(allChars.length());

            randString += allChars.charAt(index);
        }

        // Encrypt password
        for (int i = 0; i < userPassword.length(); i++) {
            encrypted += randString.charAt(i * 2);
            encrypted += randString.charAt(i * 2 + 1);
            encrypted += userPassword.charAt(i);
        }

        // Ending marker for encrypted password
        encrypted += "$$";

        return encrypted;
    }

    public String decryptPassword(String encryptedPassword) {
        String encryptedNoMarker = encryptedPassword.substring(2, encryptedPassword.length() - 2);
        String decrypted = "";

        // Decrypt the password
        for (int i = 2; i < encryptedNoMarker.length(); i += 3) {
            decrypted += encryptedNoMarker.charAt(i);
        }

        return decrypted;
    }

    public boolean checkUsernameExist(String userName) {
        if (userName == null) {
            return false;
        }
        return users.stream().anyMatch(u -> userName.equals(u.getUserName()));
    }

    public boolean validateUsername(String userName) {
        return userName != null && Pattern.matches("^[a-zA-Z_]{5,}$", userName);
    }

    public boolean validatePassword(String userPassword) {
        return userPassword != null && userPassword.matches("^(?=.*[A-Za-z])(?=.*\\d).{5,}$");
    }

    public User login(String userName, String userPassword) {
        if (userName == null || userPassword == null) {
            return null;
        }
        return users.stream()
                .filter(u -> userName.equals(u.getUserName()) && userPassword.equals(u.getUserPassword()))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        if (user != null) {
            users.add(user);
            saveUsersToFile();
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
