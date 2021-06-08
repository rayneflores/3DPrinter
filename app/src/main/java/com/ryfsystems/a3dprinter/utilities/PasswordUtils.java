package com.ryfsystems.a3dprinter.utilities;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordUtils {

    public static final String ENCRYPT_ALG = "AES";
    public static final String CHARSET = "UTF-8";
    public static final String SALT = "ChUrRuMiNo";

    public static String encrypt(String password, String salt) throws Exception {
        SecretKeySpec secretKey = generateKey(salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALG);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] byteEncryptedPassword = cipher.doFinal(password.getBytes());
        return Base64.encodeToString(byteEncryptedPassword, Base64.DEFAULT);
    }

    public static String decrypt(String password, String salt) throws Exception {
        SecretKeySpec secretKey = generateKey(salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALG);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedPassword = Base64.decode(password, Base64.DEFAULT);
        byte[] byteDecryptedPassword = cipher.doFinal(decodedPassword);
        return new String(byteDecryptedPassword);
    }

    public static SecretKeySpec generateKey(String salt) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = salt.getBytes(CHARSET);
        key = sha.digest(key);
        return new SecretKeySpec(key, ENCRYPT_ALG);
    }
}
