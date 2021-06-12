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
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

@SuppressWarnings({"unchecked", "rawtypes"})
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
    private JTextField klientImieField;
    private JTextField klientNazwiskoField;
    private JTextField klientUrField;
    private JTextField klientNumerField;
    private JLabel klientStatusLabel;
    private JComboBox klientFirmaBox;
    private JButton addClientButton;

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

        addClientButton.addActionListener(e -> {
            klientStatusLabel.setText("");
            klientStatusLabel.setForeground(Color.RED);

            // validate
            if (klientImieField.getText().isBlank() || klientNazwiskoField.getText().isBlank()
                    || klientNumerField.getText().isBlank() || klientUrField.getText().isBlank()){
                klientStatusLabel.setText("Proszę wypełnić wszystkie pola");
                return;
            }

            String imie = klientImieField.getText();
            String nazwisko = klientNazwiskoField.getText();
            Date data_ur;
            int numer;
            try {
                data_ur = Date.valueOf(klientUrField.getText());
            } catch (IllegalArgumentException exception){
                klientStatusLabel.setText("Data urodzenia powinna być zapisana w formacie rrrr-mm-dd");
                return;
            }
            try{
                numer = Integer.parseInt(klientNumerField.getText());
            } catch (NumberFormatException exception){
                klientStatusLabel.setText("Proszę podać poprawny numer telefonu");
                return;
            }
            Client client = new Client(-1,imie,nazwisko,numer,data_ur,Date.valueOf(LocalDate.now()));

            try {
                DatabaseConnector.createClient(client);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                JOptionPane.showMessageDialog(null,"Failed to create entry due to: " + throwables.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            // update client table
            Client.all_clients.clear();
            try {
                DatabaseConnector.getClients();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            ((ClientTableModel)clientTable.getModel()).fireTableDataChanged();


            // clear input fields
            klientImieField.setText("");
            klientNazwiskoField.setText("");
            klientUrField.setText("");
            klientNumerField.setText("");
            klientFirmaBox.setSelectedItem("-- brak --");
        });
    }

    private void prepareComboBox() {
        przedstawicielBox.removeAllItems();
        klientFirmaBox.removeAllItems();

        // Dodaj firmę
        przedstawicielBox.addItem("-- brak --");
        for (Client client : Client.all_clients) {
            // one client can be representing at most one company
            if(client.firma_id != 0)
                continue;

            String selectionString = client.imie + " " + client.nazwisko + " (" + client.id + ")";
            przedstawicielBox.addItem(selectionString);
        }

        // Dodaj klienta
        klientFirmaBox.addItem("-- brak --");
        for (Company company : Company.allCompanies) {
            klientFirmaBox.addItem(company.nazwa);
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
