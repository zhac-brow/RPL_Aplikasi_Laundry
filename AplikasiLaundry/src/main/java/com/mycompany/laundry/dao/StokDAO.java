package com.mycompany.laundry.dao;

import java.util.List;
import com.mycompany.laundry.model.Stok;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class StokDAO {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Stok> semua() {
        return getSession().createQuery("from Stok order by nama", Stok.class).list();
    }

    // barang yang berkurang otomatis saat ada transaksi (detergen, pewangi, dst.)
    public List<Stok> semuaOtomatis() {
        return getSession().createQuery("from Stok where otomatis = true order by id", Stok.class).list();
    }

    public Stok cariById(int id) {
        return getSession().get(Stok.class, id);
    }

    public Stok cariByNama(String nama) {
        return getSession()
                .createQuery("from Stok s where lower(s.nama) = :nama", Stok.class)
                .setParameter("nama", nama.toLowerCase())
                .setMaxResults(1)
                .uniqueResult();
    }

    public void simpanAtauUpdate(Stok stok) {
        getSession().saveOrUpdate(stok);
    }

    public void hapus(Stok stok) {
        getSession().delete(stok);
    }

    // Kurangi stok secara atomik. Mengembalikan 0 jika stok tidak cukup (tidak ada yang berubah).
    public int kurangi(int id, int banyak) {
        return getSession()
                .createQuery("update Stok set jumlah = jumlah - :n where id = :id and jumlah >= :n")
                .setParameter("n", banyak)
                .setParameter("id", id)
                .executeUpdate();
    }
}
