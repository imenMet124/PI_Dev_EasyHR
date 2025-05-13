package tn.esprit.tache.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;
import tn.esprit.tache.services.QuizService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizController {

    @FXML
    private Text questionText;
    @FXML
    private VBox optionsBox;
    @FXML
    private Label resultLabel;

    private String correctAnswer;

    @FXML
    public void initialize() {
        loadNewQuestion();
    }

    public void loadNewQuestion() {
        resultLabel.setText(""); // Clear previous result
        optionsBox.getChildren().clear(); // Clear previous buttons

        try {
            String jsonResponse = QuizService.getQuizQuestion();
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray results = json.getJSONArray("results");

            if (results.length() > 0) {
                JSONObject questionObj = results.getJSONObject(0);
                String question = questionObj.getString("question");
                correctAnswer = questionObj.getString("correct_answer");

                // Decode special characters
                question = org.apache.commons.text.StringEscapeUtils.unescapeHtml4(question);
                correctAnswer = org.apache.commons.text.StringEscapeUtils.unescapeHtml4(correctAnswer);

                questionText.setText(question);

                // Create answer buttons
                List<String> choices = new ArrayList<>();
                JSONArray incorrectAnswers = questionObj.getJSONArray("incorrect_answers");
                for (int i = 0; i < incorrectAnswers.length(); i++) {
                    choices.add(org.apache.commons.text.StringEscapeUtils.unescapeHtml4(incorrectAnswers.getString(i)));
                }
                choices.add(correctAnswer);
                Collections.shuffle(choices);

                for (String choice : choices) {
                    Button btn = new Button(choice);
                    btn.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
                    btn.setOnAction(event -> checkAnswer(choice, btn));
                    optionsBox.getChildren().add(btn);
                }

                // Auto next question in 5 seconds
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(event -> loadNewQuestion());
                pause.play();
            }
        } catch (Exception e) {
            questionText.setText("Failed to load question!");
            e.printStackTrace();
        }
    }



    private void checkAnswer(String selectedAnswer, Button btn) {
        if (selectedAnswer.equals(correctAnswer)) {
            resultLabel.setText("✅ Correct!");
            resultLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        } else {
            resultLabel.setText("❌ Wrong! Correct answer: " + correctAnswer);
            resultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            btn.setStyle("-fx-background-color: #FF5733; -fx-text-fill: white;");
        }

        // Wait 2 seconds then go to next question
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> loadNewQuestion());
        pause.play();
    }
}
