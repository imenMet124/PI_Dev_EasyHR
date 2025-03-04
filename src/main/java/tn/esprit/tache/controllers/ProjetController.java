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
import tn.esprit.tache.entities.Projet;
import tn.esprit.tache.services.ProjetService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ProjetController {

    @FXML private TableView<Projet> projetTable;
    @FXML private TableColumn<Projet, Integer> idCol;
    @FXML private TableColumn<Projet, String> nomCol;
    @FXML private TableColumn<Projet, String> descCol;
    @FXML private TableColumn<Projet, String> statutCol;
    @FXML private TableColumn<Projet, Date> debutCol;
    @FXML private TableColumn<Projet, Date> finCol;

    @FXML private Button addProjetBtn, editProjetBtn, deleteProjetBtn;

    private final ProjetService projetService = new ProjetService();
    private ObservableList<Projet> projetList;

    @FXML
    public void initialize() {
        // ✅ Bind TableView columns to Projet attributes
        idCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdProjet()));
        nomCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNomProjet()));
        descCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescProjet()));
        statutCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatutProjet()));
        debutCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateDebutProjet()));
        finCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateFinProjet()));

        // ✅ Load Projects into TableView
        loadProjets();
    }

    private void loadProjets() {
        List<Projet> projets = projetService.getAllProjets();
        projetList = FXCollections.observableArrayList(projets);
        projetTable.setItems(projetList);
    }

    @FXML
    private void ajouterProjet() {
        openProjetForm(null);
    }

    @FXML
    private void modifierProjet() {
        Projet selectedProjet = projetTable.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            openProjetForm(selectedProjet);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un projet.");
        }
    }

    @FXML
    private void supprimerProjet() {
        Projet selectedProjet = projetTable.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            projetService.supprimerProjet(selectedProjet.getIdProjet());
            projetList.remove(selectedProjet);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un projet à supprimer.");
        }
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

            // ✅ Refresh table after adding/editing a project
            loadProjets();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
