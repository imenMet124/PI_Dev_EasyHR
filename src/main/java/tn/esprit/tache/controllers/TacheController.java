package tn.esprit.tache.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.tache.entities.Tache;
import tn.esprit.tache.entities.Projet;
import tn.esprit.tache.services.TacheService;
import tn.esprit.tache.services.ProjetService;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class TacheController {

    @FXML private TableView<Tache> tacheTable;
    @FXML private TableColumn<Tache, Integer> idCol;
    @FXML private TableColumn<Tache, String> titreCol;
    @FXML private TableColumn<Tache, String> statutCol;
    @FXML private TableColumn<Tache, String> projetCol;
    @FXML private TableColumn<Tache, Date> deadlineCol;
    @FXML private Button addTacheBtn, editTacheBtn, deleteTacheBtn;

    private final TacheService tacheService = new TacheService();
    private final ProjetService projetService = new ProjetService();
    private ObservableList<Tache> tacheList;

    @FXML
    public void initialize() {
        // ✅ Liaison des colonnes avec les données de l'entité Tache
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getIdTache()));
        titreCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitreTache()));
        statutCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatutTache()));
        projetCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProjet().getNomProjet()));
        deadlineCol.setCellValueFactory(cellData -> {
            java.util.Date deadline = cellData.getValue().getDeadline();
            return new SimpleObjectProperty<>(new java.sql.Date(deadline.getTime()));
        });
        // ✅ Charger les tâches
        loadTaches();
    }

    private void loadTaches() {
        List<Tache> taches = tacheService.getAllTaches();
        tacheList = FXCollections.observableArrayList(taches);
        tacheTable.setItems(tacheList);
    }

    @FXML
    private void ajouterTache() {
        openTaskForm(null);
    }

    @FXML
    private void modifierTache() {
        Tache selectedTache = tacheTable.getSelectionModel().getSelectedItem();
        if (selectedTache != null) {
            openTaskForm(selectedTache);
        } else {
            showAlert("Erreur", "Veuillez sélectionner une tâche.");
        }
    }

    @FXML
    private void supprimerTache() {
        Tache selectedTache = tacheTable.getSelectionModel().getSelectedItem();
        if (selectedTache != null) {
            tacheService.supprimerTache(selectedTache.getIdTache());
            tacheList.remove(selectedTache);
        } else {
            showAlert("Erreur", "Veuillez sélectionner une tâche à supprimer.");
        }
    }

    private void openTaskForm(Tache tache) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tache_form.fxml"));
            Parent root = loader.load();
            TacheFormController controller = loader.getController();
            controller.initData(tache, tacheService, projetService);

            Stage stage = new Stage();
            stage.setTitle(tache == null ? "Ajouter Tâche" : "Modifier Tâche");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // ✅ Rafraîchir la table après modification
            loadTaches();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
