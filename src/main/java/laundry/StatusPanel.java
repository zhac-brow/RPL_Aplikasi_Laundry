package laundry;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

public class StatusPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Pelanggan", "Layanan", "Jumlah", "Tanggal Masuk", "Status"};
    private static final String[] STATUS = {"Diterima", "Dicuci", "Disetrika", "Selesai", "Sudah Diambil"};

    private final JTable tabel = new JTable();
    private final JComboBox<String> cmbStatus = new JComboBox<>(STATUS);
    private final JButton btnSimpan = new JButton("Simpan Status");

    public StatusPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bawah.add(new JLabel("Status baru:"));
        bawah.add(cmbStatus);
        bawah.add(btnSimpan);

        add(new JScrollPane(tabel), BorderLayout.CENTER);
        add(bawah, BorderLayout.SOUTH);

        btnSimpan.addActionListener(e -> simpan());
        refresh();
    }

    public void refresh() {
        Koneksi.isiTabel(tabel, KOLOM,
                "SELECT t.id, p.nama, l.nama, t.jumlah, t.tanggal_masuk, t.status "
                + "FROM transaksi t JOIN pelanggan p ON t.id_pelanggan = p.id "
                + "JOIN layanan l ON t.id_layanan = l.id ORDER BY t.id DESC");
    }

    private void simpan() {
        int r = tabel.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Pilih pesanan pada tabel terlebih dahulu.");
            return;
        }
        int id = Integer.parseInt(tabel.getValueAt(r, 0).toString());
        try {
            Koneksi.exec("UPDATE transaksi SET status=? WHERE id=?", cmbStatus.getSelectedItem().toString(), id);
            refresh();
        } catch (SQLException e) {
            Koneksi.error(e);
        }
    }
}
