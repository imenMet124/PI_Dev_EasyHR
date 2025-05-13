package tn.esprit.tache.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.tache.services.WeatherService;
import org.json.JSONObject;

public class WeatherController {

    @FXML
    private TextField cityTextField;

    @FXML
    private Button getWeatherButton;

    @FXML
    private Label weatherLabel;

    @FXML
    public void initialize() {
        fetchWeatherByLocation(); // Automatically fetch weather on app start
    }

    @FXML
    private void fetchWeather() {
        String city = cityTextField.getText().trim();
        if (!city.isEmpty()) {
            String weatherInfo = WeatherService.getWeatherByCity(city);
            weatherLabel.setText(weatherInfo);
        } else {
            weatherLabel.setText("❌ Please enter a city name.");
        }
    }

    @FXML
    private void fetchWeatherByLocation() {
        JSONObject location = WeatherService.getUserLocation();
        if (location != null && location.has("lat") && location.has("lon") && location.has("city")) {
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");
            String detectedCity = location.getString("city");

            String weatherInfo = WeatherService.getWeatherByLocation(lat, lon);
            weatherLabel.setText(weatherInfo);
            cityTextField.setText(detectedCity); // Autofill detected city
        } else {
            weatherLabel.setText("❌ Could not determine location.");
        }
    }
}
