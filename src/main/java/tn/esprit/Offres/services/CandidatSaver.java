package tn.esprit.Offres.services;

import tn.esprit.Offres.entities.Candidat;
import java.io.File;
import java.sql.SQLException;

public class CandidatSaver {

    public static void saveCandidat(Candidat candidat) {
        ServiceCandidat serviceCandidat = new ServiceCandidat();
        try {
            serviceCandidat.ajouter(candidat);
            System.out.println("✅ Candidat ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du candidat: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Change the file path to the actual uploaded PDF path
        File pdfFile = new File("C:\\Users\\eyaam\\Documents\\uploaded_candidat.pdf");

        // Extract text from PDF
        String pdfContent = PDFExtractor.extractTextFromPDF(pdfFile);

        // Parse extracted text into a Candidat object
        Candidat extractedCandidat = PDFDataParser.extractCandidatInfo(pdfContent);

        // Save extracted Candidat to the database
        saveCandidat(extractedCandidat);
    }
}
