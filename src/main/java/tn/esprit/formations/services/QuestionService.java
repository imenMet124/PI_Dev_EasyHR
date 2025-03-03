package tn.esprit.formations.services;

import tn.esprit.formations.entities.Option;
import tn.esprit.formations.entities.Question;
import tn.esprit.formations.entities.Quiz;
import tn.esprit.formations.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {

    private final Connection connection;

    public QuestionService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Question question) throws SQLException {
        String query = "INSERT INTO question (text, quiz_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, question.getText());
            stmt.setInt(2, question.getQuiz().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    question.setId(rs.getInt(1));
                }
            }
        }

        // Save options
        if (question.getOptions() != null) {
            for (Option option : question.getOptions()) {
                String optionQuery = "INSERT INTO option (text, is_correct, question_id) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(optionQuery, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, option.getText());
                    stmt.setBoolean(2, option.isCorrect());
                    stmt.setInt(3, question.getId());
                    stmt.executeUpdate();

                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            option.setId(rs.getInt(1));
                        }
                    }
                }
            }
        }
    }

    public void modifier(Question question) throws SQLException {
        String query = "UPDATE question SET text = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, question.getText());
            stmt.setInt(2, question.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No question found with ID: " + question.getId());
            }
        }

        // Delete existing options
        String deleteOptionsQuery = "DELETE FROM option WHERE question_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteOptionsQuery)) {
            stmt.setInt(1, question.getId());
            stmt.executeUpdate();
        }

        // Save new options
        if (question.getOptions() != null) {
            for (Option option : question.getOptions()) {
                String optionQuery = "INSERT INTO option (text, is_correct, question_id) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(optionQuery, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, option.getText());
                    stmt.setBoolean(2, option.isCorrect());
                    stmt.setInt(3, question.getId());
                    stmt.executeUpdate();

                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            option.setId(rs.getInt(1));
                        }
                    }
                }
            }
        }
    }

    public void supprimer(int id) throws SQLException {
        // Delete associated options first
        String deleteOptionsQuery = "DELETE FROM option WHERE question_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteOptionsQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Delete the question
        String query = "DELETE FROM question WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No question found with ID: " + id);
            }
        }
    }

    public List<Question> getQuestionsForQuiz(int quizId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM question WHERE quiz_id = ? ORDER BY id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Question question = new Question(
                            rs.getInt("id"),
                            rs.getString("text"),
                            new ArrayList<>(),
                            null // Quiz will be set by the caller
                    );
                    // Load options for this question
                    List<Option> options = getOptionsForQuestion(question.getId());
                    question.setOptions(options);
                    questions.add(question);
                }
            }
        }
        return questions;
    }

    private List<Option> getOptionsForQuestion(int questionId) throws SQLException {
        List<Option> options = new ArrayList<>();
        String query = "SELECT * FROM option WHERE question_id = ? ORDER BY id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Option option = new Option(
                            rs.getInt("id"),
                            rs.getString("text"),
                            rs.getBoolean("is_correct")
                    );
                    options.add(option);
                }
            }
        }
        return options;
    }
}