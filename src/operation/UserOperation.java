package operation;

import model.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * Singleton class to manage User data including loading, saving,
 * validation, login, and user CRUD operations.
 */
public class UserOperation {
    // Singleton instance
    private static UserOperation instance;

    // In-memory list of users
    private final List<User> users;

    // File path to persist user data
    private static final String USERS_FILE = "data/users.txt";

    /**
     * Private constructor initializes users list and loads users from file.
     */
    private UserOperation() {
        users = new ArrayList<>();
        loadUsersFromFile();
    }

    /**
     * Provides singleton instance of UserOperation.
     */
    public static UserOperation getInstance() {
        if (instance == null) {
            instance = new UserOperation();
        }
        return instance;
    }

    /**
     * Load users from file into the users list.
     * Creates directories if needed.
     */
    private void loadUsersFromFile() {
        File file = new File(USERS_FILE);
        File parentDir = file.getParentFile();

        // Create directory if missing
        if (parentDir != null && !parentDir.exists()) {
            boolean dirsCreated = parentDir.mkdirs();
            if (!dirsCreated) {
                System.err.println("Failed to create directories for: " + USERS_FILE);
                return;
            }
        }

        // If file doesn't exist, no users to load
        if (!file.exists()) {
            return;
        }

        // Read users line-by-line and parse
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

    /**
     * Parse a user JSON-like string and instantiate corresponding User object.
     * Returns null if the line does not represent a user.
     */
    private User parseUser(String line) {
        // Skip lines containing order or product data
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
                // Create Admin user
                return new Admin(userId, userName, password, registerTime, role);
            } else {
                // Create Customer user with email and mobile
                String email = userMap.get("user_email");
                String mobile = userMap.get("user_mobile");
                return new Customer(userId, userName, password, registerTime, role, email, mobile);
            }
        } catch (Exception e) {
            System.err.println("Error parsing user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert a JSON-like user string into a Map of key-value pairs.
     */
    private Map<String, String> createUserMap(String userString) {
        Map<String, String> userMap = new HashMap<>();
        try {
            // Remove the enclosing braces
            String content = userString.substring(1, userString.length() - 1);
            // Split by "," possibly with spaces
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

    /**
     * Save all users in memory to the file in JSON-like format.
     */
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

    /**
     * Generate a unique user ID based on current time and a random number.
     */
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

    /**
     * Check if a username already exists.
     */
    public boolean checkUsernameExist(String userName) {
        if (userName == null) {
            return false;
        }
        return users.stream().anyMatch(u -> userName.equals(u.getUserName()));
    }

    /**
     * Validate username format (at least 5 chars, letters or underscores).
     */
    public boolean validateUsername(String userName) {
        return userName != null && Pattern.matches("^[a-zA-Z_]{5,}$", userName);
    }

    /**
     * Validate password format (at least 5 chars, must contain letters and digits).
     */
    public boolean validatePassword(String userPassword) {
        return userPassword != null && userPassword.matches("^(?=.*[A-Za-z])(?=.*\\d).{5,}$");
    }

    /**
     * Attempt to login user by matching username and password.
     * Returns user if found, null otherwise.
     */
    public User login(String userName, String userPassword) {
        if (userName == null || userPassword == null) {
            return null;
        }
        return users.stream()
                .filter(u -> userName.equals(u.getUserName()) && userPassword.equals(u.getUserPassword()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add a new user to the in-memory list and persist to file.
     */
    public void addUser(User user) {
        if (user != null) {
            users.add(user);
            saveUsersToFile();
        }
    }

    /**
     * Return a copy of all users.
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
