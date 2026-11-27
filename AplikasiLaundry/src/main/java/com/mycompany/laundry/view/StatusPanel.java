package com.mycompany.laundry.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import com.mycompany.laundry.service.TransaksiService;
import com.mycompany.laundry.util.HibernateUtil;
import com.mycompany.laundry.model.Transaksi;
import javax.swing.table.DefaultTableModel;

public class StatusPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Pelanggan", "Layanan", "Jumlah", "Tanggal Masuk", "Status"};
    private static final String[] STATUS = {"Diterima", "Dicuci", "Disetrika", "Selesai", "Sudah Diambil"};

    private final TransaksiService service = HibernateUtil.getBean(TransaksiService.class);

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
        try {
            List<Object[]> baris = new ArrayList<>();
            for (Transaksi t : service.semua()) {
                baris.add(new Object[]{t.getId(), t.getPelanggan().getNama(), t.getLayanan().getNama(),
                    t.getJumlah(), t.getTanggalMasuk(), t.getStatus()});
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
    }

    private void simpan() {
        int r = tabel.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Pilih pesanan pada tabel terlebih dahulu.");
            return;
        }
        int id = Integer.parseInt(tabel.getValueAt(r, 0).toString());
        try {
            service.ubahStatus(id, cmbStatus.getSelectedItem().toString());
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
