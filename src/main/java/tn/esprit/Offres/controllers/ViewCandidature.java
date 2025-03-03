package tn.esprit.Offres.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.Offres.entities.Candidature;
import tn.esprit.Offres.services.ServiceCandidature;

import java.sql.SQLException;
import java.util.List;

public class ViewCandidature {

    @FXML
    private ListView<Candidature> listCre; // ListView pour afficher les candidatures

    private ServiceCandidature serviceCandidature; // Service pour récupérer les candidatures

    public ViewCandidature() {
        serviceCandidature = new ServiceCandidature(); // Initialisation du service
    }

    @FXML
    public void initialize() {
        try {
            // Récupérer les candidatures à partir du service
            List<Candidature> candidatures = serviceCandidature.afficher();
            // Créer une ObservableList pour lier avec la ListView
            ObservableList<Candidature> observableCandidatures = FXCollections.observableArrayList(candidatures);
            // Définir les éléments de la ListView
            listCre.setItems(observableCandidatures);
            // Définir le type de cellule personnalisée si nécessaire
            listCre.setCellFactory(param -> new CandidatureCell());
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs SQL
        }
    }
    // Cellule personnalisée pour afficher les informations des candidatures dans la ListView
    private static class CandidatureCell extends javafx.scene.control.ListCell<Candidature> {
        @Override
        protected void updateItem(Candidature item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {

                setText(

                        "Candidat: " + item.getCandidat().getIdCandidat() + " "  +
                                " | Offre: " + item.getOffre().getTitrePoste() +
                                " | Statut: " + item.getStatutCandidature() +
                                " | Date: " + item.getDateCandidature()
                );
            }
        }
    }
}