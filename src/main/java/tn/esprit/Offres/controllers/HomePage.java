package tn.esprit.Offres.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.Offres.entities.Candidat;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.model.UserSession;


import java.io.IOException;

public class HomePage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            User loggedInUser = UserSession.getInstance().getLoggedInUser();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidat.fxml"));
            Parent root = loader.load();
            CandidatController controller = loader.getController();
            controller.remplirInformationsCandidat(loggedInUser);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
}
