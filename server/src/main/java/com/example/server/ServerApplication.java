package com.example.server;

import com.example.server.entity.GameStatut;
import com.example.server.repository.GameStatutRepository;
import com.example.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Autowired
	GameService gameService;
	@Override
	public void run(String... args) throws Exception {
		gameService.startGame();
	}
}
