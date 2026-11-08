package laundry;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Koneksi {

    private static final String URL  = "jdbc:mysql://localhost:3306/db_applaundry";
    private static final String USER = "root";
    private static final String PASS = "";   // ubah jika MySQL kamu memakai password

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL belum ditambahkan ke Libraries.");
        }
        return conn;
    }

    // INSERT / UPDATE / DELETE
    public static int exec(String sql, Object... param) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            for (int i = 0; i < param.length; i++) {
                ps.setObject(i + 1, param[i]);
            }
            return ps.executeUpdate();
        }
    }

    // SELECT -> langsung ditampilkan ke JTable
    public static void isiTabel(JTable tabel, String[] kolom, String sql, Object... param) {
        DefaultTableModel model = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            for (int i = 0; i < param.length; i++) {
                ps.setObject(i + 1, param[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] baris = new Object[kolom.length];
                    for (int i = 0; i < kolom.length; i++) {
                        baris[i] = rs.getObject(i + 1);
                    }
                    model.addRow(baris);
                }
            }
        } catch (SQLException e) {
            error(e);
        }
        tabel.setModel(model);
    }

    public static void error(Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
