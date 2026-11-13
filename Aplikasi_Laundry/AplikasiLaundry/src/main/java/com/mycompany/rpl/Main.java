package com.mycompany.rpl;

import javax.swing.SwingUtilities;
import com.mycompany.rpl.view.LoginForm;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
