package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.model.UserSession;
import tn.esprit.Users.entities.User;

import java.io.IOException;

public class AcceuilController {

    @FXML
    private Label nomUtilisateur;

    @FXML
    private Button gestionUser;

    @FXML
    private Button gestionOffres;

    @FXML
    private Button gestionTaches;

    @FXML
    private Button gestionEvenements;

    @FXML
    private Button gestionFormations;
    private AnchorPane dashboardView;
    @FXML
    private StackPane contentArea; // Conteneur où les vues seront chargées

    @FXML
    public void initialize() {
        User loggedInUser = UserSession.getInstance().getLoggedInUser();
        if (loggedInUser != null) {
            nomUtilisateur.setText("Utilisateur: " + loggedInUser.getIyedNomUser());
        } else {
            nomUtilisateur.setText("Utilisateur: Invité");
        }

        showDashboard();
    }

    @FXML
    private void ouvrirGestionUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
            Parent root = loader.load();

            // Get the current stage (Acceuil window)
            Stage stage = (Stage) gestionUser.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestion des Utilisateurs");
            stage.show();

            // Ensure mainPane is initialized properly
            MainMenuController mainMenuController = loader.getController();
            mainMenuController.initialize();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page: /MainMenu.fxml");
        }
    }


    @FXML
    private void ouvrirGestionOffres() {
        User loggedInUser = UserSession.getInstance().getLoggedInUser();
        if (loggedInUser != null) {
            UserRole role = loggedInUser.getIyedRoleUser(); // Get user role

            if (role == UserRole.RESPONSABLE_RH) {
                ouvrirNouvelleFenetre("/AdminOffre.fxml", "Gestion des Offres - Responsable RH");
            } else {
                ouvrirNouvelleFenetre("/offre ui.fxml", "Gestion des Offres - Employé");
            }
        } else {
            System.out.println("Aucun utilisateur connecté.");
        }
    }

    @FXML
    private void ouvrirGestionTaches() {
        ouvrirNouvelleFenetre("/views/Main.fxml", "Gestion des Tâches");
    }

    @FXML
    private void ouvrirGestionEvenements() {
        User loggedInUser = UserSession.getInstance().getLoggedInUser();
        if (loggedInUser != null) {
            UserRole role = loggedInUser.getIyedRoleUser(); // Get user role

            if (role == UserRole.RESPONSABLE_RH) {
                ouvrirNouvelleFenetre("/AdminEventsView.fxml", "Evenements - Responsable RH");
            } else {
                ouvrirNouvelleFenetre("/UserEventsView.fxml", "Evenements - Employé");
            }
        } else {
            System.out.println("Aucun utilisateur connecté.");
        }
    }

    @FXML
    private void ouvrirGestionFormations() {
        ouvrirNouvelleFenetre("/fxml/admin/AdminView.fxml", "Gestion des Formations");
    }

    private void ouvrirNouvelleFenetre(String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titre);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page: " + fxmlPath);
        }
    }

    @FXML
    public void showDashboard() {
        try {
            if (dashboardView == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardView.fxml"));
                dashboardView = loader.load(); // ✅ Ne pas caster en VBox

            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardView);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du Dashboard.");
        }
    }
}