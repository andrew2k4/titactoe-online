package controller;

import model.Pair;
import model.PairServer;
import model.TicTacToeModelInterface;
import view.TicTacToeView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is part of our controller.
 * It implements the right interface.
 * It serves as the controller in the MVC pattern.
 * Facilitating the interaction between view and model.
 */
public class TicTacToeControllerImpl implements TicTacToeControllerInterface {
    private TicTacToeView view;
    private TicTacToeModelInterface model;
    private final String urlString = "http://localhost:2020/api"; // Backend server URL
    private final byte player = 22;
    /**
     * Constructor, also initialize view and model.
     *
     * @param model game model with respect to MVC pattern.
     */
    public TicTacToeControllerImpl(TicTacToeModelInterface model) {
        this.model = model;
        this.view = new TicTacToeView(model, this);
        pollGameState();
    }

    /**
     * Called when a mouse press event occurs in the game.
     * This method handles the user interaction to update the game state.
     * It checks if the game already ended or if the position is already occupied.
     * If not the position will be set.
     *
     * @param c the column index of the position where the mouse is pressed.
     * @param r the row index of the position where the mouse is pressed
     */
    @Override
    public void whenMousePressed(byte c, byte r) throws IOException {

        if (c >= model.getColumns() || r >= model.getRows()) {
            return;
        }

        if (model.ended()) {
            return;
        }

        if (model.getAtPosition(c, r) != model.getPlayerNone()) {
            return;
        }
        byte t = player;
        model = model.setAtPosition(c, r, t);
        view.setGame(model);
        view.repaint();


        String jsonString = String.format("{\"c\":%d,\"r\":%d ,\"currentPLayer\":%d }", c, r, t);

        sendMoveToServer(jsonString);
    }

    /**
     * Method to send a move to the server via a POST request
     * @param jsonString the JSON string containing the move information
     */
    private void sendMoveToServer(String jsonString) {
        HttpURLConnection connection = null;

        try {

            URL url = new URL(urlString + "/addPair");


            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }


            int responseCode = connection.getResponseCode();
            System.out.println("Server response code: " + responseCode);


            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Error while sending the move: " + responseCode);
            }

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Error during server communication.");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    private void pollGameState() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    fetchGameState();
                } catch (IOException e) {
                    System.err.println("Error fetching game state: " + e.getMessage());
                }
            }
        }, 0, 5000); // Poll every 5 seconds
    }

    private void fetchGameState() throws IOException {

        URL url = new URL(urlString + "/getGameState");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");


        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            String jsonResponse = response.toString();
            System.out.println("Server response: " + jsonResponse);

            if (jsonResponse == null || jsonResponse.trim().isEmpty() || jsonResponse.equals("[]")) {
                System.out.println("No response from server.");
                return;
            }
            jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1);


            String[] items = jsonResponse.split("\\},\\{");


            List<PairServer> pairs = new ArrayList<>();


            for (String item : items) {

                item = item.replace("{", "").replace("}", "");


                PairServer pair = new PairServer();


                String[] keyValuePairs = item.split(",");


                for (String keyValue : keyValuePairs) {
                    String[] keyValueArray = keyValue.split(":");
                    String key = keyValueArray[0].trim().replace("\"", "");
                    String value = keyValueArray[1].trim().replace("\"", "");


                    switch (key) {
                        case "id":
                            pair.setId(Long.parseLong(value));
                            break;
                        case "c":
                            pair.setC(Byte.parseByte(value));
                            break;
                        case "r":
                            pair.setR(Byte.parseByte(value));
                            break;
                        case "currentPLayer":
                            pair.setCurrentPlayer(Byte.parseByte(value));
                            break;
                    }
                }


                pairs.add(pair);
            }

            // Update the view when the current player of the pair is different from the 'player' attribute
            if (pairs.get(pairs.size() - 1).getCurrentPlayer() != player) {
                PairServer move = pairs.get(pairs.size() - 1);
                model.setAtPosition(move.getC(), move.getR(), move.getCurrentPlayer());
                view.setGame(model);
                view.repaint();
            }

            System.out.println("List of moves: ");
            for (PairServer p : pairs) {
                System.out.println(p);
            }

        } else {
            System.out.println("Failed to fetch game state. Response code: " + responseCode);
        }

        connection.disconnect();
    }


}
