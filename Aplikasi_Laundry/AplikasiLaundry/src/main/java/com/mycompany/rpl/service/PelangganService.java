package com.mycompany.rpl.service;

import com.mycompany.rpl.dao.PelangganDAO;
import com.mycompany.rpl.model.Pelanggan;
import org.springframework.transaction.annotation.Transactional;

public class PelangganService {

    private PelangganDAO pelangganDAO;

    // IMPLEMENTASI DI: Setter Injection
    public void setPelangganDAO(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
    }

    // Declarative Transaction: tidak ada beginTransaction/commit/rollback manual
    @Transactional
    public void prosesSimpanPelanggan(String namaPelanggan, String noTelepon) {
        if (namaPelanggan == null || namaPelanggan.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pelanggan tidak boleh kosong.");
        }
        
        Pelanggan pelanggan = pelangganDAO.cariByNama(namaPelanggan);
        if (pelanggan == null) {
            // Data belum ada: buat baru
            pelanggan = new Pelanggan();
            pelanggan.setNama(namaPelanggan);
            pelanggan.setAlamat("-");        // kolom alamat NOT NULL di database
            pelanggan.setTelepon(noTelepon);
        } else {
            // Data sudah ada, lakukan update jika diperlukan
            pelanggan.setTelepon(noTelepon);
        }
        
        pelangganDAO.simpanAtauUpdate(pelanggan);
    }
}