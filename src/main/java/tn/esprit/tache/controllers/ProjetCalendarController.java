package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tn.esprit.tache.entities.Projet;
 import        tn.esprit.tache.services.ProjetService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Date;

public class ProjetCalendarController {

    @FXML private GridPane calendarGrid;
    @FXML private Button addProjetBtn, editProjetBtn, deleteProjetBtn;
    @FXML private ListView<Projet> projetListView;

    private final ProjetService projetService = new ProjetService();

    @FXML
    public void initialize() {
        loadCalendar();
        loadProjetList();

        addProjetBtn.setOnAction(e -> openProjetForm(null));
        editProjetBtn.setOnAction(e -> editSelectedProjet());
        deleteProjetBtn.setOnAction(e -> deleteSelectedProjet());
    }

    private void loadCalendar() {
        List<Projet> projets = projetService.getAllProjets();
        calendarGrid.getChildren().clear();

        for (Projet projet : projets) {
            LocalDate start = convertToLocalDate(projet.getDateDebutProjet());
            LocalDate end = convertToLocalDate(projet.getDateFinProjet());

            if (start == null) {
                start = LocalDate.now(); // Default to today if null
            }

            int column = start.getDayOfWeek().getValue() % 7; // Adjusted to ensure Sunday is 0
            int row = (start.getDayOfMonth() - 1) / 7; // Approximate week number

            Button projetBtn = new Button(projet.getNomProjet());
            projetBtn.setStyle("-fx-padding: 10; -fx-font-size: 14px;" + getColorByStatus(projet.getStatutProjet()));
            projetBtn.setOnAction(e -> openProjetForm(projet));

            calendarGrid.add(projetBtn, column, row);
        }
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void loadProjetList() {
        projetListView.getItems().clear();
        projetListView.getItems().addAll(projetService.getAllProjets());
    }

    private void openProjetForm(Projet projet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/projet_form.fxml"));
            Parent root = loader.load();
            ProjetFormController controller = loader.getController();
            controller.initData(projet, projetService);

            Stage stage = new Stage();
            stage.setTitle(projet == null ? "Ajouter Projet" : "Modifier Projet");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadCalendar();
            loadProjetList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editSelectedProjet() {
        Projet selectedProjet = projetListView.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            openProjetForm(selectedProjet);
        } else {
            showAlert("Aucun projet sélectionné", "Veuillez sélectionner un projet à modifier.");
        }
    }

    private void deleteSelectedProjet() {
        Projet selectedProjet = projetListView.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            projetService.supprimerProjet(selectedProjet.getIdProjet());
            loadCalendar();
            loadProjetList();
        } else {
            showAlert("Aucun projet sélectionné", "Veuillez sélectionner un projet à supprimer.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String getColorByStatus(String status) {
        switch (status.toLowerCase()) {
            case "completed": return "-fx-background-color: #28A745; -fx-text-fill: white;";
            case "in progress": return "-fx-background-color: #FFC107; -fx-text-fill: black;";
            case "delayed": return "-fx-background-color: #DC3545; -fx-text-fill: white;";
            default: return "-fx-background-color: #6C757D; -fx-text-fill: white;";
        }
    }
}
