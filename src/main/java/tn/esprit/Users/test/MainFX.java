package tn.esprit.Users.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.Users.controller.SceneController;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Main Menu Scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();

            // Set the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Main Menu");

            // Set the initial size of the window
            primaryStage.setWidth(800); // Set initial width
            primaryStage.setHeight(600); // Set initial height

            // Optionally, you can make the window resizable if needed
            primaryStage.setResizable(true); // Set to false if you don't want resizing

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
