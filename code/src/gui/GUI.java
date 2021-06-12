package gui;

import app.database.DatabaseConnector;
import app.tables.ClientTableModel;
import app.tables.CourtTableModel;
import app.tables.TrainerTableModel;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.time.LocalDate;

public class GUI implements Runnable {
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JLabel statusLabel1;
    private JLabel statusLabel2;
    private JPanel statusPanel;
    private JTable clientTable;
    private JTable trenerTable;
    private JTable courtTable;

    private JFrame frame;

    @Override
    public void run() {
        // download data
        getData();
        prepareTables();

        initFrame();

        statusLabel1.setText("Zalogowany jako: " + DatabaseConnector.current_user);
        statusLabel2.setText(String.valueOf(LocalDate.now()));


        frame.setVisible(true);
    }

    private void prepareTables() {
        clientTable.setModel(new ClientTableModel());
        trenerTable.setModel(new TrainerTableModel());
        courtTable.setModel(new CourtTableModel());
    }

    private void getData() {
        try {
            DatabaseConnector.getClients();
            DatabaseConnector.getTrainers();
            DatabaseConnector.getCourts();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Failed to connect to database","ERROR",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initFrame() {
        frame = new JFrame("WKS Warszawianka - panel zarządzania");
        frame.add(mainPanel);
        frame.setSize(800,600);
        frame.setMinimumSize(new Dimension(400,300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setAlwaysOnTop(false);
    }
}
