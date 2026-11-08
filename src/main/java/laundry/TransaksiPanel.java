package laundry;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class TransaksiPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Pelanggan", "Layanan", "Jumlah", "Total", "Bayar", "Tanggal Masuk", "Status"};

    private final JComboBox<Item> cmbPelanggan = new JComboBox<>();
    private final JComboBox<Item> cmbLayanan = new JComboBox<>();
    private final JLabel lblHarga = new JLabel("Rp 0");
    private final JTextField txtJumlah = new JTextField();
    private final JTextField txtTanggal = new JTextField(new java.sql.Date(System.currentTimeMillis()).toString());
    private final JLabel lblTotal = new JLabel("Rp 0");
    private final JTextField txtBayar = new JTextField();
    private final JLabel lblKembali = new JLabel("Rp 0");
    private final JButton btnSimpan = new JButton("Simpan Transaksi");
    private final JButton btnBaru = new JButton("Bersihkan");
    private final JTable tabel = new JTable();

    public TransaksiPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(8, 2, 8, 6));
        form.add(new JLabel("Pelanggan"));
        form.add(cmbPelanggan);
        form.add(new JLabel("Layanan"));
        form.add(cmbLayanan);
        form.add(new JLabel("Harga Satuan"));
        form.add(lblHarga);
        form.add(new JLabel("Berat (kg) / Jumlah (pcs)"));
        form.add(txtJumlah);
        form.add(new JLabel("Tanggal Masuk (yyyy-mm-dd)"));
        form.add(txtTanggal);
        form.add(new JLabel("Total Harga"));
        form.add(lblTotal);
        form.add(new JLabel("Nominal Bayar"));
        form.add(txtBayar);
        form.add(new JLabel("Kembalian"));
        form.add(lblKembali);

        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tombol.add(btnSimpan);
        tombol.add(btnBaru);

        JPanel atas = new JPanel(new BorderLayout(8, 8));
        atas.add(form, BorderLayout.NORTH);
        atas.add(tombol, BorderLayout.SOUTH);
        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        cmbLayanan.addActionListener(e -> hitung());
        KeyAdapter ka = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitung();
            }
        };
        txtJumlah.addKeyListener(ka);
        txtBayar.addKeyListener(ka);
        btnSimpan.addActionListener(e -> simpan());
        btnBaru.addActionListener(e -> bersihkan());

        refresh();
    }

    public void refresh() {
        isiCombo(cmbPelanggan, "SELECT id, nama, 0 FROM pelanggan ORDER BY nama");
        isiCombo(cmbLayanan, "SELECT id, CONCAT(nama, ' (per ', satuan, ')'), harga FROM layanan ORDER BY id");
        Koneksi.isiTabel(tabel, KOLOM,
                "SELECT t.id, p.nama, l.nama, t.jumlah, t.total, t.bayar, t.tanggal_masuk, t.status "
                + "FROM transaksi t JOIN pelanggan p ON t.id_pelanggan = p.id "
                + "JOIN layanan l ON t.id_layanan = l.id ORDER BY t.id DESC");
        hitung();
    }

    private void isiCombo(JComboBox<Item> cmb, String sql) {
        cmb.removeAllItems();
        try (Statement st = Koneksi.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cmb.addItem(new Item(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
            }
        } catch (SQLException e) {
            Koneksi.error(e);
        }
    }

    private String rupiah(double d) {
        return String.format("Rp %,.0f", d);
    }

    private double ambilJumlah() {
        try {
            return Double.parseDouble(txtJumlah.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void hitung() {
        Item l = (Item) cmbLayanan.getSelectedItem();
        double harga = (l == null) ? 0 : l.harga;
        double total = ambilJumlah() * harga;
        lblHarga.setText(rupiah(harga));
        lblTotal.setText(rupiah(total));
        try {
            double bayar = Double.parseDouble(txtBayar.getText().trim());
            lblKembali.setText(rupiah(Math.max(0, bayar - total)));
        } catch (NumberFormatException e) {
            lblKembali.setText("Rp 0");
        }
    }

    private void simpan() {
        Item p = (Item) cmbPelanggan.getSelectedItem();
        Item l = (Item) cmbLayanan.getSelectedItem();
        if (p == null || l == null) {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan dan layanan. Tambahkan data pelanggan terlebih dahulu jika masih kosong.");
            return;
        }
        double jumlah = ambilJumlah();
        if (jumlah <= 0) {
            JOptionPane.showMessageDialog(this, "Berat/jumlah harus berupa angka lebih dari 0.");
            return;
        }
        java.sql.Date tgl;
        try {
            tgl = java.sql.Date.valueOf(txtTanggal.getText().trim());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-mm-dd, contoh 2026-11-05.");
            return;
        }
        double total = jumlah * l.harga;
        double bayar;
        try {
            bayar = Double.parseDouble(txtBayar.getText().trim());
        } catch (NumberFormatException e) {
            bayar = -1;
        }
        if (bayar < total) {
            JOptionPane.showMessageDialog(this, "Nominal bayar harus diisi dan tidak boleh kurang dari total.");
            return;
        }
        try {
            Koneksi.exec("INSERT INTO transaksi(id_pelanggan, id_layanan, jumlah, total, bayar, tanggal_masuk, status) "
                    + "VALUES(?,?,?,?,?,?,'Diterima')",
                    p.id, l.id, jumlah, total, bayar, tgl);
            JOptionPane.showMessageDialog(this, "Transaksi tersimpan.\nTotal: " + rupiah(total)
                    + "\nKembalian: " + rupiah(bayar - total));
            bersihkan();
            refresh();
        } catch (SQLException e) {
            Koneksi.error(e);
        }
    }

    private void bersihkan() {
        txtJumlah.setText("");
        txtBayar.setText("");
        txtTanggal.setText(new java.sql.Date(System.currentTimeMillis()).toString());
        hitung();
    }
}
