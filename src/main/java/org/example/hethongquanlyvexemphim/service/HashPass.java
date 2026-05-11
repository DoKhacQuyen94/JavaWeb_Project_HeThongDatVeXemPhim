package org.example.hethongquanlyvexemphim.service;

import org.mindrot.jbcrypt.BCrypt;

public class HashPass {
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // 12 là độ mạnh
    }

    public static boolean check(String rawPassword, String hash) {
        return BCrypt.checkpw(rawPassword, hash);
    }

}
