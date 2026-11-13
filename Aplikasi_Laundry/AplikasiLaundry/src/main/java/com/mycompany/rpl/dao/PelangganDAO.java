package com.mycompany.rpl.dao;

import com.mycompany.rpl.model.Pelanggan; // Sesuaikan dengan model Anda
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class PelangganDAO {

    private SessionFactory sessionFactory;

    // Setter Injection untuk Spring
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // Contoh method pencarian berdasarkan nama pelanggan
    public Pelanggan cariByNama(String namaPelanggan) {
        return getSession()
                .createQuery("FROM Pelanggan p WHERE p.nama = :nama", Pelanggan.class)
                .setParameter("nama", namaPelanggan)
                .uniqueResult();
    }

    // Menyimpan atau memperbarui data
    public void simpanAtauUpdate(Pelanggan pelanggan) {
        getSession().saveOrUpdate(pelanggan);
    }
}