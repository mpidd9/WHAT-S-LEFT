/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.whats_left2;

import javax.swing.SwingUtilities;
import whats_left.view.MainFrame;
import whats_left.repository.DatabaseUtil;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author LOQ
 */
public class Whats_Left2 {

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseUtil.getConnection();

            System.out.println("Koneksi database berhasil!");

            connection.close();

            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            });

        } catch (SQLException e) {
            System.out.println("Koneksi database gagal!");
            System.out.println(e.getMessage());
        }
    }
}
