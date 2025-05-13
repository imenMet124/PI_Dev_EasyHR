package tn.esprit.Offres.services;

import tn.esprit.Offres.entities.Candidat;
import tn.esprit.Offres.services.PDFExtractor;

import java.io.File;
import java.util.regex.*;

public class PDFDataParser {

    public static Candidat extractCandidatInfo(String pdfContent) {
        Candidat candidat = new Candidat();

        candidat.setNom(extractValue(pdfContent, "Nom:\\s*(.*)"));
        candidat.setEmail(extractValue(pdfContent, "Email:\\s*([\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,})"));
        candidat.setPhone(extractValue(pdfContent, "Téléphone:\\s*(\\d{8,15})"));
        candidat.setPosition(extractValue(pdfContent, "Poste Actuel:\\s*(.*)"));
        candidat.setCompetence(extractValue(pdfContent, "Compétences:\\s*(.*)"));

        // Handling Disponibilité Enum
        String dispoText = extractValue(pdfContent, "Disponibilité:\\s*(.*)");
        if (dispoText != null) {
            try {
                candidat.setDisponibilite(Candidat.Disponibilite.valueOf(dispoText.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("⚠ Disponibilité non reconnue: " + dispoText);
            }
        }

        return candidat;
    }

    private static String extractValue(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static void main(String[] args) {
        File pdfFile = new File("C:\\Users\\MSI\\Downloads\\uploaded_candidat.pdf"); // Change path
        String pdfContent = PDFExtractor.extractTextFromPDF(pdfFile);

        Candidat extractedCandidat = extractCandidatInfo(pdfContent);
        System.out.println("✅ Candidat Extracted:");
        System.out.println("📌 Nom: " + extractedCandidat.getNom());
        System.out.println("📧 Email: " + extractedCandidat.getEmail());
        System.out.println("📞 Téléphone: " + extractedCandidat.getPhone());
        System.out.println("💼 Poste Actuel: " + extractedCandidat.getPosition());
        System.out.println("🛠️ Compétences: " + extractedCandidat.getCompetence());
        System.out.println("📅 Disponibilité: " + extractedCandidat.getDisponibilite());
    }
}
