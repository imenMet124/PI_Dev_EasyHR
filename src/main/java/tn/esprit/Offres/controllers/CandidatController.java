package tn.esprit.Offres.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.Offres.entities.Candidat;
import tn.esprit.Users.entities.User;
import tn.esprit.Offres.services.ServiceCandidat;
import tn.esprit.Users.model.UserSession;
import tn.esprit.Users.services.ServiceUsers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class CandidatController {

    @FXML
    private TextField emailC;

    @FXML
    private Button confirmerC;

    @FXML
    private TextField compC;

    @FXML
    private ComboBox<Disponibilite> dispC;

    @FXML
    private TextField nomC;

    @FXML
    private TextField expC;

    @FXML
    private TextField depC;

    @FXML
    private TextField telC;

    @FXML
    private TextField posteC;

    private final ServiceCandidat serviceCandidat = new ServiceCandidat();
    private final ServiceUsers serviceUser = new ServiceUsers();

    private User loggedInUser;

    @FXML
    public void initialize() {
        dispC.setItems(FXCollections.observableArrayList(Disponibilite.values()));
        dispC.getSelectionModel().select(0);
        confirmerC.setOnAction(event -> onAjouterCandidatClick());

        // Retrieve the logged-in user
        loggedInUser = UserSession.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            remplirInformationsCandidat(loggedInUser);
        } else {
            System.out.println("⚠ Aucun utilisateur connecté !");
        }
    }

    public void remplirInformationsCandidat(User user) {
        nomC.setText(user.getIyedNomUser());
        emailC.setText(user.getIyedEmailUser());
        telC.setText(user.getIyedPhoneUser());
        posteC.setText(user.getIyedPositionUser());
        depC.setText(user.getIyedDepartment() != null ? user.getIyedDepartment().getIyedNomDep() : "No Department");
    }

    @FXML
    private void onAjouterCandidatClick() {
        try {
            if (loggedInUser == null) {
                System.out.println("❌ Erreur : Aucun utilisateur connecté !");
                return;
            }

            Candidat candidat = new Candidat();
            candidat.setUser(loggedInUser);
            candidat.setNom(nomC.getText());
            candidat.setEmail(emailC.getText());
            candidat.setPhone(telC.getText());
            candidat.setPosition(posteC.getText());
            candidat.setDepartment(depC.getText());
            candidat.setExperienceInterne(expC.getText());
            candidat.setCompetence(compC.getText());
            candidat.setDisponibilite(Disponibilite.valueOf(dispC.getValue().name()));

            serviceCandidat.ajouter(candidat);
            System.out.println("✅ Candidat ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du candidat : " + e.getMessage());
        }
    }
    public enum Disponibilite {
        IMMEDIATE, UN_MOIS, DEUX_MOIS, TROIS_MOIS
    }
    @FXML
    private void handleUploadExperienceInterne(ActionEvent event) {
        handleFileUpload(event, "experienceInterne", expC);
    }

    @FXML
    private void handleUploadCompetence(ActionEvent event) {
        handleFileUpload(event, "competence", compC);
    }

    private void handleFileUpload(ActionEvent event, String fileType, TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                String savedFilePath = saveFile(selectedFile, fileType);
                textField.setText(savedFilePath);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Fichier téléchargé avec succès !");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du téléchargement du fichier : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun fichier sélectionné", "Veuillez sélectionner un fichier.");
        }
    }

    private String saveFile(File file, String fileType) throws IOException {
        Path destinationPath = Paths.get("uploads/" + fileType + "/" + file.getName());
        Files.createDirectories(destinationPath.getParent());
        Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return destinationPath.toAbsolutePath().toString();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public int ajouterCandidatEtRetournerId() {
        try {
            if (loggedInUser == null) {
                System.out.println("❌ Erreur : Aucun utilisateur connecté !");
                return -1;
            }

            Candidat candidat = new Candidat();
            candidat.setUser(loggedInUser);
            candidat.setNom(nomC.getText());
            candidat.setEmail(emailC.getText());
            candidat.setPhone(telC.getText());
            candidat.setPosition(posteC.getText());
            candidat.setDepartment(depC.getText());
            candidat.setExperienceInterne(expC.getText());
            candidat.setCompetence(compC.getText());

            if (dispC.getValue() == null) {
                System.out.println("⚠ Avertissement : Disponibilité non sélectionnée, valeur par défaut appliquée.");
                candidat.setDisponibilite(Candidat.Disponibilite.IMMEDIATE);
            } else {
                candidat.setDisponibilite(dispC.getValue());
            }

            serviceCandidat.ajouter(candidat);

            System.out.println("✅ Candidat ajouté avec ID: " + candidat.getIdCandidat());
            return candidat.getIdCandidat();

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de l'ajout du candidat : " + e.getMessage());
            return -1;
        }
    }


    public void setNomC(String nom) {
        nomC.setText(nom);
    }
}
