package tn.esprit.Offres.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Base {
    private final String URL="jdbc:mysql://localhost:3306/Base";
    private final String USER="root";
    private final String PSW="";

    private Connection connection;
    private static Base instance;

    private Base(){
        try {
            connection = DriverManager.getConnection(URL,USER,PSW);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Base getInstance(){
        if(instance == null)
            instance = new Base();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
