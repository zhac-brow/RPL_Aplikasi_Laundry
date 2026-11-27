package com.mycompany.laundry.service;

import java.util.List;
import com.mycompany.laundry.dao.PelangganDAO;
import com.mycompany.laundry.model.Pelanggan;
import org.springframework.transaction.annotation.Transactional;

public class PelangganService {

    private PelangganDAO pelangganDAO;

    // Setter Injection: DAO disuntikkan oleh Spring
    public void setPelangganDAO(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
    }

    // Declarative Transaction: tidak ada beginTransaction/commit/rollback manual
    @Transactional(readOnly = true)
    public List<Pelanggan> cari(String kata) {
        return pelangganDAO.cari(kata);
    }

    @Transactional
    public void tambah(String nama, String alamat, String telepon) {
        pelangganDAO.simpanAtauUpdate(new Pelanggan(nama, alamat, telepon));
    }

    @Transactional
    public void ubah(int id, String nama, String alamat, String telepon) {
        Pelanggan p = pelangganDAO.cariById(id);
        p.setNama(nama);
        p.setAlamat(alamat);
        p.setTelepon(telepon);
        pelangganDAO.simpanAtauUpdate(p);
    }

    // false jika pelanggan sudah punya transaksi (tidak boleh dihapus)
    @Transactional
    public boolean hapus(int id) {
        if (pelangganDAO.jumlahTransaksi(id) > 0) {
            return false;
        }
        pelangganDAO.hapus(pelangganDAO.cariById(id));
        return true;
    }

    // Simpan berdasarkan nama: baru jika belum ada, update telepon jika sudah ada
    @Transactional
    public void prosesSimpanPelanggan(String namaPelanggan, String noTelepon) {
        if (namaPelanggan == null || namaPelanggan.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pelanggan tidak boleh kosong.");
        }

        Pelanggan pelanggan = pelangganDAO.cariByNama(namaPelanggan);
        if (pelanggan == null) {
            pelanggan = new Pelanggan();
            pelanggan.setNama(namaPelanggan);
            pelanggan.setAlamat("-");        // kolom alamat NOT NULL di database
            pelanggan.setTelepon(noTelepon);
        } else {
            pelanggan.setTelepon(noTelepon);
        }
        pelangganDAO.simpanAtauUpdate(pelanggan);
    }
}
