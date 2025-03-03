package tn.esprit.formations.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private static MyDatabase instance;
    private Connection connection;

    private final String url = "jdbc:mysql://localhost:3306/esprit";
    private final String user = "root";
    private final String password = "";

    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion à la base de données réussie !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }

    public static synchronized MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
