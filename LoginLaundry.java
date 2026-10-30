
import javax.swing.*;
import java.awt.*;

public class LoginLaundry extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginLaundry() {
        setTitle("Login - Laundry Kilat");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(
            20, 30, 20, 30
        ));

        JLabel judul = new JLabel(
            "LOGIN APLIKASI LAUNDRY KILAT", SwingConstants.CENTER
        );
        judul.setFont(new Font("Arial", Font.BOLD, 20));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        JPanel userPanel = new JPanel(new BorderLayout(5, 5));
        userPanel.add(new JLabel("Username"), BorderLayout.NORTH);
        userPanel.add(txtUsername, BorderLayout.CENTER);

        JPanel passPanel = new JPanel(new BorderLayout(5, 5));
        passPanel.add(new JLabel("Password"), BorderLayout.NORTH);
        passPanel.add(txtPassword, BorderLayout.CENTER);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(23, 54, 93));
        btnLogin.setForeground(Color.WHITE);

        panel.add(judul);
        panel.add(userPanel);
        panel.add(passPanel);
        panel.add(btnLogin);

        JLabel info = new JLabel(
            "Masukkan akun Admin atau Kasir",
            SwingConstants.CENTER
        );
        panel.add(info);

        add(panel);

        btnLogin.addActionListener(e -> prosesLogin());

        getRootPane().setDefaultButton(btnLogin);
    }

    private void prosesLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        String role = "";

        if (username.equals("admin")
                && password.equals("admin123")) {
            role = "Admin";
        } else if (username.equals("kasir")
                && password.equals("kasir123")) {
            role = "Kasir";
        }

        if (!role.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Login berhasil sebagai " + role
            );

            dispose();

            new MenuLaundry(role).setVisible(true);

        } else {
            JOptionPane.showMessageDialog(
                this,
                "Username atau password salah!",
                "Login Gagal",
                JOptionPane.ERROR_MESSAGE
            );

            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new LoginLaundry().setVisible(true)
        );
    }
}
