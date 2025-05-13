package tn.esprit.tache.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TodoistService {

    private static final String CLIENT_ID = "935b272bb3f04031a1e54a0731baa622";
    private static final String CLIENT_SECRET = "a6dd8e9791b040d3b3b963e01a53a268";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";
    private static final String TOKEN_URL = "https://api.todoist.com/oauth/access_token";
    private static final String TASKS_URL = "https://api.todoist.com/rest/v2/tasks";

    // Method to get OAuth URL
    public static String getOAuthUrl() {
        String scope = "task:add,data:read_write";
        return "https://todoist.com/oauth/authorize?client_id=" + CLIENT_ID + "&scope=" + scope + "&redirect_uri=" + REDIRECT_URI;
    }

    // Exchange authorization code for access token
    public static String exchangeAuthCodeForToken(String authorizationCode) {
        try {
            String payload = "client_id=" + CLIENT_ID +
                    "&client_secret=" + CLIENT_SECRET +
                    "&code=" + authorizationCode +
                    "&redirect_uri=" + REDIRECT_URI;

            URL url = new URL(TOKEN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    errorResponse.append(line);
                }
                System.err.println("Error response: " + errorResponse.toString());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            String accessToken = jsonResponse.getString("access_token");
            System.out.println("Access Token: " + accessToken);
            return accessToken;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve tasks from Todoist
    public static List<String> getTasks(String accessToken) {
        List<String> tasks = new ArrayList<>();
        if (accessToken == null || accessToken.isEmpty()) {
            System.err.println("Invalid Access Token!");
            return tasks;
        }

        try {
            URL url = new URL(TASKS_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Error fetching tasks. Response Code: " + responseCode);
                return tasks;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONArray jsonResponse = new JSONArray(response.toString());
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject task = jsonResponse.getJSONObject(i);
                String taskContent = task.getString("content");
                tasks.add(taskContent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Create a task in Todoist
    public static boolean createTask(String accessToken, String content, String dueDate) {
        if (accessToken == null || accessToken.isEmpty() || content.isEmpty()) {
            System.err.println("Invalid request parameters!");
            return false;
        }

        try {
            URL url = new URL("https://api.todoist.com/rest/v2/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Use JSONObject for safer JSON creation
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("content", content);
            if (dueDate != null && !dueDate.isEmpty()) {
                jsonBody.put("due_date", dueDate);
            }

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            // If the response is an error, print it for debugging
            if (responseCode >= 400) {
                InputStream errorStream = connection.getErrorStream();
                if (errorStream != null) {
                    String errorResponse = new BufferedReader(new InputStreamReader(errorStream))
                            .lines().collect(Collectors.joining("\n"));
                    System.err.println("Error Response: " + errorResponse);
                }
            }

            return responseCode >= 200 && responseCode < 300;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static List<Map<String, String>> getTasksWithDueDates(String accessToken) {
        List<Map<String, String>> tasksWithDueDates = new ArrayList<>();

        if (accessToken == null || accessToken.isEmpty()) {
            System.err.println("Invalid Access Token!");
            return tasksWithDueDates;
        }

        try {
            URL url = new URL("https://api.todoist.com/rest/v2/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Error fetching tasks. Response Code: " + responseCode);
                return tasksWithDueDates;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONArray jsonResponse = new JSONArray(response.toString());
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject task = jsonResponse.getJSONObject(i);
                Map<String, String> taskData = new HashMap<>();
                taskData.put("content", task.getString("content"));

                // Handle "due" field safely
                if (task.has("due") && !task.isNull("due")) {
                    Object dueField = task.get("due");

                    if (dueField instanceof JSONObject) {
                        JSONObject dueObject = (JSONObject) dueField;
                        taskData.put("due_date", dueObject.optString("date", "No due date"));
                    } else {
                        taskData.put("due_date", "Invalid due format");
                    }
                } else {
                    taskData.put("due_date", "No due date");
                }

                tasksWithDueDates.add(taskData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasksWithDueDates;
    }



    public static boolean completeTask(String accessToken, String taskContent) {
        if (accessToken == null || accessToken.isEmpty() || taskContent == null) {
            System.err.println("Invalid request parameters!");
            return false;
        }

        try {
            // Retrieve task ID from its content
            String taskId = getTaskIdByContent(accessToken, taskContent);
            if (taskId == null) {
                System.err.println("Task not found!");
                return false;
            }

            // API endpoint to close (complete) a task
            URL url = new URL(TASKS_URL + "/" + taskId + "/close");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // Delete a task in Todoist
    public static boolean deleteTask(String accessToken, String taskContent) {
        if (accessToken == null || accessToken.isEmpty() || taskContent == null) {
            System.err.println("Invalid request parameters!");
            return false;
        }

        try {
            // Retrieve task ID from its content
            String taskId = getTaskIdByContent(accessToken, taskContent);
            if (taskId == null) {
                System.err.println("Task not found!");
                return false;
            }

            System.out.println("Deleting task with ID: " + taskId);

            // Construct API URL
            URL url = new URL(TASKS_URL + "/" + taskId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode >= 400) {
                // Print API error response
                InputStream errorStream = connection.getErrorStream();
                if (errorStream != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        System.err.println("Error Response: " + response.toString());
                    }
                }
            }

            return responseCode >= 200 && responseCode < 300;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // Helper method to get task ID by task content
    private static String getTaskIdByContent(String accessToken, String content) {
        try {
            URL url = new URL(TASKS_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONArray jsonResponse = new JSONArray(response.toString());

            // Debug: Print all fetched tasks
            System.out.println("Fetched Tasks:");
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject task = jsonResponse.getJSONObject(i);
                System.out.println("Task ID: " + task.getString("id") + ", Content: " + task.getString("content"));
            }

            // Now try to find the correct task
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject task = jsonResponse.getJSONObject(i);
                if (task.getString("content").trim().equalsIgnoreCase(content.trim())) {
                    return task.getString("id");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
