import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurityUtils {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Hashes a password using SHA-256 combined with a unique salt.
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedBytes = md.digest(password.getBytes());
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found!", e);
        }
    }

    /**
     * Generates a secure random salt string (32 characters hex).
     */
    public static String generateSalt() {
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

    /**
     * Validates if a password meets strength requirements (min 6 chars, contains uppercase and a digit).
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) return false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasUppercase && hasDigit;
    }

    /**
     * Validates username (alphanumeric, 3-20 characters).
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.length() < 3 || username.length() > 20) return false;
        return username.matches("^[a-zA-Z0-9_]+$");
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
