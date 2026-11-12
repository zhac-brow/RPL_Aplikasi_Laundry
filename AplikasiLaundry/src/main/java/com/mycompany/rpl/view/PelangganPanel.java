package com.mycompany.rpl.view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import com.mycompany.rpl.controller.PelangganController;
import com.mycompany.rpl.model.Pelanggan;
import com.mycompany.rpl.util.UiUtil;

public class PelangganPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Nama", "Alamat", "Telepon"};

    private final PelangganController controller = new PelangganController();

    private final JTextField txtNama = new JTextField();
    private final JTextField txtAlamat = new JTextField();
    private final JTextField txtTelepon = new JTextField();
    private final JTextField txtCari = new JTextField(15);
    private final JButton btnTambah = new JButton("Tambah");
    private final JButton btnUbah = new JButton("Ubah");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnBersih = new JButton("Bersihkan");
    private final JTable tabel = new JTable();
    private int idDipilih = -1;

    public PelangganPanel(String role) {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Nama"));
        form.add(txtNama);
        form.add(new JLabel("Alamat"));
        form.add(txtAlamat);
        form.add(new JLabel("No. Telepon"));
        form.add(txtTelepon);

        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tombol.add(btnTambah);
        tombol.add(btnUbah);
        tombol.add(btnHapus);
        tombol.add(btnBersih);
        tombol.add(new JLabel("   Cari:"));
        tombol.add(txtCari);

        JPanel atas = new JPanel(new BorderLayout(8, 8));
        atas.add(form, BorderLayout.NORTH);
        atas.add(tombol, BorderLayout.SOUTH);
        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        btnHapus.setEnabled(role.equals("admin"));   // hapus hanya admin

        btnTambah.addActionListener(e -> tambah());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnBersih.addActionListener(e -> bersihkan());
        txtCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refresh();   // pencarian langsung saat mengetik
            }
        });
        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tabel.getSelectedRow();
                if (r < 0) return;
                idDipilih = Integer.parseInt(tabel.getValueAt(r, 0).toString());
                txtNama.setText(tabel.getValueAt(r, 1).toString());
                txtAlamat.setText(tabel.getValueAt(r, 2).toString());
                txtTelepon.setText(tabel.getValueAt(r, 3).toString());
            }
        });

        refresh();
    }

    public void refresh() {
        try {
            List<Object[]> baris = new ArrayList<>();
            for (Pelanggan p : controller.cari(txtCari.getText().trim())) {
                baris.add(new Object[]{p.getId(), p.getNama(), p.getAlamat(), p.getTelepon()});
            }
            tabel.setModel(UiUtil.model(KOLOM, baris));
        } catch (Exception e) {
            UiUtil.error(this, e);
        }
    }

    private boolean valid() {
        if (txtNama.getText().trim().isEmpty() || txtAlamat.getText().trim().isEmpty()
                || txtTelepon.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama, alamat, dan telepon harus diisi.");
            return false;
        }
        if (!txtTelepon.getText().trim().matches("\\d{8,15}")) {
            JOptionPane.showMessageDialog(this, "Nomor telepon harus angka (8-15 digit).");
            return false;
        }
        return true;
    }

    private void tambah() {
        if (!valid()) return;
        try {
            controller.tambah(txtNama.getText().trim(), txtAlamat.getText().trim(), txtTelepon.getText().trim());
            bersihkan();
            refresh();
        } catch (Exception e) {
            UiUtil.error(this, e);
        }
    }

    private void ubah() {
        if (idDipilih < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu.");
            return;
        }
        if (!valid()) return;
        try {
            controller.ubah(idDipilih, txtNama.getText().trim(), txtAlamat.getText().trim(), txtTelepon.getText().trim());
            bersihkan();
            refresh();
        } catch (Exception e) {
            UiUtil.error(this, e);
        }
    }

    private void hapus() {
        if (idDipilih < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Hapus data pelanggan ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        try {
            if (controller.hapus(idDipilih)) {
                bersihkan();
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Pelanggan tidak bisa dihapus karena sudah memiliki transaksi.");
            }
        } catch (Exception e) {
            UiUtil.error(this, e);
        }
    }

    private void bersihkan() {
        idDipilih = -1;
        txtNama.setText("");
        txtAlamat.setText("");
        txtTelepon.setText("");
        tabel.clearSelection();
    }
}
