package com.securevault.security;

import java.util.Base64;

import javax.crypto.Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static final Logger logger =
            LoggerFactory.getLogger(AESUtil.class);

    private static final String SECRET_KEY = "1234567890123456";

    public static String encrypt(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedData = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encryptedData);

        } catch (Exception e) {
    logger.error("AES encryption failed", e);
    throw new RuntimeException("Encryption failed");
}
    }

    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedData = Base64.getDecoder().decode(encryptedData);

            return new String(cipher.doFinal(decodedData));

        } catch (Exception e) {
    logger.error("AES decryption failed", e);
    throw new RuntimeException("Decryption failed");
}
    }
}