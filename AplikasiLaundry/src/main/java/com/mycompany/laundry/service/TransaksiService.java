package com.mycompany.laundry.service;

import java.time.LocalDate;
import java.util.List;
import com.mycompany.laundry.dao.LayananDAO;
import com.mycompany.laundry.dao.PelangganDAO;
import com.mycompany.laundry.dao.TransaksiDAO;
import com.mycompany.laundry.model.Layanan;
import com.mycompany.laundry.model.Pelanggan;
import com.mycompany.laundry.model.Transaksi;
import org.springframework.transaction.annotation.Transactional;

public class TransaksiService {

    private TransaksiDAO transaksiDAO;
    private PelangganDAO pelangganDAO;
    private LayananDAO layananDAO;

    public void setTransaksiDAO(TransaksiDAO transaksiDAO) {
        this.transaksiDAO = transaksiDAO;
    }

    public void setPelangganDAO(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
    }

    public void setLayananDAO(LayananDAO layananDAO) {
        this.layananDAO = layananDAO;
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

    @Transactional
    public void simpan(Pelanggan pelanggan, Layanan layanan, double jumlah, double bayar, LocalDate tanggal) {
        Pelanggan p = pelangganDAO.cariById(pelanggan.getId());
        Layanan l = layananDAO.cariById(layanan.getId());
        double total = jumlah * l.getHarga();
        transaksiDAO.simpan(new Transaksi(p, l, jumlah, total, bayar, tanggal, "Diterima"));
    }

    @Transactional
    public void ubahStatus(int idTransaksi, String statusBaru) {
        transaksiDAO.cariById(idTransaksi).setStatus(statusBaru);
    }
}
