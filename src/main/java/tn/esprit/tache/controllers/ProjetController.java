package tn.esprit.tache.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.stream.Collectors;

public class ProjetController {

    @FXML private TableView<Projet> projetTable;
    @FXML private TableColumn<Projet, String> nomCol;
    @FXML private TableColumn<Projet, String> descCol;
    @FXML private TableColumn<Projet, String> statutCol;
    @FXML private TableColumn<Projet, Date> debutCol;
    @FXML private TableColumn<Projet, Date> finCol;

    @FXML private TextField searchField;
    @FXML private Button addProjetBtn, editProjetBtn, deleteProjetBtn;

    private final ProjetService projetService = new ProjetService();
    private ObservableList<Projet> projetList;

    @FXML
    public void initialize() {
        // ✅ Bind TableView columns to Projet attributes
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomProjet()));
        descCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescProjet()));
        statutCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatutProjet()));
        debutCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateDebutProjet()));
        finCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateFinProjet()));

        // ✅ Charger les projets
        loadProjets();

        // ✅ Appliquer la recherche en temps réel
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterProjets(newValue));
    }

    private void loadProjets() {
        List<Projet> projets = projetService.getAllProjets();
        projetList = FXCollections.observableArrayList(projets);
        projetTable.setItems(projetList);
    }

    // ✅ Filtrer les projets selon la recherche
    private void filterProjets(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            projetTable.setItems(projetList); // Si vide, afficher tout
        } else {
            String lowerKeyword = keyword.toLowerCase();
            List<Projet> filteredList = projetList.stream()
                    .filter(projet -> projet.getNomProjet().toLowerCase().contains(lowerKeyword) ||
                            projet.getDescProjet().toLowerCase().contains(lowerKeyword) ||
                            projet.getStatutProjet().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
            projetTable.setItems(FXCollections.observableArrayList(filteredList));
        }
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

            // ✅ Rafraîchir après ajout/modification
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
