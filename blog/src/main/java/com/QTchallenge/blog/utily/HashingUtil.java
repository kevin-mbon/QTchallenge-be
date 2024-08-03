package com.QTchallenge.blog.utily;

import org.apache.commons.codec.digest.DigestUtils;

public class HashingUtil {

    // Hashes a password
     public static String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }
    // Verifies if the hashed password matches the raw password
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return DigestUtils.sha256Hex(rawPassword).equals(hashedPassword);
    }
}
