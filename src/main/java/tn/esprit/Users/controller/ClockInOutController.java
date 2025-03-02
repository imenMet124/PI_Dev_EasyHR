package tn.esprit.Users.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import org.json.JSONObject;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.Users.services.UserPhotoService;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.utils.KairosAPI;
import tn.esprit.Users.model.UserSession;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import java.awt.Dimension;

public class ClockInOutController {

    @FXML
    private ImageView webcamView;

    @FXML
    private ComboBox<String> clockActionComboBox;

    @FXML
    private Label statusLabel;

    private Webcam webcam;
    private File capturedImageFile;

    private final ServiceUsers serviceUsers = new ServiceUsers();
    private final UserPhotoService userPhotoService = new UserPhotoService();

    @FXML
    private void initialize() {
        // Initialize the webcam
        startWebcam();

        // Initialize the ComboBox with options
        clockActionComboBox.getItems().addAll("Clock In", "Clock Out");
        clockActionComboBox.setValue("Clock In"); // Set default value
    }



    private void startWebcam() {
        webcam = Webcam.getDefault();

        if (webcam != null) {
            // Set resolution to at least 640x480 or higher
            Dimension bestResolution = new Dimension(640, 480);
            webcam.setViewSize(bestResolution);

            webcam.open();

            WebcamPanel webcamPanel = new WebcamPanel(webcam);
            webcamPanel.setFPSDisplayed(true);
            webcamPanel.setDisplayDebugInfo(true);

            SwingNode swingNode = new SwingNode();
            createSwingContent(swingNode, webcamPanel);

            VBox parent = (VBox) webcamView.getParent();
            parent.getChildren().remove(webcamView); // Remove the ImageView
            parent.getChildren().add(swingNode); // Add the SwingNode
        } else {
            System.out.println("No webcam detected.");
        }
    }


    private void createSwingContent(SwingNode swingNode, WebcamPanel webcamPanel) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(webcamPanel));
    }

    @FXML
    private void handleCaptureAndProcess() {
        if (webcam != null) {
            System.out.println("Webcam is available, starting capture...");

            // Capture the image from the webcam
            BufferedImage bufferedImage = webcam.getImage();
            if (bufferedImage != null) {
                try {
                    System.out.println("Captured image successfully.");

                    // Save the image to a file
                    capturedImageFile = new File("captured_image.jpg");
                    ImageIO.write(bufferedImage, "jpg", capturedImageFile);
                    System.out.println("Image saved to file: " + capturedImageFile.getAbsolutePath());

                    // Get the selected action from the ComboBox
                    String selectedAction = clockActionComboBox.getValue();
                    if (selectedAction == null) {
                        statusLabel.setText("Please select Clock In or Clock Out.");
                        System.out.println("No action selected.");
                        return;
                    }
                    System.out.println("Selected action: " + selectedAction);

                    // Retrieve the logged-in user's ID from the session
                    User loggedInUser = UserSession.getInstance().getLoggedInUser();
                    if (loggedInUser == null) {
                        statusLabel.setText("No user is logged in.");
                        System.out.println("No logged-in user found.");
                        return;
                    }
                    System.out.println("Logged-in user: " + loggedInUser.getIyedNomUser());

                    // Retrieve the user's photo from the database
                    String userPhotoPath = userPhotoService.getUserPhotoPath(loggedInUser.getIyedIdUser());
                    if (userPhotoPath == null) {
                        statusLabel.setText("User photo not found.");
                        System.out.println("User photo not found for user ID: " + loggedInUser.getIyedIdUser());
                        return;
                    }
                    System.out.println("User photo path retrieved: " + userPhotoPath);

                    File userPhotoFile = new File(userPhotoPath);
                    if (!userPhotoFile.exists()) {
                        statusLabel.setText("User photo file not found.");
                        System.out.println("User photo file not found at path: " + userPhotoFile.getAbsolutePath());
                        return;
                    }
                    System.out.println("User photo file exists: " + userPhotoFile.getAbsolutePath());

                    // Perform the biometric verification (comparing the selfie with the stored user photo)
                    String kairosResponse = KairosAPI.verifyBiometric(capturedImageFile, userPhotoFile);
                    System.out.println("Kairos response: " + kairosResponse);

                    // Parse the response
                    JSONObject jsonResponse = new JSONObject(kairosResponse);
                    int responseCode = jsonResponse.getInt("response_code");

                    if (responseCode == 0) {
                        // Verification successful
                        JSONObject decision = jsonResponse.getJSONObject("response_data").getJSONObject("decision");
                        double rejectScore = decision.getDouble("reject_score");
                        double reviewScore = decision.getDouble("review_score");

                        if (rejectScore == 0 && reviewScore == 0) {
                            // Proceed with clock-in/out logic
                            if (selectedAction.equals("Clock In")) {
                                loggedInUser.setIyedStatutUser(UserStatus.ACTIVE);
                                statusLabel.setText("Clocked in successfully for user: " + loggedInUser.getIyedNomUser());
                                System.out.println("User clocked in: " + loggedInUser.getIyedNomUser());
                            } else if (selectedAction.equals("Clock Out")) {
                                loggedInUser.setIyedStatutUser(UserStatus.INACTIVE);
                                statusLabel.setText("Clocked out successfully for user: " + loggedInUser.getIyedNomUser());
                                System.out.println("User clocked out: " + loggedInUser.getIyedNomUser());
                            }

                            // Save the updated user status
                            serviceUsers.modifier(loggedInUser);
                            System.out.println("User status updated in database.");

                            // Close the webcam after successful clock-in/out
                            webcam.close();
                            System.out.println("Webcam closed successfully.");
                        } else {
                            statusLabel.setText("Face verification failed. Please try again.");
                            System.out.println("Face verification failed. Reject score: " + rejectScore + ", Review score: " + reviewScore);
                        }
                    } else {
                        statusLabel.setText("Face verification failed. Please try again.");
                        System.out.println("Face verification failed. Response code: " + responseCode);
                    }
                } catch (IOException | SQLException | JSONException e) {
                    statusLabel.setText("Error capturing or processing image: " + e.getMessage());
                    e.printStackTrace(); // Log the exception for debugging
                    System.out.println("Error during image capture or processing: " + e.getMessage());
                }
            } else {
                System.out.println("Captured image is null.");
            }
        } else {
            System.out.println("Webcam is not available.");
        }
    }

}
