package tn.esprit.tache.services;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class OAuthRedirectHandler {

    private static final int PORT = 8080;
    private HttpServer server;
    private Consumer<String> authCallback;

    public OAuthRedirectHandler() {}

    // Start the local HTTP server to listen for OAuth redirect
    public void startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/callback", new OAuthHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Listening for OAuth redirect on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Stop the server after receiving the authorization code
    public void stopServer() {
        if (server != null) {
            server.stop(1);
            System.out.println("OAuth server stopped.");
        }
    }

    public void setAuthCallback(Consumer<String> callback) {
        this.authCallback = callback;
    }

    private class OAuthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI requestUri = exchange.getRequestURI();
            String query = requestUri.getQuery();
            String response;

            if (query != null && query.contains("code=")) {
                Map<String, String> queryParams = splitQuery(query);
                String authorizationCode = queryParams.get("code");

                if (authorizationCode != null) {
                    if (authCallback != null) {
                        authCallback.accept(authorizationCode);
                    }
                    response = "Authorization successful! You can now close this window.";
                    stopServer();
                } else {
                    response = "Error: Authorization code not found.";
                }
            } else {
                response = "Invalid request.";
            }

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private Map<String, String> splitQuery(String query) {
            Map<String, String> queryPairs = new HashMap<>();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    queryPairs.put(pair.substring(0, idx), pair.substring(idx + 1));
                }
            }
            return queryPairs;
        }
    }
}
