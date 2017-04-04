package main.java.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DB {

    private static DB instance = new DB();
    private static final String URL = "jdbc:mysql://termin8.tech/termin8";
    private static final String USER = "termin8";
    private static final String PASSWORD = "jeghaterbarnmedraraksent";
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private DB() {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection lagKobling() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.toString());

            System.out.println("ERROR: Unable to Connect to Database.");
        }
        return connection;
    }

    public static int getLastInsertID(String tableName) {
        String SQL = "SELECT LAST_INSERT_ID() AS last_id FROM " + tableName;
        try {
            Connection connection = DB.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(SQL);
            resultSet.beforeFirst();
            resultSet.next();
            int last_ID = resultSet.getInt("last_id");
            connection.close();
            return last_ID;
        } catch (SQLException e) {
            return -1;
        }
    }

    public static Connection getConnection() {
        return instance.lagKobling();
    }

}