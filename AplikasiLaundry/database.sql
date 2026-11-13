CREATE DATABASE IF NOT EXISTS db_laundry;
USE db_laundry;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(30) NOT NULL UNIQUE,
  password VARCHAR(64) NOT NULL,
  role ENUM('admin','kasir') NOT NULL
);

CREATE TABLE pelanggan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nama VARCHAR(60) NOT NULL,
  alamat VARCHAR(120) NOT NULL,
  telepon VARCHAR(15) NOT NULL
);

CREATE TABLE layanan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nama VARCHAR(40) NOT NULL,
  satuan VARCHAR(10) NOT NULL,
  harga DECIMAL(10,2) NOT NULL
);

CREATE TABLE transaksi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_pelanggan INT NOT NULL,
  id_layanan INT NOT NULL,
  jumlah DECIMAL(8,2) NOT NULL,
  total DECIMAL(12,2) NOT NULL,
  bayar DECIMAL(12,2) NOT NULL,
  tanggal_masuk DATE NOT NULL,
  status ENUM('Diterima','Dicuci','Disetrika','Selesai','Sudah Diambil') NOT NULL DEFAULT 'Diterima',
  FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id),
  FOREIGN KEY (id_layanan) REFERENCES layanan(id)
);

INSERT INTO users(username,password,role) VALUES
  ('admin', MD5('admin123'), 'admin'),
  ('kasir', MD5('kasir123'), 'kasir');

INSERT INTO layanan(nama,satuan,harga) VALUES
  ('Cuci Kering','kg',5000),
  ('Cuci Setrika','kg',7000),
  ('Setrika Saja','kg',4000),
  ('Selimut','pcs',20000);
