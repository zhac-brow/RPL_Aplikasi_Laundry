package com.mycompany.rpl.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import com.mycompany.rpl.controller.TransaksiController;
import com.mycompany.rpl.model.Transaksi;
import com.mycompany.rpl.util.UiUtil;

public class StatusPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Pelanggan", "Layanan", "Jumlah", "Tanggal Masuk", "Status"};
    private static final String[] STATUS = {"Diterima", "Dicuci", "Disetrika", "Selesai", "Sudah Diambil"};

    private final TransaksiController controller = new TransaksiController();

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
            for (Transaksi t : controller.semua()) {
                baris.add(new Object[]{t.getId(), t.getPelanggan().getNama(), t.getLayanan().getNama(),
                    t.getJumlah(), t.getTanggalMasuk(), t.getStatus()});
            }
            tabel.setModel(UiUtil.model(KOLOM, baris));
        } catch (Exception e) {
            UiUtil.error(this, e);
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
            controller.ubahStatus(id, cmbStatus.getSelectedItem().toString());
            refresh();
        } catch (Exception e) {
            UiUtil.error(this, e);
        }
    }
}
