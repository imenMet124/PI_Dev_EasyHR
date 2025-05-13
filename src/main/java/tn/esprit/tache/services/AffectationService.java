package tn.esprit.tache.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import tn.esprit.tache.entities.Affectation;
import tn.esprit.tache.entities.User;
import tn.esprit.tache.utils.DBConnection;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AffectationService implements IAffectationService {
    private Connection conn = DBConnection.getInstance().getConnection();

    @Override
    public void affecterEmployeTache(int idEmp, int idTache) {
        // Vérifier si l'employé existe
        String checkUserSql = "SELECT COUNT(*) FROM user WHERE iyedIdUser = ?";
        String checkTacheSql = "SELECT COUNT(*) FROM tache WHERE id_tache = ?";
        String insertSql = "INSERT INTO `affectation` ( `id_emp`, `id_tache`,  `date_affectation`) VALUES (?, ?, NOW());";

        System.out.println("🔍 Vérification de l'existence de l'employé avec ID: " + idEmp);
        System.out.println("🔍 Vérification de l'existence de la tâche avec ID: " + idTache);

        try (
                PreparedStatement checkUserPs = conn.prepareStatement(checkUserSql);
                PreparedStatement checkTachePs = conn.prepareStatement(checkTacheSql);
                PreparedStatement insertPs = conn.prepareStatement(insertSql)
        ) {
            // Vérifier l'employé
            checkUserPs.setInt(1, idEmp);
            System.out.println("📝 SQL: " + checkUserSql.replace("?", String.valueOf(idEmp))); // Debug SQL
            ResultSet rsUser = checkUserPs.executeQuery();
            rsUser.next();
            if (rsUser.getInt(1) == 0) {
                System.err.println("❌ Erreur : L'employé avec l'ID " + idEmp + " n'existe pas.");
                return;
            }

            // Vérifier la tâche
            checkTachePs.setInt(1, idTache);
            System.out.println("📝 SQL: " + checkTacheSql.replace("?", String.valueOf(idTache))); // Debug SQL
            ResultSet rsTache = checkTachePs.executeQuery();
            rsTache.next();
            if (rsTache.getInt(1) == 0) {
                System.err.println("❌ Erreur : La tâche avec l'ID " + idTache + " n'existe pas.");
                return;
            }

            // Si tout est OK, insérer l'affectation
            insertPs.setInt(1, idEmp);
            insertPs.setInt(2, idTache);

            // Affichage de la requête avec les valeurs remplacées
            String finalInsertSql = insertSql
                    .replaceFirst("\\?", String.valueOf(idEmp))
                    .replaceFirst("\\?", String.valueOf(idTache));
            System.out.println("📝 SQL: " + finalInsertSql); // Debug SQL

            insertPs.executeUpdate();
            System.out.println("✅ Employé " + idEmp + " affecté à la tâche " + idTache + " avec succès.");

        } catch (SQLException e) {
            System.err.println("🚨 Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public List<Affectation> getAllAffectations() {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT a.*, u.iyedNomUser, t.titre_tache FROM affectation a " +
                "JOIN user u ON a.id_emp = u.iyedIdUser " +  // Fixed column name
                "JOIN tache t ON a.id_tache = t.id_tache";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Affectation affectation = new Affectation(
                        rs.getInt("id_affectation"),
                        rs.getInt("id_emp"),  // Fixed column name
                        rs.getInt("id_tache"),
                        rs.getDate("date_affectation")
                );
                affectations.add(affectation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }

    @Override
    public void retirerEmployeTache(int idEmp, int idTache) {
        String sql = "DELETE FROM affectation WHERE id_emp=? AND id_tache=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmp);  // Employee ID
            ps.setInt(2, idTache);  // Task ID
            ps.executeUpdate();
            System.out.println("✅ Employé retiré de la tâche.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Affectation> getAffectationsByEmploye(int idEmp) {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT * FROM affectation WHERE id_emp=?";  // Fixed column name
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmp);  // Employee ID
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                affectations.add(new Affectation(rs.getInt("id_affectation"), rs.getInt("id_emp"), rs.getInt("id_tache"), rs.getDate("date_affectation")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }

    @Override
    public List<Affectation> getAffectationsByTache(int idTache) {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT * FROM affectation WHERE id_tache=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTache);  // Task ID
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                affectations.add(new Affectation(rs.getInt("id_affectation"), rs.getInt("id_emp"), rs.getInt("id_tache"), rs.getDate("date_affectation")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }

    /**
     * Génère un PDF avec la liste des affectations de tâches, y compris les données de la table 'User'.
     *
     * @param filePath Chemin pour enregistrer le fichier PDF généré.
     */
    public void genererPdf(String filePath) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new GrayColor(0.2f));
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

        // Titre
        Paragraph title = new Paragraph("📄 Liste des Affectations\n\n", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Créer une table avec 4 colonnes (Employé, Tâche, Projet, Date Affectation)
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{2, 2, 2, 2}); // Largeur des colonnes égales

        // En-têtes avec style
        PdfPCell cell;
        BaseColor headerColor = new BaseColor(0, 102, 204); // Bleu Pro
        String[] headers = {"Employé", "Tâche", "Projet", "Date Affectation"};

        for (String header : headers) {
            cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }

        // SQL pour récupérer les données
        String sql = "SELECT u.iyedNomUser, t.titre_tache, p.nom_projet, a.date_affectation " +
                "FROM affectation a " +
                "JOIN user u ON a.id_emp = u.iyedIdUser " + // Joindre avec la table 'user'
                "JOIN tache t ON a.id_tache = t.id_tache " +
                "JOIN projet p ON t.id_projet = p.id_projet";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String employe = rs.getString("iyedNomUser");
                String tache = rs.getString("titre_tache");
                String projet = rs.getString("nom_projet");

                // Convertir la date en format lisible
                java.sql.Date dateAffectation = rs.getDate("date_affectation");
                String dateFormatted = (dateAffectation != null) ? new SimpleDateFormat("dd/MM/yyyy").format(dateAffectation) : "N/A";

                // Ajouter les données au tableau
                table.addCell(createCell(employe, cellFont));
                table.addCell(createCell(tache, cellFont));
                table.addCell(createCell(projet, cellFont));
                table.addCell(createCell(dateFormatted, cellFont)); // Date affichée correctement
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        document.add(table);
        document.close();
        System.out.println("✅ PDF généré avec succès : " + filePath);
    }

    // Méthode utilitaire pour créer des cellules de tableau avec un style
    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

}
