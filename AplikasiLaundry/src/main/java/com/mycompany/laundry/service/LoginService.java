package com.mycompany.laundry.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.mycompany.laundry.dao.UserDAO;
import com.mycompany.laundry.model.User;
import org.springframework.transaction.annotation.Transactional;

public class LoginService {

    private UserDAO userDAO;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Mengembalikan role ("admin"/"kasir") jika berhasil, null jika gagal
    @Transactional(readOnly = true)
    public String login(String username, String password) {
        User u = userDAO.cariByUsername(username);
        if (u != null && u.getPassword().equals(md5(password))) {
            return u.getRole();
        }
        return null;
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
