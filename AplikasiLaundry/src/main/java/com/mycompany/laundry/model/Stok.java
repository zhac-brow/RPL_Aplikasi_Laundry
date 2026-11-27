package com.mycompany.laundry.model;

import javax.persistence.*;

@Entity
@Table(name = "stok")
public class Stok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nama;
    private String satuan;
    private int jumlah;

    // true = berkurang otomatis setiap ada transaksi cucian per kilo (mis. detergen, pewangi)
    private boolean otomatis;

    public Stok() {
    }

    public Stok(String nama, String satuan, int jumlah, boolean otomatis) {
        this.nama = nama;
        this.satuan = satuan;
        this.jumlah = jumlah;
        this.otomatis = otomatis;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getSatuan() { return satuan; }
    public int getJumlah() { return jumlah; }
    public boolean isOtomatis() { return otomatis; }

    public void setNama(String nama) { this.nama = nama; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setOtomatis(boolean otomatis) { this.otomatis = otomatis; }

    @Override
    public String toString() {
        return nama;
    }
}
