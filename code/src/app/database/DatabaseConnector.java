package app.database;

import app.credentials.Hasher;
import app.data.Company;
import app.data.Court;
import app.data.person.Client;
import app.data.person.Trainer;
import java.sql.*;

public class DatabaseConnector {
    private static final String DB_ADDRESS = "185.201.115.126";
    private static final String DB_USERNAME = "mas_user";
    private static final String DB_PASSWORD = "Mas123!!!";
    private static final String DB_NAME = "mas";
    private static final int DB_PORT = 3306;

    public static String current_user = "";

    @SuppressWarnings("unused")
    public static void testConnection(){
        try {
            Connection connection = connect();
            close(connection);
            System.out.println("connection successful");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        Connection connection;

        //establish connection
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("A fatal error occurred:\n" + e.getMessage());
        }
        String address = "jdbc:mysql://" + DB_ADDRESS + ':' + DB_PORT + '/' + DB_NAME + "?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false";
        connection = DriverManager.getConnection(address, DB_USERNAME, DB_PASSWORD);

        return connection;
    }

    public static void close(Connection connection){
        try{
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void getClients() throws SQLException {
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(Queries.GET_CLIENTS.expression);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String imie = resultSet.getString(2);
            String nazwisko = resultSet.getString(3);
            Date data_ur = resultSet.getDate(4);
            int numer = resultSet.getInt(5);
            Date data_rej = resultSet.getDate(6);
            int firma_id = resultSet.getInt(7);

            Client client = new Client(id,imie,nazwisko,numer,data_ur,data_rej);
            client.firma_id = firma_id;
        }

        close(connection);
    }

    public static void getTrainers() throws SQLException {
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(Queries.GET_TRAINERS.expression);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String imie = resultSet.getString(2);
            String nazwisko = resultSet.getString(3);
            Date data_ur = resultSet.getDate(4);
            int numer = resultSet.getInt(5);
            String poziom = resultSet.getString(6);
            String opis = resultSet.getString(7);

            new Trainer(id,imie,nazwisko,numer,data_ur,poziom,opis);
        }
        close(connection);
    }

    public static void getCourts() throws SQLException {
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(Queries.GET_COURTS.expression);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String nawierzchnia = resultSet.getString(2);
            int cena = resultSet.getInt(3);
            boolean oswietlenie = resultSet.getBoolean(4);
            boolean kryty = resultSet.getBoolean(4);
            new Court(id,cena,nawierzchnia,oswietlenie,kryty);
        }
    }

    public static void getCompanies() throws SQLException {
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(Queries.GET_COMPANIES.expression);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String nazwa = resultSet.getString(2);
            String nip = resultSet.getString(3);
            String branza = resultSet.getString(4);
            new Company(id,nazwa,nip,branza);
        }
    }

    public static boolean authorize(String user, String pass) throws SQLException {
        current_user = user;

        String db_pass;
        String hashed_pass = Hasher.hash(pass);

        // connect to database and get correct pass hash for this user
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(Queries.LOGIN.expression);
        statement.setString(1,user);
        ResultSet resultSet = statement.executeQuery();

        try {
            resultSet.next();
            db_pass = resultSet.getString(1);
        } catch (SQLException e){
            if(e.getMessage().contains("empty result set"))
                return false;
            else
                throw e;
        }
        close(connection);

        //verify if password correct
        return db_pass.equals(hashed_pass);
    }

    public static void addCompany(Company company) throws SQLException {
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(Queries.ADD_COMPANY.expression);
        statement.setString(1,company.nazwa);
        statement.setString(2,company.NIP);
        statement.setString(3,company.branza);
        statement.executeUpdate();
        close(connection);
    }

    public static void updateClient(Client client) throws SQLException {
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_CLIENT.expression);
        statement.setInt(1,client.getNumer());
        statement.setInt(2,client.firma_id);
        statement.setInt(3,client.id);
        statement.executeUpdate();
        close(connection);
    }

    public static void createClient(Client client) throws SQLException {
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(Queries.CREATE_CLIENT.expression);
        statement.setString(1,client.imie);
        statement.setString(2,client.nazwisko);
        statement.setDate(3,client.data_ur);
        statement.setInt(4,client.getNumer());
        statement.setDate(5,client.data_rejestracji);
        statement.executeUpdate();
        close(connection);
    }
}