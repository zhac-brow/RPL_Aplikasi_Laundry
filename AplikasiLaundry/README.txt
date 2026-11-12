APLIKASI LAUNDRY - MVC + HIBERNATE (Java with Maven)

STRUKTUR
  laundry.Main                  -> titik masuk program (klik kanan > Run File)
  laundry.model                 -> User, Pelanggan, Layanan, Transaksi (entity Hibernate)
  laundry.view                  -> LoginForm, MainMenu, PelangganPanel, TransaksiPanel,
                                   StatusPanel, LaporanPanel (semua UI Swing)
  laundry.controller            -> LoginController, PelangganController,
                                   TransaksiController, LaporanController (logika + Hibernate)
  laundry.util                  -> HibernateUtil (SessionFactory), UiUtil (bantu JTable/dialog)
  src/main/resources            -> hibernate.cfg.xml (koneksi + daftar entity)

CARA MENJALANKAN
1. Nyalakan MySQL (XAMPP), import database.sql lewat phpMyAdmin.
2. NetBeans: File > Open Project > pilih folder AplikasiLaundryMaven.
   Tunggu Maven mengunduh dependency (Hibernate + driver MySQL).
3. Jika MySQL memakai password/port berbeda, ubah di src/main/resources/hibernate.cfg.xml.
4. Klik kanan project > Clean and Build, lalu klik kanan Main.java > Run File.

Akun:  admin / admin123 (akses penuh)  |  kasir / kasir123 (tanpa Laporan, tanpa hapus pelanggan)
