package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.tache.entities.Tache;
import tn.esprit.tache.entities.Projet;
import tn.esprit.tache.services.TacheService;
import tn.esprit.tache.services.ProjetService;
import tn.esprit.tache.services.AffectationService;
import tn.esprit.tache.services.EmployeService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TacheAndAffectationFormController {

    @FXML private TextField titreField;
    @FXML private TextArea descField;
    @FXML private ComboBox<String> prioriteCombo;
    @FXML private ComboBox<String> statutCombo;
    @FXML private DatePicker deadlinePicker;
    @FXML private ComboBox<String> projetCombo;
    @FXML private ComboBox<String> employeCombo;
    @FXML private ComboBox<String> tacheCombo;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;

    private TacheService tacheService;
    private ProjetService projetService;
    private AffectationService affectationService;
    private EmployeService employeService;
    private Tache tacheToEdit;
    private Map<Integer, String> tacheMap;
    private Map<Integer, String> employeMap;

    // Initialization for Tache form (for creating or editing tasks)
    public void initDataForTache(Tache tache, TacheService tacheService, ProjetService projetService) {
        this.tacheService = tacheService;
        this.projetService = projetService;
        this.tacheToEdit = tache;

        // Chargement des projets dans la ComboBox
        List<Projet> projets = projetService.getAllProjets();
        for (Projet p : projets) {
            if ("En cours".equals(p.getStatutProjet())) {  // Filter projects by status
                projetCombo.getItems().add(p.getNomProjet());
            }
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
        } else {
            // Création d'une nouvelle tâche
            statutCombo.setValue("En cours");  // Défaut "En cours"
            statutCombo.setDisable(true);      // L'utilisateur ne peut pas le modifier
        }
    }

    // Initialization for Affectation form (for assigning employees to tasks)
    public void initDataForAffectation(AffectationService affectationService, EmployeService employeService, TacheService tacheService) {
        this.affectationService = affectationService;
        this.tacheMap = tacheService.getTacheNames();
        this.employeMap = employeService.getEmployeNames();

        employeCombo.getItems().addAll(employeMap.values());
        tacheCombo.getItems().addAll(tacheMap.values());
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
        if (isInputValidForTache()) {
            Projet selectedProjet = projetService.getProjetByName(projetCombo.getValue());
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
    }

    @FXML
    private void saveAffectation() {
        if (employeCombo.getValue() == null || tacheCombo.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un employé et une tâche.");
            return;
        }

        // Get selected Name
        String selectedEmploye = employeCombo.getValue();
        String selectedTache = tacheCombo.getValue();

        // Convert Name to ID
        int idEmp = employeMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(selectedEmploye))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);

        int idTache = tacheMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(selectedTache))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);

        // Check if IDs are valid
        if (idEmp == -1 || idTache == -1) {
            showAlert("Erreur", "Problème de correspondance des données.");
            return;
        }

        // Insert Affectation
        affectationService.affecterEmployeTache(idEmp, idTache);
        closeForm();
    }

    private boolean isInputValidForTache() {
        StringBuilder errorMessage = new StringBuilder();

        // Vérification du titre
        if (titreField.getText() == null || titreField.getText().trim().isEmpty()) {
            errorMessage.append("Le titre de la tâche ne peut pas être vide.\n");
        } else if (!Pattern.matches("^[A-Za-z0-9À-ÖØ-öø-ÿ\\s-]+$", titreField.getText())) {
            errorMessage.append("Le titre de la tâche ne peut contenir que des lettres, chiffres et espaces.\n");
        }

        // Vérification de la description
        if (descField.getText() == null || descField.getText().trim().isEmpty()) {
            errorMessage.append("La description de la tâche ne peut pas être vide.\n");
        } else if (descField.getText().trim().length() < 10) {
            errorMessage.append("La description doit contenir au moins 10 caractères.\n");
        }

        // Vérification de la priorité
        if (prioriteCombo.getValue() == null) {
            errorMessage.append("Veuillez sélectionner une priorité.\n");
        }

        // Vérification du statut
        if (statutCombo.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un statut.\n");
        }

        // Vérification de la deadline
        if (deadlinePicker.getValue() == null) {
            errorMessage.append("Veuillez sélectionner une date limite.\n");
        } else if (deadlinePicker.getValue().isBefore(LocalDate.now())) {
            errorMessage.append("La deadline ne peut pas être dans le passé.\n");
        }

        // Vérification du projet
        if (projetCombo.getValue() == null) {
            errorMessage.append("Veuillez sélectionner un projet.\n");
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showAlert("Erreur de saisie", errorMessage.toString());
            return false;
        }
    }
    @FXML

    private void closeForm() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
