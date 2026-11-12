package com.mycompany.rpl.model;

import javax.persistence.*;

@Entity
@Table(name = "layanan")
public class Layanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nama;
    private String satuan;
    private double harga;

    public Layanan() {
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getSatuan() { return satuan; }
    public double getHarga() { return harga; }

    // dipakai JComboBox
    @Override
    public String toString() {
        return nama + " (per " + satuan + ")";
    }
}
