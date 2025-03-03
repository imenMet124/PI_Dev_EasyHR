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
import tn.esprit.Offres.entities.User;
import tn.esprit.Offres.services.ServiceCandidat;
import tn.esprit.Offres.services.ServiceUser;

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
    private final ServiceUser serviceUser = new ServiceUser();

    public enum Disponibilite {
        IMMEDIATE, UN_MOIS, DEUX_MOIS, TROIS_MOIS
    }

    @FXML
    public void initialize() {
        dispC.setItems(FXCollections.observableArrayList(Disponibilite.values()));
        dispC.getSelectionModel().select(0);
        confirmerC.setOnAction(event -> onAjouterCandidatClick());
    }

    public void setNomC(String nom) {
        this.nomC.setText(nom);
    }

    User userConnecte = new User(
            1,
            "Dupont",
            "dupont@example.com",
            "12345678",
            "Employé",
            "Développeur",
            3000.0,
            new java.util.Date(),
            "Actif",
            "Informatique"
    );

    public void remplirInformationsCandidat(User user) {
        nomC.setText(user.getNomEmp());
        emailC.setText(user.getEmail());
        depC.setText(user.getDepartment());
        telC.setText(user.getPhone());
        posteC.setText(user.getPosition());
    }

    @FXML
    private void onAjouterCandidatClick() {
        try {
            Candidat candidat = new Candidat();
            candidat.setUser(userConnecte);
            candidat.setNom(nomC.getText());
            candidat.setEmail(emailC.getText());
            candidat.setPhone(telC.getText());
            candidat.setPosition(posteC.getText());
            candidat.setDepartment(depC.getText());
            candidat.setExperienceInterne(expC.getText());
            candidat.setCompetence(compC.getText());
            candidat.setDisponibilite(tn.esprit.Offres.entities.Candidat.Disponibilite.valueOf(dispC.getValue().name()));
            if (userConnecte == null) {
                System.out.println("Erreur : Aucun utilisateur connecté !");
                return;
            }
            serviceCandidat.ajouter(candidat);
            System.out.println("Candidat ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void handleUploadExperienceInterne(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                String savedFilePath = saveFile(selectedFile, "experienceInterne");
                expC.setText(savedFilePath); // ✅ Update TextField with file path
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Fichier téléchargé avec succès !");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du téléchargement du fichier : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun fichier sélectionné", "Veuillez sélectionner un fichier.");
        }
    }


    @FXML
    private void handleUploadCompetence(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez un fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                String savedFilePath = saveFile(selectedFile, "competence");
                compC.setText(savedFilePath); // ✅ Update TextField with file path
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Fichier téléchargé avec succès !");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du téléchargement du fichier : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun fichier sélectionné", "Veuillez sélectionner un fichier.");
        }
    }


    private void uploadFile(String fileType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                saveFile(selectedFile, fileType);
                showAlert(Alert.AlertType.INFORMATION, "Success", "File uploaded successfully!");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "File upload failed: " + e.getMessage());
            }
        }
    }

    private String saveFile(File file, String fileType) throws IOException {
        Path destinationPath = Paths.get("uploads/" + fileType + "/" + file.getName());
        Files.createDirectories(destinationPath.getParent());
        Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return destinationPath.toAbsolutePath().toString(); // ✅ Return full file path
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
            Candidat candidat = new Candidat();
            candidat.setUser(userConnecte);
            candidat.setNom(nomC.getText());
            candidat.setEmail(emailC.getText());
            candidat.setPhone(telC.getText());
            candidat.setPosition(posteC.getText());
            candidat.setDepartment(depC.getText());
            candidat.setExperienceInterne(expC.getText());
            candidat.setCompetence(compC.getText());

            // Vérification et correction de la disponibilité
            if (dispC.getValue() == null) {
                System.out.println("⚠ Avertissement : Disponibilité non sélectionnée, valeur par défaut appliquée.");
                candidat.setDisponibilite(Candidat.Disponibilite.IMMEDIATE); // Valeur par défaut
            } else {
                candidat.setDisponibilite(Candidat.Disponibilite.valueOf(dispC.getValue().name()));
            }

            System.out.println("📌 Disponibilité du candidat avant insertion : " + candidat.getDisponibilite());

            serviceCandidat.ajouter(candidat);

            System.out.println("✅ Candidat ajouté avec ID: " + candidat.getIdCandidat());
            return candidat.getIdCandidat();

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de l'ajout du candidat : " + e.getMessage());
            return -1;
        }
    }


}