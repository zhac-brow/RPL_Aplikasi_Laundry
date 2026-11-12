package com.mycompany.rpl.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import com.mycompany.rpl.controller.LaporanController;
import com.mycompany.rpl.model.Transaksi;
import com.mycompany.rpl.util.UiUtil;

public class LaporanPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Pelanggan", "Layanan", "Jumlah", "Total", "Tanggal Masuk", "Status"};

    private final LaporanController controller = new LaporanController();

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
        LocalDate dari, sampai;
        try {
            dari = LocalDate.parse(txtDari.getText().trim());
            sampai = LocalDate.parse(txtSampai.getText().trim());
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-mm-dd, contoh 2026-11-05.");
            return;
        }
        if (dari.isAfter(sampai)) {
            JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh melebihi tanggal akhir.");
            return;
        }
        try {
            List<Object[]> baris = new ArrayList<>();
            double total = 0;
            for (Transaksi t : controller.laporan(dari, sampai)) {
                baris.add(new Object[]{t.getId(), t.getPelanggan().getNama(), t.getLayanan().getNama(),
                    t.getJumlah(), t.getTotal(), t.getTanggalMasuk(), t.getStatus()});
                total += t.getTotal();
            }
            tabel.setModel(UiUtil.model(KOLOM, baris));
            lblJumlah.setText("Jumlah transaksi: " + baris.size());
            lblTotal.setText("Total pendapatan: " + UiUtil.rupiah(total));
        } catch (Exception e) {
            UiUtil.error(this, e);
        }
    }
}
