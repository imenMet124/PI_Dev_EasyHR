package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.tache.entities.Tache;
import tn.esprit.tache.entities.Projet;
import tn.esprit.tache.services.TacheService;
import tn.esprit.tache.services.ProjetService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class TacheFormController {

    @FXML private TextField titreField;
    @FXML private TextArea descField;
    @FXML private ComboBox<String> prioriteCombo;
    @FXML private ComboBox<String> statutCombo;
    @FXML private DatePicker deadlinePicker;
    @FXML private ComboBox<String> projetCombo;
    @FXML private Button saveBtn;

    private TacheService tacheService;
    private ProjetService projetService;
    private Tache tacheToEdit;

    public void initData(Tache tache, TacheService tacheService, ProjetService projetService) {
        this.tacheService = tacheService;
        this.projetService = projetService;
        this.tacheToEdit = tache;

        // Chargement des projets dans la ComboBox
        List<Projet> projets = projetService.getAllProjets();
        for (Projet p : projets) {
            projetCombo.getItems().add(p.getNomProjet());
        }

        // Si édition, pré-remplir les champs
        if (tache != null) {
            titreField.setText(tache.getTitreTache());
            descField.setText(tache.getDescTache());
            prioriteCombo.setValue(tache.getPriorite());
            statutCombo.setValue(tache.getStatutTache());

            if (tache.getDeadline() != null) {
                deadlinePicker.setValue(convertToLocalDate(tache.getDeadline()));
            }

            projetCombo.setValue(tache.getProjet().getNomProjet());
        }else {
            // Creating a new task
            statutCombo.setValue("En cours");  // ✅ Set "En cours" as the default
            statutCombo.setDisable(true);      // ✅ Prevent user from changing it
        }
    }

    private LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert instanceof java.sql.Date) {
            return ((java.sql.Date) dateToConvert).toLocalDate();
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }



    @FXML
    private void saveTache() {
        if (titreField.getText().isEmpty() || projetCombo.getValue() == null || deadlinePicker.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Projet selectedProjet = projetService.getProjetByName(projetCombo.getValue());

        // Conversion correcte de la date
        Date deadline = Date.valueOf(deadlinePicker.getValue());

        if (tacheToEdit == null) { // Ajouter une nouvelle tâche
            Tache newTache = new Tache(
                    titreField.getText(),
                    descField.getText(),
                    prioriteCombo.getValue(),
                    statutCombo.getValue(),
                    deadline,
                    0,
                    selectedProjet
            );
            tacheService.ajouterTache(newTache);
        } else { // Modifier une tâche existante
            tacheToEdit.setTitreTache(titreField.getText());
            tacheToEdit.setDescTache(descField.getText());
            tacheToEdit.setPriorite(prioriteCombo.getValue());
            tacheToEdit.setStatutTache(statutCombo.getValue());
            tacheToEdit.setDeadline(deadline);
            tacheToEdit.setProjet(selectedProjet);
            tacheService.modifierTache(tacheToEdit);
        }

        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
