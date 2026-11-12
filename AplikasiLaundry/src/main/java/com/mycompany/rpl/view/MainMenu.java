package com.mycompany.rpl.view;

import java.awt.Component;
import javax.swing.*;

public class MainMenu extends JFrame {

    public MainMenu(String username, String role) {
        setTitle("Aplikasi Laundry - " + username + " (" + role + ")");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Menu Akun > Logout
        JMenuBar bar = new JMenuBar();
        JMenu menuAkun = new JMenu("Akun");
        JMenuItem itemLogout = new JMenuItem("Logout");
        itemLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        menuAkun.add(itemLogout);
        bar.add(menuAkun);
        setJMenuBar(bar);

        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Data Pelanggan", new PelangganPanel(role));
        tab.addTab("Transaksi Laundry", new TransaksiPanel());
        tab.addTab("Status Cucian", new StatusPanel());
        if (role.equals("admin")) {                 // laporan hanya untuk admin
            tab.addTab("Laporan", new LaporanPanel());
        }

        // data diperbarui setiap pindah tab
        tab.addChangeListener(e -> {
            Component c = tab.getSelectedComponent();
            if (c instanceof PelangganPanel) ((PelangganPanel) c).refresh();
            if (c instanceof TransaksiPanel) ((TransaksiPanel) c).refresh();
            if (c instanceof StatusPanel)    ((StatusPanel) c).refresh();
        });

        add(tab);
    }
}
