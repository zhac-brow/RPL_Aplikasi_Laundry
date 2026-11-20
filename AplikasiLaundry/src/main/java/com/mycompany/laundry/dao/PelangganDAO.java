package com.mycompany.laundry.dao;

import java.util.List;
import com.mycompany.laundry.model.Pelanggan;
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

    public List<Pelanggan> semua() {
        return getSession().createQuery("from Pelanggan order by nama", Pelanggan.class).list();
    }

    public List<Pelanggan> cari(String kata) {
        return getSession()
                .createQuery("from Pelanggan where nama like :kw or alamat like :kw "
                        + "or telepon like :kw order by id", Pelanggan.class)
                .setParameter("kw", "%" + kata + "%")
                .list();
    }

    public Pelanggan cariById(int id) {
        return getSession().get(Pelanggan.class, id);
    }

    public Pelanggan cariByNama(String namaPelanggan) {
        return getSession()
                .createQuery("from Pelanggan p where p.nama = :nama", Pelanggan.class)
                .setParameter("nama", namaPelanggan)
                .setMaxResults(1)
                .uniqueResult();
    }

    public long jumlahTransaksi(int idPelanggan) {
        Long n = getSession()
                .createQuery("select count(t) from Transaksi t where t.pelanggan.id = :id", Long.class)
                .setParameter("id", idPelanggan)
                .uniqueResult();
        return n == null ? 0 : n;
    }

    public void simpanAtauUpdate(Pelanggan pelanggan) {
        getSession().saveOrUpdate(pelanggan);
    }

    public void hapus(Pelanggan pelanggan) {
        getSession().delete(pelanggan);
    }
}
