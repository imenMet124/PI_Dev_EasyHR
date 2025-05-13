package tn.esprit.tache.services;

import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DeepSeekService {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = "sk-8e90da0e83c247f48ab1ca253ed638a2"; // Replace with your actual key

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS)     // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS)    // Increase write timeout
            .build();

    public static String sendMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON request body (matches Postman format)
            String json = "{"
                    + "\"model\": \"deepseek-chat\","
                    + "\"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}],"
                    + "\"max_tokens\": 10"
                    + "}";

            RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(API_URL)
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            System.out.println("🔹 API Response: " + responseBody); // Debugging log

            // Parse JSON response
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            return jsonResponse.get("choices").get(0).get("message").get("content").asText();

        } catch (IOException e) {
            e.printStackTrace();
            return "❌ Error: Unable to contact DeepSeek API.";
        }
    }
}
