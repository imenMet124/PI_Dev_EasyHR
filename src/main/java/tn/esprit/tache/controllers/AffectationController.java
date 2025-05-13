package tn.esprit.tache.controllers;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.model.UserSession;
import tn.esprit.tache.entities.Affectation;
import tn.esprit.tache.entities.Tache;
import tn.esprit.tache.services.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class AffectationController {

    @FXML private TableView<Affectation> affectationTable;
    @FXML private TableColumn<Affectation, String> employeCol;
    @FXML private TableColumn<Affectation, String> tacheCol;
    @FXML private TableColumn<Affectation, String> deadlineCol;
    @FXML private TableColumn<Affectation, Date> dateCol;
    @FXML private TableColumn<Affectation, String> statutCol; // ✅ Ajout de la colonne statut
    @FXML private Button ajouterBtn;
    @FXML private Button retirerBtn;
    @FXML private Button terminerTacheBtn;
    @FXML private TableColumn<Affectation, String> projetCol; // ✅ Add this column

    private final AffectationService affectationService = new AffectationService();
    private final ServiceUsers employeService = new ServiceUsers();
    private final TacheService tacheService = new TacheService();

    private final ProjetService projetService = new ProjetService();
    User loggedInUser = UserSession.getInstance().getLoggedInUser();

    @FXML
    private TextField searchField;

    @FXML
    public void initialize() {
        boolean isEmployee; // You can replace this with an actual check based on your app's user session.
        if (loggedInUser != null && (loggedInUser.getIyedRoleUser() == UserRole.RESPONSABLE_RH ||
                loggedInUser.getIyedRoleUser() == UserRole.CHEF_PROJET)) { isEmployee = false; }
        else { isEmployee = true; }

        if (isEmployee) {
            ajouterBtn.setVisible(false);
            retirerBtn.setVisible(false);
        }else {
            // If not an employee, ensure the buttons are visible
            ajouterBtn.setVisible(true);
            retirerBtn.setVisible(true);
        }




        loadAffectations();


        employeCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(employeService.getById(cellData.getValue().getIdEmp()).getIyedNomUser())
        );

        projetCol.setCellValueFactory(cellData -> {
            Tache tache = tacheService.getTacheById(cellData.getValue().getIdTache());
            return new SimpleStringProperty(tache != null && tache.getProjet() != null ? tache.getProjet().getNomProjet() : "Aucun projet");
        });

        tacheCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(tacheService.getTacheById(cellData.getValue().getIdTache()).getTitreTache())
        );

        dateCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDateAffectation() != null) {
                return new SimpleObjectProperty<>(new java.sql.Date(cellData.getValue().getDateAffectation().getTime()));
            } else {
                return new SimpleObjectProperty<>(null); // Or use a default date
            }
        });


        // ✅ Affichage formaté de la deadline
        deadlineCol.setCellValueFactory(cellData -> {
            Tache tache = tacheService.getTacheById(cellData.getValue().getIdTache());
            if (tache != null && tache.getDeadline() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                return new SimpleObjectProperty<>(dateFormat.format(tache.getDeadline()));
            } else {
                return new SimpleObjectProperty<>("Pas de deadline");
            }
        });

        // ✅ Statut : "En cours" ou "Terminé" avec couleur
        statutCol.setCellValueFactory(cellData -> {
            Tache tache = tacheService.getTacheById(cellData.getValue().getIdTache());
            return new SimpleStringProperty(tache != null && "Terminé".equals(tache.getStatutTache()) ? "Terminé" : "En cours");
        });

        statutCol.setCellFactory(column -> new TableCell<Affectation, String>() {
            @Override
            protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(statut);
                    if ("Terminé".equals(statut)) {
                        setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }




    private void loadAffectations() {
        List<Affectation> affectations = affectationService.getAllAffectations();
        ObservableList<Affectation> observableList = FXCollections.observableArrayList(affectations);
        affectationTable.setItems(observableList);
    }

    @FXML
    private void generatePdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("affectations.pdf");

        File file = fileChooser.showSaveDialog(null);
        if (file == null || file.getAbsolutePath().trim().isEmpty()) {
            showAlert("Erreur", "Aucun fichier sélectionné.", Alert.AlertType.ERROR);
            return;
        }

        try {
            affectationService.genererPdf(file.getAbsolutePath());
            showAlert("Succès", "Le PDF a été généré avec succès.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de générer le PDF : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace(); // Check for NullPointerException here
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void ouvrirFormulaireAffectation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/affectation_form.fxml"));
            Parent root = loader.load();

            AffectationFormController formController = loader.getController();
            formController.initData(affectationService, employeService, tacheService , projetService);

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

    @FXML
    private void terminerTache() {
        Affectation selectedAffectation = affectationTable.getSelectionModel().getSelectedItem();
        if (selectedAffectation == null) {
            showAlert("Erreur", "Veuillez sélectionner une affectation.");
            return;
        }

        Tache selectedTache = tacheService.getTacheById(selectedAffectation.getIdTache());
        if (selectedTache == null) {
            showAlert("Erreur", "Tâche introuvable.");
            return;
        }

        // ✅ Vérifier si la tâche est déjà terminée
        if ("Terminé".equals(selectedTache.getStatutTache())) {
            showAlert("Information", "Cette tâche est déjà terminée.");
            return;
        }

        // ✅ Mettre à jour la tâche comme terminée
        tacheService.terminerTache(selectedTache.getIdTache());
        selectedTache.setStatutTache("Terminé");
        loadAffectations();  // ✅ Recharger les données après modification
        showAlert("Succès", "La tâche a été marquée comme terminée.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void rechercherAffectation() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            loadAffectations();
            return;
        }

        List<Affectation> allAffectations = affectationService.getAllAffectations();
        ObservableList<Affectation> filteredList = FXCollections.observableArrayList();

        for (Affectation affectation : allAffectations) {
            String employeName = employeService.getById(affectation.getIdEmp()).getIyedNomUser().toLowerCase();
            String tacheTitle = tacheService.getTacheById(affectation.getIdTache()).getTitreTache().toLowerCase();

            if (employeName.contains(searchText) || tacheTitle.contains(searchText)) {
                filteredList.add(affectation);
            }
        }

        affectationTable.setItems(filteredList);
    }


}
