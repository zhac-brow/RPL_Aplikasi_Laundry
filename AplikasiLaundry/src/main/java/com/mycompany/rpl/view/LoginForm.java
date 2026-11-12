package com.mycompany.rpl.view;

import java.awt.*;
import javax.swing.*;
import com.mycompany.rpl.controller.LoginController;
import com.mycompany.rpl.util.UiUtil;

public class LoginForm extends JFrame {

    private final LoginController controller = new LoginController();

    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnLogin = new JButton("Login");

    public LoginForm() {
        setTitle("Login - Aplikasi Laundry");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.add(new JLabel("Username"));
        form.add(txtUsername);
        form.add(new JLabel("Password"));
        form.add(txtPassword);
        form.add(new JLabel(""));
        form.add(btnLogin);
        add(form);

        getRootPane().setDefaultButton(btnLogin);   // tekan Enter = Login
        btnLogin.addActionListener(e -> login());
    }

    private void login() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi.");
            return;
        }
        try {
            String role = controller.login(user, pass);
            if (role != null) {
                dispose();
                new MainMenu(user, role).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah.",
                        "Login Gagal", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            UiUtil.error(this, e);
        }
    }
}
