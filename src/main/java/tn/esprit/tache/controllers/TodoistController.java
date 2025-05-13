package tn.esprit.tache.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tn.esprit.tache.services.OAuthRedirectHandler;
import tn.esprit.tache.services.TodoistService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TodoistController {

    private String accessToken;
    private final Map<String, LocalDate> taskDateMap = new HashMap<>(); // Store tasks with their dates
    private ObservableList<String> tasks = FXCollections.observableArrayList();

    @FXML private ListView<String> taskListView;
    @FXML private TextField taskContentField;
    @FXML private Button createTaskButton, deleteTaskButton, refreshTasksButton, authorizeButton;
    @FXML private DatePicker taskDatePicker, calendarView;
    @FXML private VBox calendarContainer;
    @FXML private CheckBox taskCompletionCheckBox;
    @FXML private ProgressBar taskProgressBar;

    @FXML
    private void initialize() {
        taskListView.setItems(tasks);
        calendarView.setOnAction(event -> filterTasksByDate());
    }

    @FXML
    private void startOAuthProcess() {
        OAuthRedirectHandler server = new OAuthRedirectHandler();
        server.setAuthCallback(this::handleAuthorizationCode);
        server.startServer();

        try {
            String authUrl = TodoistService.getOAuthUrl();
            java.awt.Desktop.getDesktop().browse(new java.net.URI(authUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAuthorizationCode(String authorizationCode) {
        new Thread(() -> {
            accessToken = TodoistService.exchangeAuthCodeForToken(authorizationCode);
            Platform.runLater(() -> {
                if (accessToken != null) {
                    System.out.println("Authorized! Access Token: " + accessToken);
                    loadTasks();
                } else {
                    System.err.println("Failed to retrieve access token.");
                }
            });
        }).start();
    }

    @FXML
    private void loadTasks() {
        if (accessToken == null) {
            System.err.println("Access token is null. Please authenticate first.");
            return;
        }

        new Thread(() -> {
            try {
                List<Map<String, String>> fetchedTasks = TodoistService.getTasksWithDueDates(accessToken);

                if (fetchedTasks == null) {
                    System.err.println("Failed to fetch tasks. Response was null.");
                    return;
                }

                Platform.runLater(() -> {
                    tasks.clear();
                    taskDateMap.clear();

                    for (Map<String, String> taskData : fetchedTasks) {
                        String taskName = taskData.getOrDefault("content", "Unnamed Task");
                        String dueDateString = taskData.get("due_date");

                        LocalDate dueDate = null;
                        if (dueDateString != null && !dueDateString.isEmpty()) {
                            try {
                                dueDate = LocalDate.parse(dueDateString);
                            } catch (Exception e) {
                                System.err.println("Invalid due date format: " + dueDateString);
                            }
                        }

                        // Store task with a date
                        String taskEntry = (dueDate != null) ? taskName + " - " + dueDate : taskName;
                        tasks.add(taskEntry);
                        if (dueDate != null) {
                            taskDateMap.put(taskEntry, dueDate);
                        }
                    }
                    updateProgressBar();
                });

            } catch (Exception e) {
                System.err.println("Error while fetching tasks: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }


    @FXML
    private void createTask() {
        String taskName = taskContentField.getText().trim();
        LocalDate dueDate = taskDatePicker.getValue();

        if (accessToken == null) {
            System.err.println("Please authenticate first.");
            return;
        }

        if (taskName.isEmpty()) {
            System.err.println("Task name cannot be empty.");
            return;
        }

        new Thread(() -> {
            boolean success = TodoistService.createTask(accessToken, taskName, (dueDate != null) ? dueDate.toString() : null);
            Platform.runLater(() -> {
                if (success) {
                    loadTasks();
                    taskContentField.clear();
                    taskDatePicker.setValue(null);
                } else {
                    System.err.println("Failed to create task.");
                }
            });
        }).start();
    }

    @FXML
    private void deleteSelectedTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null && selectedTask.contains(" - ")) {
            selectedTask = selectedTask.substring(0, selectedTask.lastIndexOf(" - ")); // Remove due date
        }

        if (accessToken == null || selectedTask == null) {
            System.err.println("No task selected.");
            return;
        }

        String finalSelectedTask = selectedTask;
        new Thread(() -> {
            boolean success = TodoistService.deleteTask(accessToken, finalSelectedTask);
            Platform.runLater(() -> {
                if (success) {
                    loadTasks();
                } else {
                    System.err.println("Failed to delete task.");
                }
            });
        }).start();
    }

    @FXML
    private void markTaskAsCompleted() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null && selectedTask.contains(" - ")) {
            selectedTask = selectedTask.substring(0, selectedTask.lastIndexOf(" - ")); // Remove due date
        }
        if (accessToken == null || selectedTask == null) {
            System.err.println("No task selected.");
            return;
        }

        if (taskCompletionCheckBox.isSelected()) {
            String finalSelectedTask = selectedTask;
            new Thread(() -> {
                boolean success = TodoistService.completeTask(accessToken, finalSelectedTask);
                Platform.runLater(() -> {
                    if (success) {
                        loadTasks();
                    } else {
                        System.err.println("Failed to mark task as completed.");
                    }
                });
            }).start();
        }
    }

    @FXML
    private void filterTasksByDate() {
        LocalDate selectedDate = calendarView.getValue();
        if (selectedDate == null) {
            taskListView.setItems(tasks);
            return;
        }

        List<String> filteredTasks = taskDateMap.entrySet().stream()
                .filter(entry -> selectedDate.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        taskListView.setItems(FXCollections.observableArrayList(filteredTasks));
    }

    private void updateProgressBar() {
        if (tasks.isEmpty()) {
            taskProgressBar.setProgress(0);
        } else {
            long completedTasks = tasks.stream().filter(task -> task.contains("[✔]")).count();
            double progress = (double) completedTasks / tasks.size();
            taskProgressBar.setProgress(progress);
        }
    }
}
