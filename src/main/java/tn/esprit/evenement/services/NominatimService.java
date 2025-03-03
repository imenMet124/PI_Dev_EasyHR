package tn.esprit.evenement.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NominatimService {

    private static final String API_URL = "https://nominatim.openstreetmap.org/search?q=Tunis&format=json&addressdetails=1&limit=5";
    private static final OkHttpClient client = new OkHttpClient();

    public static List<String> getPlaceSuggestions(String input) {
        List<String> suggestions = new ArrayList<>();
        try {
            if (input.length() < 3) {
                return suggestions; // Ne pas faire de requête pour des mots trop courts
            }

            HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
            urlBuilder.addQueryParameter("q", input);
            urlBuilder.addQueryParameter("format", "json");
            urlBuilder.addQueryParameter("addressdetails", "1");
            urlBuilder.addQueryParameter("limit", "5");

            Request request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .header("User-Agent", "JavaFX-Event-App") // 🔥 Obligatoire pour éviter d'être bloqué par Nominatim
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                System.out.println("🔍 Réponse JSON : " + jsonData); // ✅ Vérifier si les données arrivent bien
                JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject place = jsonArray.get(i).getAsJsonObject();
                    String displayName = place.get("display_name").getAsString();
                    suggestions.add(displayName);
                }
            } else {
                System.out.println("⚠ Erreur API Nominatim : " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return suggestions;
    }
}
