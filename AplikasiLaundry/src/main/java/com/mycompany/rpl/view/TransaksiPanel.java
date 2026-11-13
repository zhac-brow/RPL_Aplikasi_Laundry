package com.mycompany.rpl.view;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import com.mycompany.rpl.controller.TransaksiController;
import com.mycompany.rpl.model.Layanan;
import com.mycompany.rpl.model.Pelanggan;
import com.mycompany.rpl.model.Transaksi;
import javax.swing.table.DefaultTableModel;

public class TransaksiPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Pelanggan", "Layanan", "Jumlah", "Total", "Bayar", "Tanggal Masuk", "Status"};

    private final TransaksiController controller = new TransaksiController();

    private final JComboBox<Pelanggan> cmbPelanggan = new JComboBox<>();
    private final JComboBox<Layanan> cmbLayanan = new JComboBox<>();
    private final JLabel lblHarga = new JLabel("Rp 0");
    private final JTextField txtJumlah = new JTextField();
    private final JTextField txtTanggal = new JTextField(LocalDate.now().toString());
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
        try {
            cmbPelanggan.removeAllItems();
            for (Pelanggan p : controller.daftarPelanggan()) {
                cmbPelanggan.addItem(p);
            }
            cmbLayanan.removeAllItems();
            for (Layanan l : controller.daftarLayanan()) {
                cmbLayanan.addItem(l);
            }
            List<Object[]> baris = new ArrayList<>();
            for (Transaksi t : controller.semua()) {
                baris.add(new Object[]{t.getId(), t.getPelanggan().getNama(), t.getLayanan().getNama(),
                    t.getJumlah(), t.getTotal(), t.getBayar(), t.getTanggalMasuk(), t.getStatus()});
            }
            DefaultTableModel modelTabel = new DefaultTableModel(KOLOM, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            for (Object[] b : baris) {
                modelTabel.addRow(b);
            }
            tabel.setModel(modelTabel);
        } catch (Exception e) {
            tampilkanError(e);
        }
        hitung();
    }

    private double ambilJumlah() {
        try {
            return Double.parseDouble(txtJumlah.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void hitung() {
        Layanan l = (Layanan) cmbLayanan.getSelectedItem();
        double harga = (l == null) ? 0 : l.getHarga();
        double total = ambilJumlah() * harga;
        lblHarga.setText(String.format("Rp %,.0f", harga));
        lblTotal.setText(String.format("Rp %,.0f", total));
        try {
            double bayar = Double.parseDouble(txtBayar.getText().trim());
            lblKembali.setText(String.format("Rp %,.0f", Math.max(0, bayar - total)));
        } catch (NumberFormatException e) {
            lblKembali.setText("Rp 0");
        }
    }

    private void simpan() {
        Pelanggan p = (Pelanggan) cmbPelanggan.getSelectedItem();
        Layanan l = (Layanan) cmbLayanan.getSelectedItem();
        if (p == null || l == null) {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan dan layanan. Tambahkan data pelanggan terlebih dahulu jika masih kosong.");
            return;
        }
        double jumlah = ambilJumlah();
        if (jumlah <= 0) {
            JOptionPane.showMessageDialog(this, "Berat/jumlah harus berupa angka lebih dari 0.");
            return;
        }
        LocalDate tgl;
        try {
            tgl = LocalDate.parse(txtTanggal.getText().trim());
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-mm-dd, contoh 2026-11-05.");
            return;
        }
        double total = jumlah * l.getHarga();
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
            controller.simpan(p, l, jumlah, bayar, tgl);
            JOptionPane.showMessageDialog(this, "Transaksi tersimpan.\nTotal: " + String.format("Rp %,.0f", total)
                    + "\nKembalian: " + String.format("Rp %,.0f", bayar - total));
            bersihkan();
            refresh();
        } catch (Exception e) {
            tampilkanError(e);
        }
    }

    private void bersihkan() {
        txtJumlah.setText("");
        txtBayar.setText("");
        txtTanggal.setText(LocalDate.now().toString());
        hitung();
    }

    private void tampilkanError(Exception e) {
        Throwable t = e;
        while (t.getCause() != null) {
            t = t.getCause();
        }
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + t.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
