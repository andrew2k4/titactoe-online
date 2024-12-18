package utils;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient{
    private static final int SUCCESS_CODE = 200;
    private static final int ACCEPTED_CODE = 202;

    public static JSONObject get(String serverUrl) throws IOException, ParseException {
        HttpURLConnection connection = prepareConnection(serverUrl, "GET", null);
        return handleResponse(connection);
    }


    public static JSONObject post(String serverUrl, String payload) throws IOException, ParseException {
        HttpURLConnection connection = prepareConnection(serverUrl, "POST", payload);
        return handleResponse(connection);
    }

    // Préparer la connexion
    private static HttpURLConnection prepareConnection(String serverUrl, String method, String payload) throws IOException {
        URL url = new URL(serverUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        if ("POST".equals(method) && payload != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }
        }
        return connection;
    }

    // Gérer la réponse HTTP
    private static JSONObject handleResponse(HttpURLConnection connection) throws IOException, ParseException {
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode != SUCCESS_CODE && responseCode != ACCEPTED_CODE) {
            throw new RuntimeException("Server response failed with code: " + responseCode);
        }

        String jsonResponse = readResponse(connection);
        return parseJson(jsonResponse);
    }

    // Lire la réponse depuis le serveur
    private static String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    // Parser le JSON pour retourner un JSONObject
    private static JSONObject parseJson(String response) throws ParseException {
        // Handle empty or null input
        if (response == null || response.trim().isEmpty()) {
            return new JSONObject();
        }

        JSONParser parser = new JSONParser();
        Object parsedObject = parser.parse(response);

        if (parsedObject instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) parsedObject;
            System.out.println(2);
            if (jsonArray.isEmpty()) {
                return new JSONObject(); // Retourne un objet vide si l'array est vide
            }
            return (JSONObject) jsonArray.get(jsonArray.size() - 1); // Dernier élément de l'array
        } else if (parsedObject instanceof JSONObject) {
            return (JSONObject) parsedObject;
        } else  {
            throw new JSONException("Impossible to parse the response body");
        }
    }
}
