package com.mycompany.rpl.controller;

import java.util.List;
import com.mycompany.rpl.model.Pelanggan;
import com.mycompany.rpl.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PelangganController {

    public List<Pelanggan> cari(String kata) {
        String kw = "%" + kata + "%";
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Pelanggan where nama like :kw or alamat like :kw "
                    + "or telepon like :kw order by id", Pelanggan.class)
                    .setParameter("kw", kw)
                    .list();
        }
    }

    public void tambah(String nama, String alamat, String telepon) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.save(new Pelanggan(nama, alamat, telepon));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void ubah(int id, String nama, String alamat, String telepon) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Pelanggan p = s.get(Pelanggan.class, id);
            p.setNama(nama);
            p.setAlamat(alamat);
            p.setTelepon(telepon);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // false jika pelanggan sudah punya transaksi (tidak boleh dihapus)
    public boolean hapus(int id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Long jumlah = s.createQuery("select count(t) from Transaksi t where t.pelanggan.id = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (jumlah != null && jumlah > 0) {
                return false;
            }
            tx = s.beginTransaction();
            s.delete(s.get(Pelanggan.class, id));
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
