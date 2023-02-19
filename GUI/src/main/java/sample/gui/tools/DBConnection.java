package sample.gui.tools;

import java.sql.*;

public class DBConnection{
    private static final String serverName = "LAPTOP-UI0RMMIG";
    private static final String dbName = "CurrencyDB";
    private static final String url = "jdbc:sqlserver://" + serverName + ";DatabaseName=" + dbName + ";encrypt=true;" +
            "trustServerCertificate=true;integratedSecurity=true";

    private static Connection connection;

    public static void makeConnection(){

        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connection = DriverManager.getConnection(url);
            System.out.println("Successfully connected to the database");
            DBConnection.connection = connection;
        }
        catch(ClassNotFoundException | SQLException e){
            System.out.println("Failed to connect to the database");
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(){
        return connection;
    }
}


