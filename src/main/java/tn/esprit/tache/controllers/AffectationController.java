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
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.tache.entities.Affectation;
import tn.esprit.tache.entities.Employe;
import tn.esprit.tache.entities.Tache;
import tn.esprit.tache.services.AffectationService;
import tn.esprit.tache.services.EmployeService;
import tn.esprit.tache.services.TacheService;

import java.sql.Date;
import java.util.List;

public class AffectationController {

    @FXML private TableView<Affectation> affectationTable;
    @FXML private TableColumn<Affectation, Integer> idAffectationCol;
    @FXML private TableColumn<Affectation, String> employeCol;
    @FXML private TableColumn<Affectation, String> tacheCol;
    @FXML private TableColumn<Affectation, Date> dateCol;
    @FXML private Button ajouterBtn;
    @FXML private Button retirerBtn;

    private final AffectationService affectationService = new AffectationService();
    private final EmployeService employeService = new EmployeService();
    private final TacheService tacheService = new TacheService();

    @FXML
    public void initialize() {
        loadAffectations();

        idAffectationCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getIdAffectation())
        );

        employeCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(employeService.getEmployeById(cellData.getValue().getIdEmp()).getNomEmp())
        );

        tacheCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(tacheService.getTacheById(cellData.getValue().getIdTache()).getTitreTache())
        );

        dateCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(new java.sql.Date(cellData.getValue().getDateAffectation().getTime()))
        );

    }

        private void loadAffectations() {
        List<Affectation> affectations = affectationService.getAllAffectations();
        ObservableList<Affectation> observableList = FXCollections.observableArrayList(affectations);
        affectationTable.setItems(observableList);
    }

    @FXML
    private void ouvrirFormulaireAffectation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/affectation_form.fxml"));
            Parent root = loader.load();

            AffectationFormController formController = loader.getController();
            formController.initData(affectationService, employeService, tacheService);

            Stage stage = new Stage();
            stage.setTitle("Ajouter Affectation");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadAffectations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void retirerEmployeTache() {
        Affectation selectedAffectation = affectationTable.getSelectionModel().getSelectedItem();
        if (selectedAffectation == null) {
            showAlert("Erreur", "Veuillez sélectionner une affectation à retirer.");
            return;
        }

        affectationService.retirerEmployeTache(selectedAffectation.getIdEmp(), selectedAffectation.getIdTache());
        loadAffectations();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
