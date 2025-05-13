package tn.esprit.tache.services;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.Desktop;
import java.net.URI;

public class TodoistOAuthApp extends Application {

    private String accessToken;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        Button authButton = new Button("Authorize with Todoist");
        statusLabel = new Label("Status: Not authorized");

        authButton.setOnAction(e -> startOAuthProcess());

        VBox root = new VBox(10);
        root.getChildren().addAll(authButton, statusLabel);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Todoist OAuth");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startOAuthProcess() {
        String authUrl = TodoistService.getOAuthUrl();
        OAuthRedirectHandler server = new OAuthRedirectHandler();

        server.setAuthCallback(this::handleAuthorizationCode);
        server.startServer();

        try {
            Desktop.getDesktop().browse(new URI(authUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAuthorizationCode(String authorizationCode) {
        new Thread(() -> {
            String token = TodoistService.exchangeAuthCodeForToken(authorizationCode);
            Platform.runLater(() -> {
                if (token != null) {
                    this.accessToken = token;
                    statusLabel.setText("Status: Authorized ✅");
                    System.out.println("Access Token: " + token);
                } else {
                    statusLabel.setText("Authorization failed ❌");
                }
            });
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
