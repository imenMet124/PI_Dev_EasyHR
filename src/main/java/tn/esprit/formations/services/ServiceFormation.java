package tn.esprit.formations.services;

import tn.esprit.formations.entities.Formation;
import tn.esprit.formations.entities.Quiz;
import tn.esprit.formations.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements IService<Formation> {

    private final Connection connection;
    private final QuizService quizService;

    public ServiceFormation() {
        connection = MyDatabase.getInstance().getConnection();
        quizService = new QuizService();
    }

    @Override
    public void ajouter(Formation formation) throws SQLException {
        String query = "INSERT INTO formation (titre, description, file_path, date_creation, quiz_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, formation.getTitre());
            stmt.setString(2, formation.getDescription());
            stmt.setString(3, formation.getFilePath());
            stmt.setTimestamp(4, Timestamp.valueOf(formation.getDateCreation()));
            if (formation.getQuiz() != null) {
                stmt.setInt(5, formation.getQuiz().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    formation.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void modifier(Formation formation) throws SQLException {
        String query = "UPDATE formation SET titre = ?, description = ?, file_path = ?, quiz_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, formation.getTitre());
            stmt.setString(2, formation.getDescription());
            stmt.setString(3, formation.getFilePath());
            if (formation.getQuiz() != null) {
                stmt.setInt(4, formation.getQuiz().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, formation.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No formation found with ID: " + formation.getId());
            }
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM formation WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No formation found with ID: " + id);
            }
        }
    }

    public Formation getById(int id) throws SQLException {
        String query = "SELECT * FROM formation WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Formation formation = new Formation(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getString("file_path"),
                            rs.getTimestamp("date_creation").toLocalDateTime()
                    );
                    int quizId = rs.getInt("quiz_id");
                    if (!rs.wasNull()) {
                        Quiz quiz = quizService.getById(quizId);
                        formation.setQuiz(quiz);
                    }
                    return formation;
                }
            }
        }
        return null;
    }

    @Override
    public List<Formation> afficher() throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String sql = "SELECT * FROM formation ORDER BY date_creation DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Formation formation = new Formation(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("file_path"),
                        rs.getTimestamp("date_creation").toLocalDateTime()
                );
                int quizId = rs.getInt("quiz_id");
                if (!rs.wasNull()) {
                    Quiz quiz = quizService.getById(quizId);
                    formation.setQuiz(quiz);
                }
                formations.add(formation);
            }
        }
        return formations;
    }

    // Helper method to fetch a quiz by ID (optional, if QuizService doesn't have this)
    public Quiz getQuizById(int quizId) throws SQLException {
        return quizService.getById(quizId);
    }
}