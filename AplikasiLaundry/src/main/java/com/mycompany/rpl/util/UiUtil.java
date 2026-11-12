package com.mycompany.rpl.util;

import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// Fungsi bantu untuk tampilan Swing
public class UiUtil {

    // Membuat model JTable (sel tidak bisa diedit)
    public static DefaultTableModel model(String[] kolom, List<Object[]> baris) {
        DefaultTableModel m = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Object[] b : baris) {
            m.addRow(b);
        }
        return m;
    }

    public static String rupiah(double d) {
        return String.format("Rp %,.0f", d);
    }

    public static void error(Component parent, Exception e) {
        Throwable t = e;
        while (t.getCause() != null) {
            t = t.getCause();
        }
        JOptionPane.showMessageDialog(parent, "Terjadi kesalahan: " + t.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
