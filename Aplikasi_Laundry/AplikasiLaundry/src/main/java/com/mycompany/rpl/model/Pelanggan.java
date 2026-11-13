package com.mycompany.rpl.model;

import javax.persistence.*;

@Entity
@Table(name = "pelanggan")
public class Pelanggan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nama;
    private String alamat;
    private String telepon;

    public Pelanggan() {
    }

    public Pelanggan(String nama, String alamat, String telepon) {
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
    public String getTelepon() { return telepon; }

    public void setNama(String nama) { this.nama = nama; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setTelepon(String telepon) { this.telepon = telepon; }

    // dipakai JComboBox
    @Override
    public String toString() {
        return nama;
    }
}
