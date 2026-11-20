package com.mycompany.laundry.view;

import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mycompany.laundry.model.Transaksi;
import com.mycompany.laundry.service.LaporanService;
import com.mycompany.laundry.util.HibernateUtil;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class LaporanPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Pelanggan", "Layanan", "Jumlah", "Total", "Tanggal Masuk", "Status"};

    private final LaporanService service = HibernateUtil.getBean(LaporanService.class);

    private final JTextField txtDari = new JTextField(LocalDate.now().withDayOfMonth(1).toString(), 10);
    private final JTextField txtSampai = new JTextField(LocalDate.now().toString(), 10);
    private final JButton btnTampil = new JButton("Tampilkan");
    private final JButton btnCetak = new JButton("Cetak");
    private final JTable tabel = new JTable();
    private final JLabel lblJumlah = new JLabel("Jumlah transaksi: 0");
    private final JLabel lblTotal = new JLabel("Total pendapatan: Rp 0");

    public LaporanPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Hilangkan kotak kecil (focus border) dan samakan ukuran tombol
        btnTampil.setFocusPainted(false);
        btnCetak.setFocusPainted(false);
        btnCetak.setPreferredSize(btnTampil.getPreferredSize());

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.add(new JLabel("Dari (yyyy-mm-dd):"));
        filter.add(txtDari);
        filter.add(new JLabel("Sampai:"));
        filter.add(txtSampai);
        filter.add(btnTampil);
        filter.add(btnCetak);

        JPanel ringkas = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 5));
        ringkas.add(lblJumlah);
        ringkas.add(lblTotal);

        add(filter, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
        add(ringkas, BorderLayout.SOUTH);

        btnTampil.addActionListener(e -> tampilkan());
        btnCetak.addActionListener(e -> cetakLaporan());
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
            for (Transaksi t : service.laporan(dari, sampai)) {
                baris.add(new Object[]{t.getId(), t.getPelanggan().getNama(), t.getLayanan().getNama(),
                    t.getJumlah(), t.getTotal(), t.getTanggalMasuk(), t.getStatus()});
                total += t.getTotal();
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
            lblJumlah.setText("Jumlah transaksi: " + baris.size());
            lblTotal.setText("Total pendapatan: " + String.format("Rp %,.0f", total));
        } catch (Exception e) {
            tampilkanError(e);
        }
    }

    private void cetakLaporan() {
        try {
            // 1. Lokasi file .jrxml (sesuaikan nama & lokasi file jrxml di proyek Anda)
            String reportPath = "src/main/java/reports/Simple_Blue.jrxml";

            // 2. Compile file jrxml terlebih dahulu
            JasperReport jr = JasperCompileManager.compileReport(reportPath);

            // 3. Koneksi database dari Spring DataSource melalui HibernateUtil
            DataSource ds = HibernateUtil.getBean(DataSource.class);
            try (Connection conn = ds.getConnection()) {

                // 4. Parameter (kosongkan jika tidak ada, atau isi jika filter tanggal dipakai di jrxml)
                Map<String, Object> param = new HashMap<>();
                // Contoh jika di jrxml ada parameter tanggal:
                // param.put("dari", java.sql.Date.valueOf(txtDari.getText().trim()));
                // param.put("sampai", java.sql.Date.valueOf(txtSampai.getText().trim()));

                // 5. Isi laporan
                JasperPrint jp = JasperFillManager.fillReport(jr, param, conn);

                // 6. Tampilkan laporan (false agar aplikasi utama tidak ikut tertutup saat viewer ditutup)
                JasperViewer.viewReport(jp, false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mencetak laporan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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