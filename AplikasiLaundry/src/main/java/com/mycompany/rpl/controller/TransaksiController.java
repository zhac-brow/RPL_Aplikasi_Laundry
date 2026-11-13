package com.mycompany.rpl.controller;

import java.time.LocalDate;
import java.util.List;
import com.mycompany.rpl.model.Layanan;
import com.mycompany.rpl.model.Pelanggan;
import com.mycompany.rpl.model.Transaksi;
import com.mycompany.rpl.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TransaksiController {

    public List<Pelanggan> daftarPelanggan() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Pelanggan order by nama", Pelanggan.class).list();
        }
    }

    public List<Layanan> daftarLayanan() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Layanan order by id", Layanan.class).list();
        }
    }

    public List<Transaksi> semua() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("select t from Transaksi t join fetch t.pelanggan "
                    + "join fetch t.layanan order by t.id desc", Transaksi.class).list();
        }
    }

    public void simpan(Pelanggan pelanggan, Layanan layanan, double jumlah, double bayar, LocalDate tanggal) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Pelanggan p = s.get(Pelanggan.class, pelanggan.getId());
            Layanan l = s.get(Layanan.class, layanan.getId());
            double total = jumlah * l.getHarga();
            s.save(new Transaksi(p, l, jumlah, total, bayar, tanggal, "Diterima"));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void ubahStatus(int idTransaksi, String statusBaru) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.get(Transaksi.class, idTransaksi).setStatus(statusBaru);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
