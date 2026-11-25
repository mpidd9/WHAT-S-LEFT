package whats_left.repository;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Hash password dengan PBKDF2 (bawaan Java, tanpa library tambahan).
 * Format tersimpan: iterasi:salt:hash (Base64).
 */
public final class PasswordUtil {
    private static final int ITERATIONS = 120_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    private PasswordUtil() {
    }

    public static String hash(char[] password) {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] key = pbkdf2(password, salt, ITERATIONS);

        return ITERATIONS + ":"
                + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(key);
    }

    public static boolean verify(char[] password, String stored) {
        if (stored == null) {
            return false;
        }

        String[] parts = stored.split(":");
        if (parts.length != 3) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);
            byte[] actual = pbkdf2(password, salt, iterations);

            return MessageDigest.isEqual(expected, actual);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_BITS);

        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Gagal melakukan hash password", e);
        } finally {
            spec.clearPassword();
        }
    }
}
