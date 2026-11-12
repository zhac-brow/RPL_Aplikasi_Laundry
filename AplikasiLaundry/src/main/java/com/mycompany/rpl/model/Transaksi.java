package com.mycompany.rpl.model;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_pelanggan")
    private Pelanggan pelanggan;

    @ManyToOne
    @JoinColumn(name = "id_layanan")
    private Layanan layanan;

    private double jumlah;
    private double total;
    private double bayar;

    @Column(name = "tanggal_masuk")
    private LocalDate tanggalMasuk;

    private String status;

    public Transaksi() {
    }

    public Transaksi(Pelanggan pelanggan, Layanan layanan, double jumlah, double total,
                     double bayar, LocalDate tanggalMasuk, String status) {
        this.pelanggan = pelanggan;
        this.layanan = layanan;
        this.jumlah = jumlah;
        this.total = total;
        this.bayar = bayar;
        this.tanggalMasuk = tanggalMasuk;
        this.status = status;
    }

    public int getId() { return id; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public Layanan getLayanan() { return layanan; }
    public double getJumlah() { return jumlah; }
    public double getTotal() { return total; }
    public double getBayar() { return bayar; }
    public LocalDate getTanggalMasuk() { return tanggalMasuk; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
