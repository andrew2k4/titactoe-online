package com.example.server.service;

import com.example.server.dto.GameStatutDto;
import com.example.server.dto.MoveDto;
import com.example.server.entity.GameStatut;
import com.example.server.repository.GameStatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameStatutRepository gameStatutRepository;

    public void makeMove(MoveDto pairDto){
         GameStatut gameStatut = gameStatutRepository.findLatestGameStatut();

         List<List<Byte>> board = gameStatut.getBoard();
         board.get(pairDto.getR()).set(pairDto.getC(), pairDto.getTurnPlayer());

         gameStatut.setBoard(board);

         if(pairDto.getTurnPlayer() == (byte) 1){
             gameStatut.setCurrentPlayer((byte) 22);
         }else {
             gameStatut.setCurrentPlayer((byte) 1);
         }

        gameStatutRepository.save(gameStatut);
    }


    public GameStatutDto getActualGameStatut(){

        GameStatut gameStatut = gameStatutRepository.findLatestGameStatut();
        System.out.println(gameStatut.getCurrentPlayer());

        GameStatutDto gameStatutDto = new GameStatutDto();

        gameStatutDto.setBoard(gameStatut.getBoard());
        gameStatutDto.setCurrentPlayer(gameStatut.getCurrentPlayer());
        return gameStatutDto;
    }

    public void startGame() {
        List<List<Byte>> board = new ArrayList<>() ;

        for (int i = 0; i < 3; i++) {
            List<Byte> row = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                row.add((byte) 0);
            }
            board.add(row);
        }
        gameStatutRepository.save(GameStatut.builder().board(board).currentPlayer((byte)1).build());
    }
}
