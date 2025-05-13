package tn.esprit.tache.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Singleton pour gérer la connexion à la base de données.
 * Assure qu'une seule instance de la connexion est utilisée dans toute l'application.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/base";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static DBConnection instance;
    private Connection connection;

    /**
     * Constructeur privé pour empêcher l'instanciation directe.
     * Établit la connexion initiale avec la base de données.
     */
    private DBConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion à la base de données réussie !");
        } catch (SQLException e) {
            System.out.println("❌ Échec de la connexion à la base de données : " + e.getMessage());
        }
    }

    /**
     * Retourne l'instance unique de DBConnection.
     * Si l'instance n'existe pas encore, elle est créée.
     *
     * @return l'instance DBConnection
     */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * Retourne la connexion à la base de données.
     * Si la connexion est fermée, une tentative de reconnexion est effectuée.
     *
     * @return l'objet Connection actif
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("⚠ La connexion était fermée. Tentative de reconnexion...");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("🔄 Connexion rétablie avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la reconnexion : " + e.getMessage());
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données si elle est ouverte.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔴 Connexion à la base de données fermée.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}
