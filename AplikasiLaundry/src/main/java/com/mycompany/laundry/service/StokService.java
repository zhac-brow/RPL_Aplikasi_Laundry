package com.mycompany.laundry.service;

import java.util.List;
import com.mycompany.laundry.dao.StokDAO;
import com.mycompany.laundry.model.Stok;
import org.springframework.transaction.annotation.Transactional;

public class StokService {

    private StokDAO stokDAO;

    public void setStokDAO(StokDAO stokDAO) {
        this.stokDAO = stokDAO;
    }

    @Transactional(readOnly = true)
    public List<Stok> semua() {
        return stokDAO.semua();
    }

    @Transactional
    public void tambahBarang(String nama, String satuan, int jumlahAwal, boolean otomatis) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama barang tidak boleh kosong.");
        }
        if (jumlahAwal < 0) {
            throw new IllegalArgumentException("Jumlah stok tidak boleh negatif.");
        }
        if (stokDAO.cariByNama(nama.trim()) != null) {
            throw new IllegalArgumentException("Barang \"" + nama.trim() + "\" sudah ada. Gunakan Tambah Stok.");
        }
        stokDAO.simpanAtauUpdate(new Stok(nama.trim(), satuan.trim(), jumlahAwal, otomatis));
    }

    // Input stok masuk (belanja/restock): jumlah lama + jumlah tambahan
    @Transactional
    public void tambahStok(int id, int tambahan) {
        if (tambahan <= 0) {
            throw new IllegalArgumentException("Jumlah tambahan harus lebih dari 0.");
        }
        Stok s = stokDAO.cariById(id);
        s.setJumlah(s.getJumlah() + tambahan);
        stokDAO.simpanAtauUpdate(s);
    }

    @Transactional
    public void ubahOtomatis(int id, boolean otomatis) {
        Stok s = stokDAO.cariById(id);
        s.setOtomatis(otomatis);
        stokDAO.simpanAtauUpdate(s);
    }

    @Transactional
    public void hapus(int id) {
        stokDAO.hapus(stokDAO.cariById(id));
    }
}
