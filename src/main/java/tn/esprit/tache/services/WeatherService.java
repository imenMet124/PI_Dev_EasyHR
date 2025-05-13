package tn.esprit.tache.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherService {

    private static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String GEO_API_URL = "http://ip-api.com/json"; // API for location detection
    private static final String API_KEY = "d72acde9fb091218811d3589f11873ff"; // Replace with your actual API key

    // Method to get weather by city name
    public static String getWeatherByCity(String city) {
        return getWeatherData(WEATHER_API_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric");
    }

    // Method to get weather by latitude and longitude
    public static String getWeatherByLocation(double lat, double lon) {
        return getWeatherData(WEATHER_API_URL + "?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY + "&units=metric");
    }

    // Method to automatically detect user's city
    public static JSONObject getUserLocation() {
        try {
            URL url = new URL(GEO_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generic method to fetch weather data
    private static String getWeatherData(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse weather JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            String city = jsonResponse.getString("name");
            String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            double temperature = jsonResponse.getJSONObject("main").getDouble("temp");

            return "📍 " + city + "\n🌤️ " + weatherDescription + "\n🌡️ " + temperature + "°C";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error fetching weather data.";
        }
    }
}
