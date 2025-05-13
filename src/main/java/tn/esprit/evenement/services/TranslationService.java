package tn.esprit.evenement.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslationService {

    public static String translate(String text, String fromLang, String toLang) {
        try {
            // Encodage du texte pour éviter les erreurs de requête
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            String url = "https://translate.google.com/m?hl=" + toLang + "&sl=" + fromLang + "&q=" + encodedText;

            // Connexion à Google Translate
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // Simule un navigateur pour éviter le blocage
                    .timeout(5000) // Timeout de 5 secondes pour éviter les blocages
                    .get();

            // Extraction du texte traduit
            Element translatedTextElement = doc.selectFirst(".result-container");

            if (translatedTextElement != null) {
                return translatedTextElement.text();
            } else {
                return "⚠ Impossible de récupérer la traduction.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "⚠ Erreur de connexion à Google Translate.";
        }
    }
}
