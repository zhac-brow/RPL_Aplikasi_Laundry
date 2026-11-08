package laundry;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginForm extends JFrame {

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

        getRootPane().setDefaultButton(btnLogin);
        btnLogin.addActionListener(e -> login());
    }

    private void login() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi.");
            return;
        }
        String sql = "SELECT role FROM users WHERE username=? AND password=MD5(?)";
        try (PreparedStatement ps = Koneksi.getConnection().prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    dispose();
                    new MainMenu(user, role).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Username atau password salah.",
                            "Login Gagal", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            Koneksi.error(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
