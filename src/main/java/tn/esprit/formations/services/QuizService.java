package tn.esprit.formations.services;

import tn.esprit.formations.entities.Question;
import tn.esprit.formations.entities.Quiz;
import tn.esprit.formations.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizService implements IService<Quiz> {

    private final Connection connection;
    private final QuestionService questionService;

    public QuizService() {
        connection = MyDatabase.getInstance().getConnection();
        questionService = new QuestionService();
    }

    @Override
    public void ajouter(Quiz quiz) throws SQLException {
        String query = "INSERT INTO quiz (title) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, quiz.getTitle());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    quiz.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void modifier(Quiz quiz) throws SQLException {
        String query = "UPDATE quiz SET title = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, quiz.getTitle());
            stmt.setInt(2, quiz.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No quiz found with ID: " + quiz.getId());
            }
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String deleteQuestionsQuery = "DELETE FROM question WHERE quiz_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuestionsQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        String query = "DELETE FROM quiz WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No quiz found with ID: " + id);
            }
        }
    }

    @Override
    public List<Quiz> afficher() throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM quiz ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Quiz quiz = new Quiz(
                        rs.getInt("id"),
                        rs.getString("title"),
                        new ArrayList<>()
                );
                List<Question> questions = questionService.getQuestionsForQuiz(quiz.getId());
                quiz.setQuestions(questions);
                quizzes.add(quiz);
            }
        }
        return quizzes;
    }

    public Quiz getById(int id) throws SQLException {
        String query = "SELECT * FROM quiz WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Quiz quiz = new Quiz(
                            rs.getInt("id"),
                            rs.getString("title"),
                            new ArrayList<>()
                    );
                    List<Question> questions = questionService.getQuestionsForQuiz(quiz.getId());
                    quiz.setQuestions(questions);
                    return quiz;
                }
            }
        }
        return null;
    }
}