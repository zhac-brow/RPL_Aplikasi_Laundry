package laundry;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;

public class LaporanPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Pelanggan", "Layanan", "Jumlah", "Total", "Tanggal Masuk", "Status"};

    private final JTextField txtDari = new JTextField(LocalDate.now().withDayOfMonth(1).toString(), 10);
    private final JTextField txtSampai = new JTextField(LocalDate.now().toString(), 10);
    private final JButton btnTampil = new JButton("Tampilkan");
    private final JTable tabel = new JTable();
    private final JLabel lblJumlah = new JLabel("Jumlah transaksi: 0");
    private final JLabel lblTotal = new JLabel("Total pendapatan: Rp 0");

    public LaporanPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.add(new JLabel("Dari (yyyy-mm-dd):"));
        filter.add(txtDari);
        filter.add(new JLabel("Sampai:"));
        filter.add(txtSampai);
        filter.add(btnTampil);

        JPanel ringkas = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 5));
        ringkas.add(lblJumlah);
        ringkas.add(lblTotal);

        add(filter, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
        add(ringkas, BorderLayout.SOUTH);

        btnTampil.addActionListener(e -> tampilkan());
        tampilkan();
    }

    private void tampilkan() {
        java.sql.Date dari, sampai;
        try {
            dari = java.sql.Date.valueOf(txtDari.getText().trim());
            sampai = java.sql.Date.valueOf(txtSampai.getText().trim());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-mm-dd, contoh 2026-11-05.");
            return;
        }
        if (dari.after(sampai)) {
            JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh melebihi tanggal akhir.");
            return;
        }
        Koneksi.isiTabel(tabel, KOLOM,
                "SELECT t.id, p.nama, l.nama, t.jumlah, t.total, t.tanggal_masuk, t.status "
                + "FROM transaksi t JOIN pelanggan p ON t.id_pelanggan = p.id "
                + "JOIN layanan l ON t.id_layanan = l.id "
                + "WHERE t.tanggal_masuk BETWEEN ? AND ? ORDER BY t.tanggal_masuk, t.id",
                dari, sampai);

        double total = 0;
        for (int i = 0; i < tabel.getRowCount(); i++) {
            total += Double.parseDouble(tabel.getValueAt(i, 4).toString());
        }
        lblJumlah.setText("Jumlah transaksi: " + tabel.getRowCount());
        lblTotal.setText(String.format("Total pendapatan: Rp %,.0f", total));
    }
}
