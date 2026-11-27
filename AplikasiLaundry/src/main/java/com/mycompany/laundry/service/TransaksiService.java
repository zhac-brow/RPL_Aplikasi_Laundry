package com.mycompany.laundry.service;

import java.time.LocalDate;
import java.util.List;
import com.mycompany.laundry.dao.LayananDAO;
import com.mycompany.laundry.dao.PelangganDAO;
import com.mycompany.laundry.dao.StokDAO;
import com.mycompany.laundry.dao.TransaksiDAO;
import com.mycompany.laundry.model.Layanan;
import com.mycompany.laundry.model.Pelanggan;
import com.mycompany.laundry.model.Stok;
import com.mycompany.laundry.model.Transaksi;
import org.springframework.transaction.annotation.Transactional;

public class TransaksiService {

    private TransaksiDAO transaksiDAO;
    private PelangganDAO pelangganDAO;
    private LayananDAO layananDAO;
    private StokDAO stokDAO;

    // 1 unit detergen/pewangi terpakai untuk setiap 2 kg cucian (dibulatkan ke atas):
    // 1-2 kg = 1, 3-4 kg = 2, 5-6 kg = 3, dst.
    private static final int KG_PER_PEMAKAIAN = 2;

    public void setTransaksiDAO(TransaksiDAO transaksiDAO) {
        this.transaksiDAO = transaksiDAO;
    }

    public void setPelangganDAO(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
    }

    public void setLayananDAO(LayananDAO layananDAO) {
        this.layananDAO = layananDAO;
    }

    public void setStokDAO(StokDAO stokDAO) {
        this.stokDAO = stokDAO;
    }

    @Transactional(readOnly = true)
    public List<Pelanggan> daftarPelanggan() {
        return pelangganDAO.semua();
    }

    @Transactional(readOnly = true)
    public List<Layanan> daftarLayanan() {
        return layananDAO.semua();
    }

    @Transactional(readOnly = true)
    public List<Transaksi> semua() {
        return transaksiDAO.semua();
    }

    // Mengembalikan jumlah unit stok yang dipakai per barang otomatis (0 jika layanan bukan per kg).
    // Jika stok tidak cukup, IllegalStateException dilempar dan seluruh transaksi dibatalkan (rollback).
    @Transactional
    public int simpan(Pelanggan pelanggan, Layanan layanan, double jumlah, double bayar, LocalDate tanggal) {
        Pelanggan p = pelangganDAO.cariById(pelanggan.getId());
        Layanan l = layananDAO.cariById(layanan.getId());
        double total = jumlah * l.getHarga();
        transaksiDAO.simpan(new Transaksi(p, l, jumlah, total, bayar, tanggal, "Diterima"));
        return kurangiStok(l, jumlah);
    }

    private int kurangiStok(Layanan l, double kg) {
        String satuan = l.getSatuan() == null ? "" : l.getSatuan().toLowerCase();
        boolean perKilo = satuan.contains("kg") || satuan.contains("kilo");
        if (!perKilo) {
            return 0;
        }
        int pakai = (int) Math.ceil(kg / KG_PER_PEMAKAIAN);
        for (Stok s : stokDAO.semuaOtomatis()) {
            int sisa = s.getJumlah();
            if (stokDAO.kurangi(s.getId(), pakai) == 0) {
                throw new IllegalStateException("Stok " + s.getNama() + " tidak cukup (sisa " + sisa
                        + ", dibutuhkan " + pakai + "). Tambahkan stok terlebih dahulu di tab Stok Barang.");
            }
        }
        return pakai;
    }

    @Transactional
    public void ubahStatus(int idTransaksi, String statusBaru) {
        transaksiDAO.cariById(idTransaksi).setStatus(statusBaru);
    }
}
