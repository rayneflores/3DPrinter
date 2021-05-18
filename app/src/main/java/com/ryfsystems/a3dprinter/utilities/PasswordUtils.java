package com.ryfsystems.a3dprinter.utilities;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordUtils {

    /*private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;*/
    public static final String ENCRYPT_ALG = "AES";
    public static final String CHARSET = "UTF-8";
    public static final String SALT = "ChUrRuMiNo";

    /*public static String getSalt(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static String generateSecurePassword(String password, String salt) {
        String returnValue = null;
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            returnValue = Base64.getEncoder().encodeToString(securePassword);
        }

        return returnValue;
    }*/

    public static String encrypt(String password, String salt) throws Exception {
        SecretKeySpec secretKey = generateKey(salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALG);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] byteEncryptedPassword = cipher.doFinal(password.getBytes());
        String encryptedPassword = Base64.encodeToString(byteEncryptedPassword, Base64.DEFAULT);
        return encryptedPassword;
    }

    public static String decrypt(String password, String salt) throws Exception {
        SecretKeySpec secretKey = generateKey(salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALG);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedPassword = Base64.decode(password, Base64.DEFAULT);
        byte[] byteDecryptedPassword = cipher.doFinal(decodedPassword);
        String decryptedPassword = new String(byteDecryptedPassword);
        return decryptedPassword;
    }

    public static SecretKeySpec generateKey(String salt) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = salt.getBytes(CHARSET);
        key = sha.digest(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, ENCRYPT_ALG);
        return secretKey;
    }
}
