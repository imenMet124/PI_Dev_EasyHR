package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.tache.services.DeepSeekService;

public class ChatBotController {
    @FXML private TextArea chatArea;
    @FXML private TextField userInput;

    public void initialize() {
        chatArea.appendText("🤖 DeepSeek Assistant: Hello! How can I assist you with your tasks today?\n\n");
    }

    @FXML
    private void sendMessage() {
        String userMessage = userInput.getText().trim();
        if (!userMessage.isEmpty()) {
            chatArea.appendText("👤 You: " + userMessage + "\n");

            new Thread(() -> {
                try {
                    String botResponse = DeepSeekService.sendMessage("You are a helpful assistant specialized in managing tasks. Guide the user based on their tasks. Here is their message: " + userMessage);
                    chatArea.appendText("🤖 DeepSeek Assistant: " + botResponse + "\n\n");
                } catch (Exception e) {
                    chatArea.appendText("❌ Error: Unable to contact DeepSeek API.\n");
                }
            }).start();

            userInput.clear();
        }
    }
}
