APLIKASI LAUNDRY - MVC + HIBERNATE (Java with Maven)

STRUKTUR
  com.mycompany.rpl.Main                  -> titik masuk program (klik kanan > Run File)
  com.mycompany.rpl.model                 -> User, Pelanggan, Layanan, Transaksi (entity Hibernate)
  com.mycompany.rpl.view                  -> LoginForm, MainMenu, PelangganPanel, TransaksiPanel,
                                   StatusPanel, LaporanPanel (semua UI Swing)
  com.mycompany.rpl.controller            -> LoginController, PelangganController,
                                   TransaksiController, LaporanController (logika + Hibernate)
  com.mycompany.rpl.util                  -> HibernateUtil (SessionFactory)
  src/main/resources            -> hibernate.cfg.xml (koneksi + daftar entity)

CARA MENJALANKAN
1. Nyalakan MySQL (XAMPP), import database.sql lewat phpMyAdmin.
2. NetBeans: File > Open Project > pilih folder AplikasiLaundryMaven.
   Tunggu Maven mengunduh dependency (Hibernate + driver MySQL).
3. Nama database: db_laundry (sudah sama di database.sql, hibernate.cfg.xml, dan applicationContext.xml).
   Jika MySQL memakai password/port berbeda, ubah di src/main/resources/hibernate.cfg.xml.
4. Klik kanan project > Clean and Build, lalu klik kanan Main.java > Run File.

Akun:  admin / admin123 (akses penuh)  |  kasir / kasir123 (tanpa Laporan, tanpa hapus pelanggan)
