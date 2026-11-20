package com.mycompany.laundry.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

// Memuat applicationContext.xml satu kali. Spring yang membuat SessionFactory,
// DAO, dan Service, lalu view mengambil Service lewat getBean().
public class HibernateUtil {

    private static ClassPathXmlApplicationContext context;

    public static synchronized ClassPathXmlApplicationContext getContext() {
        if (context == null) {
            ClassPathXmlApplicationContext baru = new ClassPathXmlApplicationContext("applicationContext.xml");
            baru.registerShutdownHook();
            context = baru;
        }
        return context;
    }

    public static <T> T getBean(Class<T> kelas) {
        return getContext().getBean(kelas);
    }

    public static synchronized void tutup() {
        if (context != null) {
            context.close();
            context = null;
        }
    }
}
