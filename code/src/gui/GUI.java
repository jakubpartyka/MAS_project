package gui;

import app.database.DatabaseConnector;

import javax.swing.*;
import java.time.LocalDate;

public class GUI implements Runnable {
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JLabel statusLabel1;
    private JLabel statusLabel2;
    private JPanel statusPanel;

    private JFrame frame;

    @Override
    public void run() {
        initFrame();

        statusLabel1.setText("Zalogowany jako: " + DatabaseConnector.current_user);
        statusLabel2.setText(String.valueOf(LocalDate.now()));


        frame.setVisible(true);
    }

    private void initFrame() {
        frame = new JFrame("WKS Warszawianka - panel zarządzania");
        frame.add(mainPanel);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setAlwaysOnTop(false);
    }
}
