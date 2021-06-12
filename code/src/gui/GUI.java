package gui;

import app.data.Company;
import app.data.person.Client;
import app.database.DatabaseConnector;
import app.tables.ClientTableModel;
import app.tables.CompanyTableModel;
import app.tables.CourtTableModel;
import app.tables.TrainerTableModel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;

@SuppressWarnings("unchecked")
public class GUI implements Runnable {
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JLabel statusLabel1;
    private JLabel statusLabel2;
    private JPanel statusPanel;
    private JTable clientTable;
    private JTable trainerTable;
    private JTable courtTable;
    private JTable companyTable;
    private JTextField nazwaField;
    private JComboBox przedstawicielBox;
    private JTextField branzaField;
    private JTextField nipField;
    private JButton zapiszButton;
    private JLabel firmaStatusLabel;

    private JFrame frame;

    @Override
    public void run() {
        // download data
        getData();
        prepareTables();
        prepareComboBox();
        addActionListeners();

        initFrame();

        statusLabel1.setText("Zalogowany jako: " + DatabaseConnector.current_user);
        statusLabel2.setText(String.valueOf(LocalDate.now()));


        frame.setVisible(true);
    }

    private void addActionListeners() {
        zapiszButton.addActionListener(e -> {
            // validate input
            if(nazwaField.getText().isBlank() || nipField.getText().isBlank() || branzaField.getText().isBlank()) {
                firmaStatusLabel.setText("Należy wypełnić wszystkie pola");
                firmaStatusLabel.setForeground(Color.RED);
                return;
            }
            firmaStatusLabel.setText("");

            // create new Company object
            String nazwa = nazwaField.getText();
            String nip = nipField.getText();
            String branza = branzaField.getText();
            String przedstawicielRaw = (String) przedstawicielBox.getSelectedItem();

            Company company = new Company(-1,nazwa,branza,nip);
            try {
                DatabaseConnector.addCompany(company);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                JOptionPane.showMessageDialog(null,"Failed to create entry due to: " + throwables.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            // reload data from database
            Company.allCompanies.clear();
            try {
                DatabaseConnector.getCompanies();
                ((CompanyTableModel)companyTable.getModel()).fireTableDataChanged();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // parse przedstawiciel value
            assert przedstawicielRaw != null;
            String przedstawicielId = przedstawicielRaw.substring(przedstawicielRaw.indexOf("(") + 1);
            przedstawicielId = przedstawicielId.substring(0,przedstawicielId.indexOf(")"));
            System.out.println(przedstawicielId);

            Client client = Client.getClientById(Integer.parseInt(przedstawicielId));
            if(client != null) {
                Company newComp = Company.allCompanies.get(Company.allCompanies.size() - 1);
                client.firma_id = newComp.id;
                ((ClientTableModel)clientTable.getModel()).fireTableDataChanged();
                try {
                    DatabaseConnector.updateClient(client);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            // clear input fields
            nazwaField.setText("");
            nipField.setText("");
            branzaField.setText("");
            przedstawicielBox.setSelectedItem("-- brak --");


        });
    }

    private void prepareComboBox() {
        przedstawicielBox.addItem("-- brak --");
        for (Client client : Client.all_clients) {

            // one client can be representing at most one company
            if(client.firma_id != 0)
                continue;

            String selectionString = client.imie + " " + client.nazwisko + " (" + client.id + ")";
            przedstawicielBox.addItem(selectionString);
        }
    }

    private void prepareTables() {
        clientTable.setModel(new ClientTableModel());
        trainerTable.setModel(new TrainerTableModel());
        courtTable.setModel(new CourtTableModel());
        companyTable.setModel(new CompanyTableModel());
    }

    private void getData() {
        try {
            DatabaseConnector.getClients();
            DatabaseConnector.getTrainers();
            DatabaseConnector.getCourts();
            DatabaseConnector.getCompanies();
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
