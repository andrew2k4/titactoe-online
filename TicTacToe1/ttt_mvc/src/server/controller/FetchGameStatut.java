package server.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import server.model.StatutGame;
import utils.ApiClient;
import utils.Url;

import java.io.IOException;
import java.util.List;

public class FetchGameStatut {

    public static StatutGame get() throws IOException, ParseException {
        try {
            JSONObject responseBody =  ApiClient.get(Url.GET_GAME_STATUS);

            if (responseBody == null){
                return new StatutGame();
            }

            return parseJsonToGameStatutDto(responseBody);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Impossible to reach the server");
        }
    }


    private static StatutGame parseJsonToGameStatutDto(JSONObject jsonObject) {
        // Extract the "board" data from the JSON object
        List<List<?>> boardList = (List<List<?>>) jsonObject.get("board");

        // Convert List<List<?>> to byte[][]
        byte[][] board = new byte[boardList.size()][];
        for (int i = 0; i < boardList.size(); i++) {
            List<?> row = boardList.get(i);
            board[i] = new byte[row.size()];
            for (int j = 0; j < row.size(); j++) {
                // Convert each value to Byte
                board[i][j] = ((Number) row.get(j)).byteValue();
            }
        }

        // Extract the "currentPlayer" value and ensure it's converted to a byte
        byte currentPlayer = ((Number) jsonObject.get("currentPlayer")).byteValue();

        // Create and populate the StatutGame object
        StatutGame statutGame = new StatutGame();
        statutGame.setBoard(board);
        statutGame.setCurrentPlayer(currentPlayer);

        return statutGame;
    }

}
