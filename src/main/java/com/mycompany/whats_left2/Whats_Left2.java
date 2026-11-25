package com.mycompany.whats_left2;

import javax.swing.SwingUtilities;
import whats_left.view.LoginFrame;

/**
 * Titik masuk aplikasi WHAT'S LEFT.
 * Aplikasi selalu dimulai dari halaman login. Koneksi database dicek saat
 * login/daftar, dan jika gagal akan muncul dialog (bukan program diam-diam
 * berhenti tanpa jendela).
 *
 * @author LOQ
 */
public class Whats_Left2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
