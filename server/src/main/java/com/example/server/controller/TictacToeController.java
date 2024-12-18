package com.example.server.controller;


import com.example.server.dto.MoveDto;
import com.example.server.repository.GameStatutRepository;
import com.example.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TictacToeController {


    @Autowired
    private GameService gameService;

    @Autowired
    private GameStatutRepository gameStatutRepository;

    @PostMapping("/startgame")
    public ResponseEntity<?> startGame() {
        gameService.startGame();
        return  ResponseEntity.ok().build();
    }
    @PostMapping("/move")
    public ResponseEntity<?> makeMove(@RequestBody MoveDto moveDto){

        gameService.makeMove(moveDto);

        return ResponseEntity.accepted().build();
    }


    @GetMapping("/gamestatut")
    public ResponseEntity<?> getGameStatut(){

        return ResponseEntity.ok(gameService.getActualGameStatut());
    }

}
