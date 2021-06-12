package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DB_ADDRESS = "185.201.115.126";
    private static final String DB_USERNAME = "mas_user";
    private static final String DB_PASSWORD = "Mas123!!!";
    private static final String DB_NAME = "mas";
    private static final int DB_PORT = 3306;

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
}
