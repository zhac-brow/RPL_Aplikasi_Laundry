package laundry;

import java.awt.*;
import javax.swing.*;

/** Halaman Login. */
public class LoginForm extends JFrame {

    private final JTextField tfUser = new JTextField(18);
    private final JPasswordField tfPass = new JPasswordField(18);

    public LoginForm() {
        setTitle("Login - Aplikasi Laundry");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel judul = new JLabel("APLIKASI LAUNDRY", SwingConstants.CENTER);
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 22f));
        JLabel sub = new JLabel("Silakan login untuk melanjutkan", SwingConstants.CENTER);

        JButton btnLogin = new JButton("Login");
        JButton btnKeluar = new JButton("Keluar");

        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; p.add(judul, g);
        g.gridy = 1; p.add(sub, g);
        g.gridwidth = 1;
        g.gridy = 2; g.gridx = 0; p.add(new JLabel("Username"), g);
        g.gridx = 1; p.add(tfUser, g);
        g.gridy = 3; g.gridx = 0; p.add(new JLabel("Password"), g);
        g.gridx = 1; p.add(tfPass, g);
        JPanel b = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        b.add(btnLogin);
        b.add(btnKeluar);
        g.gridy = 4; g.gridx = 0; g.gridwidth = 2; p.add(b, g);

        setContentPane(p);
        getRootPane().setDefaultButton(btnLogin);   // tombol Enter = Login
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        btnLogin.addActionListener(e -> login());
        btnKeluar.addActionListener(e -> System.exit(0));
    }

    private void login() {
        String user = tfUser.getText().trim();
        String pass = new String(tfPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            Util.warn(this, "Username dan password wajib diisi.");
            return;
        }
        try {
            Object[] r = Db.row("SELECT id_user, username, nama, role FROM users WHERE username=? AND password=?",
                    user, Util.sha256(pass));
            if (r == null) {
                Util.warn(this, "Username atau password salah.");
                tfPass.setText("");
                return;
            }
            Session.idUser = ((Number) r[0]).intValue();
            Session.username = (String) r[1];
            Session.nama = (String) r[2];
            Session.role = String.valueOf(r[3]);
            dispose();
            new MainFrame().setVisible(true);
        } catch (Exception e) {
            Db.error(this, e);
        }
    }
}
