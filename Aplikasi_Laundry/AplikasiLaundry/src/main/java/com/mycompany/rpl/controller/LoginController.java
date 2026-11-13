package com.mycompany.rpl.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.mycompany.rpl.model.User;
import com.mycompany.rpl.util.HibernateUtil;
import org.hibernate.Session;

public class LoginController {

    // Mengembalikan role ("admin"/"kasir") jika berhasil, null jika gagal
    public String login(String username, String password) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            User u = s.createQuery("from User where username = :u", User.class)
                    .setParameter("u", username)
                    .uniqueResult();
            if (u != null && u.getPassword().equals(md5(password))) {
                return u.getRole();
            }
            return null;
        }
    }

    private String md5(String teks) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(teks.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
