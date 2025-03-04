package tn.esprit.formations.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KairosAPI {

    private static String KAIROS_APP_ID;
    private static String KAIROS_APP_KEY;
    private static final String BASE_URL = "https://idv-eu.kairos.com/v0.1/biometric-verification";

    // Static block to load credentials from config.properties
    static {
        try (InputStream input = KairosAPI.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            prop.load(input);
            KAIROS_APP_ID = prop.getProperty("kairos.app.id");
            KAIROS_APP_KEY = prop.getProperty("kairos.app.key");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load credentials from config.properties", e);
        }
    }

    /**
     * Performs biometric verification using a selfie and an image for comparison.
     *
     * @param selfieFile The selfie image file (for liveness detection).
     * @param photoFile  The photo image file to compare against.
     * @return The API response as a String, or an error message if an error occurs.
     */
    public static String verifyBiometric(File selfieFile, File photoFile) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(BASE_URL);

            // Set headers
            httpPost.setHeader("app_id", KAIROS_APP_ID);
            httpPost.setHeader("app_key", KAIROS_APP_KEY);

            // Build the request body
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addBinaryBody("selfie", selfieFile, ContentType.IMAGE_JPEG, selfieFile.getName())
                    .addBinaryBody("image", photoFile, ContentType.IMAGE_JPEG, photoFile.getName())
                    .build();
            httpPost.setEntity(entity);

            // Send the request and handle the response
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                if (statusCode == 200) {
                    return responseBody; // Success
                } else {
                    return "Error: " + statusCode + " - " + responseBody; // API returned an error
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage(); // Return error message
        }
    }
}