package com.mycompany.laundry.dao;

import java.util.List;
import com.mycompany.laundry.model.Layanan;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class LayananDAO {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Layanan> semua() {
        return getSession().createQuery("from Layanan order by id", Layanan.class).list();
    }

    public Layanan cariById(int id) {
        return getSession().get(Layanan.class, id);
    }
}
