package com.mycompany.rpl.controller;

import java.time.LocalDate;
import java.util.List;
import com.mycompany.rpl.model.Transaksi;
import com.mycompany.rpl.util.HibernateUtil;
import org.hibernate.Session;

public class LaporanController {

    public List<Transaksi> laporan(LocalDate dari, LocalDate sampai) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("select t from Transaksi t join fetch t.pelanggan join fetch t.layanan "
                    + "where t.tanggalMasuk between :dari and :sampai "
                    + "order by t.tanggalMasuk, t.id", Transaksi.class)
                    .setParameter("dari", dari)
                    .setParameter("sampai", sampai)
                    .list();
        }
    }
}
