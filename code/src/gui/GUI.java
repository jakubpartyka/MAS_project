package gui;

import app.data.Company;
import app.data.events.Reservation;
import app.data.person.Client;
import app.database.DatabaseConnector;
import app.tables.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@SuppressWarnings({"unchecked", "rawtypes"})
public class GUI implements Runnable {
    public static LocalDate currentDate = LocalDate.now();
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
    private JTextField targetDateField;
    private JButton goButton;
    private JTable scheduleTable;
    private JLabel scheduleStatusLabel;
    private JButton goLeftButton;
    private JButton goRightButton;
    private JLabel detailsCourtName;
    private JLabel detailsClientName;
    private JLabel detailsClientPhone;
    private JLabel detailsResStatus;
    private JLabel detailsHeader;
    private JComboBox fromComboBox;
    private JComboBox toComboBox;
    private JComboBox clientComboBox;
    private JLabel reserveStatusLabel;
    private JLabel sumLabel;
    private JComboBox trainerComboBox;
    private JComboBox courtComboBox;
    private JTextField reserveDateField;
    private JButton saveReservationButton;
    private JButton checkAvailabilityButton;

    private JFrame frame;

    @Override
    public void run() {
        // download data
        getData();
        prepareTables();
        prepareComboBox();
        addActionListeners();

        initFrame();

        preapareUI();

        statusLabel1.setText("Zalogowany jako: " + DatabaseConnector.current_user);
        statusLabel2.setText(String.valueOf(LocalDate.now()));


        frame.setVisible(true);
    }

    private void preapareUI() {
        for (int i = 5; i < 7; i++)
            tabbedPane1.setBackgroundAt(i,Color.GREEN);

        targetDateField.setText(currentDate.toString());
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

        goLeftButton.addActionListener(e -> {
            scheduleStatusLabel.setText("");
            currentDate = currentDate.minus(1, ChronoUnit.DAYS);
            targetDateField.setText(currentDate.toString());
            ((ScheduleTableModel)scheduleTable.getModel()).fireTableDataChanged();
        });

        goRightButton.addActionListener(e -> {
            scheduleStatusLabel.setText("");
            currentDate = currentDate.plus(1,ChronoUnit.DAYS);
            targetDateField.setText(currentDate.toString());
            ((ScheduleTableModel)scheduleTable.getModel()).fireTableDataChanged();
        });

        goButton.addActionListener(e -> {
            try {
                currentDate = LocalDate.parse(targetDateField.getText());
                scheduleStatusLabel.setText("");
                ((ScheduleTableModel)scheduleTable.getModel()).fireTableDataChanged();
            } catch (DateTimeParseException e1){
                e1.printStackTrace();
                scheduleStatusLabel.setText("Data powinna zostać podana w formacie rrrr-mm-dd");
            }
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

        // Zarezerwuj
        for (int i = 0; i < 9; i++) {
            fromComboBox.addItem("1" + i + ":00");
            toComboBox.addItem("1" + i + ":00");
        }
    }

    private void prepareTables() {
        clientTable.setModel(new ClientTableModel());
        trainerTable.setModel(new TrainerTableModel());
        courtTable.setModel(new CourtTableModel());
        companyTable.setModel(new CompanyTableModel());
        scheduleTable.setModel(new ScheduleTableModel());

        resizeColumnWidth(clientTable);
        resizeColumnWidth(trainerTable);
        resizeColumnWidth(courtTable);
        resizeColumnWidth(companyTable);
        resizeColumnWidth(scheduleTable);

        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel selectionModel = scheduleTable.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if(selectionModel.getValueIsAdjusting())
                return;
            int row = scheduleTable.getSelectedRow();
            int col = scheduleTable.getSelectedColumn();
            String res_name = (String) scheduleTable.getValueAt(row,col);
            if (col > 0)
                viewReservationDetails(res_name);
        });

    }

    private void viewReservationDetails(String res_name){
        if(res_name.equals("-")){
            detailsHeader.setText("SZCZEGÓŁY REZERWACJI: PROSZĘ WYBRAĆ REZERWACJĘ ABY WYŚWIETLIĆ SZCZEGÓŁY");
            detailsCourtName.setText("KORT");
            detailsClientName.setText("KLIENT");
            detailsClientPhone.setText("TELEFON");
            detailsResStatus.setText("STATUS");
        }
        else {
            String [] data = res_name.split(":");
            String res_id = data[1];
            Reservation res = Reservation.getReservationById(Integer.parseInt(res_id));
            if(res == null)
                return;
            Client client = Client.getClientById(res.klientId);
            if(client == null)
                return;

            detailsHeader.setText("SZCZEGÓŁY REZERWACJI NR. " + res.id);
            detailsCourtName.setText("KORT NUMER " + res.kortId);
            detailsClientName.setText("KLIENT: " + client.imie + " " + client.nazwisko);
            detailsClientPhone.setText("TELEFON: " + client.getNumer());
            detailsResStatus.setText("STATUS: " + res.status);
        }
    }

    private void getData() {
        try {
            DatabaseConnector.getClients();
            DatabaseConnector.getTrainers();
            DatabaseConnector.getCourts();
            DatabaseConnector.getCompanies();
            DatabaseConnector.getReservations();
            System.out.println(Reservation.allReservations);
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

    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 10; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
}
