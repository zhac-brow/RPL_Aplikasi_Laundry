package com.mycompany.laundry.dao;

import java.time.LocalDate;
import java.util.List;
import com.mycompany.laundry.model.Transaksi;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class TransaksiDAO {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Transaksi> semua() {
        return getSession()
                .createQuery("select t from Transaksi t join fetch t.pelanggan "
                        + "join fetch t.layanan order by t.id desc", Transaksi.class)
                .list();
    }

    public List<Transaksi> antaraTanggal(LocalDate dari, LocalDate sampai) {
        return getSession()
                .createQuery("select t from Transaksi t join fetch t.pelanggan join fetch t.layanan "
                        + "where t.tanggalMasuk between :dari and :sampai "
                        + "order by t.tanggalMasuk, t.id", Transaksi.class)
                .setParameter("dari", dari)
                .setParameter("sampai", sampai)
                .list();
    }

    public Transaksi cariById(int id) {
        return getSession().get(Transaksi.class, id);
    }

    public void simpan(Transaksi transaksi) {
        getSession().save(transaksi);
    }
}
