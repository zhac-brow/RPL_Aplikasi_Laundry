package com.mycompany.laundry.dao;

import com.mycompany.laundry.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class UserDAO {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public User cariByUsername(String username) {
        return getSession()
                .createQuery("from User where username = :u", User.class)
                .setParameter("u", username)
                .uniqueResult();
    }
}
