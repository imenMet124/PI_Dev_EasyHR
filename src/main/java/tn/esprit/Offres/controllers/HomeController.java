package tn.esprit.Offres.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private BorderPane candidatureC;

    @FXML
    private BorderPane statC;

    @FXML
    private BorderPane entretienC;

    @FXML
    private BorderPane ajoutC;

    @FXML
    public void initialize() {
        // Ajouter des gestionnaires d'événements pour chaque BorderPane
        candidatureC.setOnMouseClicked(event -> ouvrirNouvellePageC(event));
        statC.setOnMouseClicked(event -> ouvrirNouvellePageS( event));
        entretienC.setOnMouseClicked(event -> ouvrirNouvellePageE( event));
        ajoutC.setOnMouseClicked(event -> ouvrirNouvellePageA( event));
    }

    @FXML
    private void ouvrirNouvellePageE(MouseEvent event) {
    }

    @FXML
    private void ouvrirNouvellePageS(MouseEvent event) {
    }


    public void ouvrirNouvellePageC(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewOffre.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((BorderPane) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void ouvrirNouvellePageA(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajout.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((BorderPane) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ouvrirPageOffres(MouseEvent event) {
        chargerVue("/ViewOffre.fxml");
    }

    @FXML
    private void ouvrirPageCandidatures(MouseEvent event) {
        chargerVue("/Candidatures.fxml");
    }

    @FXML
    private void ouvrirPageAjout(MouseEvent event) {
        chargerVue("/Ajout.fxml");
    }

    private void chargerVue(String cheminFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(cheminFxml));
            Parent root = loader.load();
            Stage stage = (Stage) ajoutC.getScene().getWindow(); // Récupère la fenêtre actuelle
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

