package tn.esprit.Offres.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.Offres.entities.Candidat;
import tn.esprit.Offres.entities.User;

import java.io.IOException;

public class HomePage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            User userConnecte = new User(
                    1, // idEmp
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidat.fxml"));
            Parent root = loader.load();
            CandidatController controller = loader.getController();
            controller.remplirInformationsCandidat(userConnecte);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
}
