package tn.esprit.Offres.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class PDFExtractor {

    public static String extractTextFromPDF(File file) {
        String extractedText = "";
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        } catch (IOException e) {
            System.err.println("❌ Error reading PDF: " + e.getMessage());
        }
        return extractedText;
    }

    public static void main(String[] args) {
        File pdfFile = new File("C:\\Users\\eyaam\\Documents\\uploaded_candidat.pdf"); // Change path
        String pdfContent = extractTextFromPDF(pdfFile);
        System.out.println("📄 Extracted PDF Content: \n" + pdfContent);
    }
}
