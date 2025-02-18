package tn.esprit.Users.test;

import javafx.application.Application;
import javafx.stage.Stage;
import tn.esprit.Users.controller.SceneController;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Set the primary stage in the SceneController
            SceneController.setPrimaryStage(primaryStage);

            // Load the initial scene, for example, the "Afficher Users" scene
            SceneController.loadMainMenuScene();

            // Show the primary stage
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
