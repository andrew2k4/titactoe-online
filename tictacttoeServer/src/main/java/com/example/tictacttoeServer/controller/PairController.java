package com.example.tictacttoeServer.controller;

import com.example.tictacttoeServer.entity.Pair;
import com.example.tictacttoeServer.repository.PairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PairController {

    @Autowired
    private PairRepository pairRepository;


    @PostMapping("/addPair")
    public ResponseEntity<?> createPair(@RequestBody Pair d ){
        Pair pair = new Pair();
        pair.setC(d.getC());
        pair.setR(d.getR());
        pair.setCurrentPLayer(d.getCurrentPLayer());
        pairRepository.save(pair);
       return ResponseEntity.ok().build();
    }


    @GetMapping("/getGameState")
    public ResponseEntity<?> getPair(){
        return ResponseEntity.ok(pairRepository.findAll());
    }
}
