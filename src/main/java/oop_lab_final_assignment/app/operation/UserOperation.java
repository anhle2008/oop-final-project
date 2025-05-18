package oop_lab_final_assignment.app.operation;

import java.util.Random;
import oop_lab_final_assignment.app.model.User;

public class UserOperation {
    private static UserOperation instance;

    private UserOperation() {}

    /**
     * Returns the single instance of UserOperation.
     * @return UserOperation instance
     */
    public static UserOperation getInstance()
    {
        if (instance == null)
        {
            instance = new UserOperation();
        }

        return instance;
    }

    /**
     * Generates and returns a 10-digit unique user id starting with 'u_'
     * every time when a new user is registered.
     * @return A string value in the format 'u_10digits', e.g., 'u_1234567890'
     */
    public String generateUniqueUserId()
    {
        String userId = "u_";
        int nDigitInId = 10;
        int min = 1;
        int max = 9;

        Random random = new Random();

        for (int i = 0; i < nDigitInId; i++)
        {
            // Generate a random number in range [min, max].
            int randNum = random.nextInt(max - min + 1) + min;

            userId += String.valueOf(randNum);
        }

        return userId;
    }

    /**
     * Encode a user-provided password.
     * Encryption steps:
     * 1. Generate a random string with a length equal to two times
     *    the length of the user-provided password. The random string
     *    should consist of characters chosen from a-zA-Z0-9.
     * 2. Combine the random string and the input password text to
     *    create an encrypted password, following the rule of selecting
     *    two letters sequentially from the random string and
     *    appending one letter from the input password. Repeat until all
     *    characters in the password are encrypted. Finally, add "^^" at
     *    the beginning and "$$" at the end of the encrypted password.
     *
     * @param userPassword The password to encrypt
     * @return Encrypted password
     */
    public String encryptPassword(String userPassword)
    {
        String charRange = "abcdefghijklmnopqrstuvxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        int randStringLength = userPassword.length() * 2;

        // Initial marker for encrypted password
        String encrypted = "^^";

        String randString = "";

        Random random = new Random();

        // Generate a random string
        for (int i = 0; i < randStringLength; i++)
        {
            int index = random.nextInt(charRange.length());

            randString += charRange.charAt(index);
        }

        // Encrypt password by appending two char from randString sequentially and one char from userPassword
        for (int i = 0; i < userPassword.length(); i++)
        {
            encrypted += randString.charAt(i * 2);
            encrypted += randString.charAt(i * 2 + 1);
            encrypted += userPassword.charAt(i);
        }

        // Ending marker for encrypted password
        encrypted += "$$";

        return encrypted;
    }

    /**
     * Decode the encrypted password with a similar rule as the encryption method.
     * @param encryptedPassword The encrypted password to decrypt
     * @return Original user-provided password
     */
    public String decryptPassword(String encryptedPassword)
    {
        String decrypted = "";

        // Index without initial marker ^^
        int initialIndex = 2;

        // Index without ending marker $$
        int endingIndex = encryptedPassword.length() - 2;

        // Index of first character of decrypted password from encrypted one
        int firstDecryptedIndex = initialIndex + 2;

        /**
         * Decrypt password by getting every third character of the encrypted password without marker.
         */
        for (int i = firstDecryptedIndex; i < endingIndex; i += 3) {
            decrypted += encryptedPassword.charAt(i);
        }

        return decrypted;
    }


    /**
     * Verify whether a user is already registered or exists in the system.
     * @param userName The username to check
     * @return true if exists, false otherwise
     */
    public boolean checkUsernameExist(String userName)
    {
        // Implementation
    }

    /**
     * Validate the user's name. The name should only contain letters or
     * underscores, and its length should be at least 5 characters.
     * @param userName The username to validate
     * @return true if valid, false otherwise
     */
    public boolean validateUsername(String userName)
    {
        int minLength = 5;
        boolean isValid = true;

        if (userName.length() < minLength)
        {
            isValid = false;
        }
        /**
         * Check if string contain only letters or underscore.
         * Based on https://stackoverflow.com/questions/5238491/check-if-string-contains-only-letters
         */
        else if (!userName.matches("[a-zA-Z_]+"))
        {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Validate the user's password. The password should contain at least
     * one letter (uppercase or lowercase) and one number. The length
     * must be greater than or equal to 5 characters.
     * @param userPassword The password to validate
     * @return true if valid, false otherwise
     */
    public boolean validatePassword(String userPassword) {
        int minLength = 5;
        boolean isValid = true;

        if (userPassword.length() < minLength)
        {
            isValid = false;
        }
        else if (!userPassword.matches("[a-zA-Z0-9]+"))
        {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Verify the provided user's name and password combination against
     * stored user data to determine the authorization status.
     * @param userName The username for login
     * @param userPassword The password for login
     * @return A User object (Customer or Admin) if successful, null otherwise
     */
    public User login(String userName, String userPassword) {
        
    }
}
