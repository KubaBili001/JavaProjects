package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class Controller {

    protected static final String CLIENT_ID = "5aaada315f4641a8adc272bfdee529fd";
    protected static final String CLIENT_SECRET = "fc76999fdff749ee9da9b1c1090f4389";
    protected static final String REDIRECT_URI = "http://localhost:8080";
    protected static final String GRANT_TYPE = "authorization_code";
    protected static String AUTH_CODE;
    protected static String ACCESS_TOKEN;
    public static String CATEGORY = "";
    public static void getAuthCode() {

        log("waiting for code...");

        try {

            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);

            server.createContext("/",
                    exchange -> {
                        String request;
                        String query = exchange.getRequestURI().getQuery();
                        if (query != null && query.contains("code")) {
                            AUTH_CODE = query.split("=")[1];
                            request = "Got the code. Return back to your program.";
                        } else {
                            request = "Authorization code not found. Try again.";
                        }

                        exchange.sendResponseHeaders(200, request.length());
                        exchange.getResponseBody().write(request.getBytes());
                        exchange.getResponseBody().close();
                    }
            );

            server.start();

            while (AUTH_CODE == null)
                Thread.sleep(1000);

            server.stop(1);

            log("code received");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void accessSpotify() {

        log("making http request for access_token...");

        try {
            HttpClient client = HttpClient.newBuilder().build();

            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .uri(URI.create(Main.authorizationServerUrl+"/api/token"))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "grant_type=" + GRANT_TYPE
                                    + "&code=" + AUTH_CODE
                                    + "&client_id=" + CLIENT_ID
                                    + "&client_secret=" + CLIENT_SECRET
                                    + "&redirect_uri=" + REDIRECT_URI
                    )).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ACCESS_TOKEN = View.parseToken(response.body());

            log("Success!");

            Main.isAuthorized = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void accessCategory(){

        if(Objects.equals(CATEGORY, "categories") || !View.categoriesMap.isEmpty()) {
            try {
                HttpClient client = HttpClient.newBuilder().build();

                HttpRequest request = HttpRequest.newBuilder()
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .uri(URI.create(Main.apiServerUrl + "/v1/browse/" + CATEGORY + "?country=US"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.body().contains("error")) {

                    if (CATEGORY.contains("categories")&&CATEGORY.contains("playlists")) {
                        JsonObject error = JsonParser.parseString(response.body()).getAsJsonObject().get("error").getAsJsonObject();
                        String message = error.get("message").getAsString();
                        log(message);
                    }

                } else {

                    switch (CATEGORY) {
                        case "new-releases" -> View.parseNew(response.body());
                        case "categories"   -> View.parseCategories(response.body());
                        default             -> View.parsePlaylists(response.body());
                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void log(String str){
        System.out.println(str);
    }
}
