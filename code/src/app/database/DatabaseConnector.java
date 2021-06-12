package app.database;

import app.credentials.Hasher;
import app.data.Klient;

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

            Klient klient = new Klient(id,imie,nazwisko,numer,data_ur,data_rej);
            klient.firma_id = firma_id;
        }

        close(connection);
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
}