package tn.esprit.Offres.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tn.esprit.Offres.entities.Candidature;
import tn.esprit.Offres.services.ServiceCandidature;

import java.sql.SQLException;
import java.util.List;

public class CandidaturesController {

    @FXML
    private ListView<String> candidaturesListView;

    @FXML
    private Button fermerButton;

    private final ServiceCandidature serviceCandidature = new ServiceCandidature();


    @FXML
    public void initialize() {
        chargerCandidatures();
    }

    private void chargerCandidatures() {
        try {
            List<String> formattedCandidatures = serviceCandidature.afficherCandidaturesFormatees();
            candidaturesListView.getItems().clear();
            candidaturesListView.getItems().addAll(formattedCandidatures);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("⚠ Erreur lors du chargement des candidatures : " + e.getMessage());
        }
    }

    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) fermerButton.getScene().getWindow();
        stage.close();
    }
}