package laundry;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

public class PelangganPanel extends JPanel {

    private static final String[] KOLOM = {"ID", "Nama", "Alamat", "Telepon"};

    private final JTextField txtNama = new JTextField();
    private final JTextField txtAlamat = new JTextField();
    private final JTextField txtTelepon = new JTextField();
    private final JButton btnTambah = new JButton("Tambah");
    private final JButton btnUbah = new JButton("Ubah");
    private final JButton btnBersih = new JButton("Bersihkan");
    private final JTable tabel = new JTable();
    private int idDipilih = -1;

    public PelangganPanel(String role) {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Nama"));
        form.add(txtNama);
        form.add(new JLabel("Alamat"));
        form.add(txtAlamat);
        form.add(new JLabel("No. Telepon"));
        form.add(txtTelepon);

        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tombol.add(btnTambah);
        tombol.add(btnUbah);
        tombol.add(btnBersih);

        JPanel atas = new JPanel(new BorderLayout(8, 8));
        atas.add(form, BorderLayout.NORTH);
        atas.add(tombol, BorderLayout.SOUTH);
        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);


        btnTambah.addActionListener(e -> tambah());
        btnUbah.addActionListener(e -> ubah());
        btnBersih.addActionListener(e -> bersihkan());

        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tabel.getSelectedRow();
                if (r < 0) return;
                idDipilih = Integer.parseInt(tabel.getValueAt(r, 0).toString());
                txtNama.setText(tabel.getValueAt(r, 1).toString());
                txtAlamat.setText(tabel.getValueAt(r, 2).toString());
                txtTelepon.setText(tabel.getValueAt(r, 3).toString());
            }
        });

        refresh();
    }

    public void refresh() {
        Koneksi.isiTabel(tabel, KOLOM,
                    "SELECT id, nama, alamat, telepon FROM pelanggan ORDER BY id");
    }
    private boolean valid() {
        if (txtNama.getText().trim().isEmpty() || txtAlamat.getText().trim().isEmpty()
                || txtTelepon.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama, alamat, dan telepon harus diisi.");
            return false;
        }
        if (!txtTelepon.getText().trim().matches("\\d{8,15}")) {
            JOptionPane.showMessageDialog(this, "Nomor telepon harus angka (8-15 digit).");
            return false;
        }
        return true;
    }

    private void tambah() {
        if (!valid()) return;
        try {
            Koneksi.exec("INSERT INTO pelanggan(nama, alamat, telepon) VALUES(?,?,?)",
                    txtNama.getText().trim(), txtAlamat.getText().trim(), txtTelepon.getText().trim());
            bersihkan();
            refresh();
        } catch (SQLException e) {
            Koneksi.error(e);
        }
    }

    private void ubah() {
        if (idDipilih < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu.");
            return;
        }
        if (!valid()) return;
        try {
            Koneksi.exec("UPDATE pelanggan SET nama=?, alamat=?, telepon=? WHERE id=?",
                    txtNama.getText().trim(), txtAlamat.getText().trim(), txtTelepon.getText().trim(), idDipilih);
            bersihkan();
            refresh();
        } catch (SQLException e) {
            Koneksi.error(e);
        }
    }


    private void bersihkan() {
        idDipilih = -1;
        txtNama.setText("");
        txtAlamat.setText("");
        txtTelepon.setText("");
        tabel.clearSelection();
    }
}
