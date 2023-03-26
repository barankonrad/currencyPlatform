package sample.gui.tools;

import java.sql.*;

public class DBConnection{

    private static final String url =
        "jdbc:sqlserver://" + PrivateData.serverName + ";DatabaseName=" + PrivateData.dbName + ";encrypt=true;" +
            "trustServerCertificate=true;integratedSecurity=true";

    private DBConnection(){}

    public static Connection getConnection(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url);
        }
        catch(ClassNotFoundException | SQLException e){
            System.out.println("Failed to connect to the database");
            throw new RuntimeException(e);
        }
    }
}


