package com.mycompany.rpl.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Membuat SessionFactory satu kali (membaca hibernate.cfg.xml)
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    public static synchronized void tutup() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
