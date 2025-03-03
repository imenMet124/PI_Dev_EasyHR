package tn.esprit.Offres.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ListView<Candidature> candidaturesListView;


    @FXML
    private Button fermerButton;

    private final ServiceCandidature serviceCandidature = new ServiceCandidature();


    @FXML
    public void initialize() {
        chargerCandidatures();


    }



    private void chargerCandidatures() {
        try {
            //  Retrieve List<Candidature>
            List<Candidature> candidatures = serviceCandidature.afficherCandidaturesFormatees();

            //  Convert List<Candidature> to ObservableList<Candidature>
            ObservableList<Candidature> observableCandidatures = FXCollections.observableArrayList(candidatures);

            // Ensure ListView supports Candidature objects
            candidaturesListView.setItems(observableCandidatures);

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

    @FXML
    private void handleGeneratePdfReport() {
        // Assuming you have a method to get the selected candidature
        Candidature selectedCandidature = getSelectedCandidature();
        if (selectedCandidature != null) {
            selectedCandidature.generatePdfReport(selectedCandidature);
            System.out.println("PDF report generated successfully.");
        } else {
            System.out.println("No candidature selected.");
        }
    }

    private Candidature getSelectedCandidature() {
        return candidaturesListView.getSelectionModel().getSelectedItem();
    }

}