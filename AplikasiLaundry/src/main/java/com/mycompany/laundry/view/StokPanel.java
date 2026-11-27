package com.mycompany.laundry.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.mycompany.laundry.model.Stok;
import com.mycompany.laundry.service.StokService;
import com.mycompany.laundry.util.HibernateUtil;

public class StokPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Nama Barang", "Satuan", "Stok", "Berkurang Otomatis"};

    private final StokService service = HibernateUtil.getBean(StokService.class);

    // form barang baru
    private final JTextField txtNama = new JTextField();
    private final JTextField txtSatuan = new JTextField("pcs");
    private final JTextField txtAwal = new JTextField("0");
    private final JCheckBox chkOtomatis = new JCheckBox("Berkurang otomatis (1 unit tiap 2 kg cucian)", true);
    private final JButton btnBarangBaru = new JButton("Tambah Barang");

    // form tambah stok
    private final JTextField txtTambah = new JTextField(6);
    private final JButton btnTambahStok = new JButton("Tambah Stok");
    private final JButton btnOtomatis = new JButton("Ubah Otomatis");
    private final JButton btnHapus = new JButton("Hapus Barang");

    private final JTable tabel = new JTable();

    public StokPanel(String role) {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 6));
        form.add(new JLabel("Nama Barang (mis. Detergen, Pewangi)"));
        form.add(txtNama);
        form.add(new JLabel("Satuan (mis. pcs, botol, sak)"));
        form.add(txtSatuan);
        form.add(new JLabel("Stok Awal"));
        form.add(txtAwal);
        form.add(new JLabel(""));
        form.add(chkOtomatis);

        JPanel tombolBaru = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tombolBaru.add(btnBarangBaru);

        JPanel tombolStok = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tombolStok.add(new JLabel("Pilih barang di tabel, jumlah masuk:"));
        tombolStok.add(txtTambah);
        tombolStok.add(btnTambahStok);
        tombolStok.add(btnOtomatis);
        tombolStok.add(btnHapus);

        JPanel bawahForm = new JPanel(new GridLayout(2, 1));
        bawahForm.add(tombolBaru);
        bawahForm.add(tombolStok);

        JPanel atas = new JPanel(new BorderLayout(8, 8));
        atas.add(form, BorderLayout.NORTH);
        atas.add(bawahForm, BorderLayout.SOUTH);
        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        // kasir hanya boleh melihat stok; input stok khusus admin
        boolean admin = role.equals("admin");
        for (JComponent c : new JComponent[]{txtNama, txtSatuan, txtAwal, chkOtomatis, btnBarangBaru,
            txtTambah, btnTambahStok, btnOtomatis, btnHapus}) {
            c.setEnabled(admin);
        }

        btnBarangBaru.addActionListener(e -> barangBaru());
        btnTambahStok.addActionListener(e -> tambahStok());
        btnOtomatis.addActionListener(e -> ubahOtomatis());
        btnHapus.addActionListener(e -> hapus());

        refresh();
    }

    public void refresh() {
        try {
            DefaultTableModel modelTabel = new DefaultTableModel(KOLOM, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            List<Object[]> baris = new ArrayList<>();
            for (Stok s : service.semua()) {
                baris.add(new Object[]{s.getId(), s.getNama(), s.getSatuan(), s.getJumlah(),
                    s.isOtomatis() ? "Ya" : "Tidak"});
            }
            for (Object[] b : baris) {
                modelTabel.addRow(b);
            }
            tabel.setModel(modelTabel);
        } catch (Exception e) {
            tampilkanError(e);
        }
    }

    private int angka(JTextField f) {
        try {
            return Integer.parseInt(f.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int idDipilih() {
        int r = tabel.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Pilih barang pada tabel terlebih dahulu.");
            return -1;
        }
        return Integer.parseInt(tabel.getValueAt(r, 0).toString());
    }

    private void barangBaru() {
        if (txtNama.getText().trim().isEmpty() || txtSatuan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama barang dan satuan harus diisi.");
            return;
        }
        int awal = angka(txtAwal);
        if (awal < 0) {
            JOptionPane.showMessageDialog(this, "Stok awal harus berupa angka bulat (0 atau lebih).");
            return;
        }
        try {
            service.tambahBarang(txtNama.getText(), txtSatuan.getText(), awal, chkOtomatis.isSelected());
            txtNama.setText("");
            txtAwal.setText("0");
            refresh();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            tampilkanError(e);
        }
    }

    private void tambahStok() {
        int id = idDipilih();
        if (id < 0) return;
        int tambah = angka(txtTambah);
        if (tambah <= 0) {
            JOptionPane.showMessageDialog(this, "Jumlah masuk harus berupa angka bulat lebih dari 0.");
            return;
        }
        try {
            service.tambahStok(id, tambah);
            txtTambah.setText("");
            refresh();
        } catch (Exception e) {
            tampilkanError(e);
        }
    }

    private void ubahOtomatis() {
        int r = tabel.getSelectedRow();
        int id = idDipilih();
        if (id < 0) return;
        boolean sekarang = "Ya".equals(tabel.getValueAt(r, 4).toString());
        try {
            service.ubahOtomatis(id, !sekarang);
            refresh();
        } catch (Exception e) {
            tampilkanError(e);
        }
    }

    private void hapus() {
        int id = idDipilih();
        if (id < 0) return;
        int ok = JOptionPane.showConfirmDialog(this, "Hapus barang ini dari stok?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        try {
            service.hapus(id);
            refresh();
        } catch (Exception e) {
            tampilkanError(e);
        }
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
