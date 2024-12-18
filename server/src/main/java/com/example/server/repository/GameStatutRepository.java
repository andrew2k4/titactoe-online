package com.example.server.repository;

import com.example.server.entity.GameStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameStatutRepository extends JpaRepository<GameStatut, Long> {
    @Query("SELECT g FROM GameStatut g ORDER BY g.id DESC")
    GameStatut findLatestGameStatut();
}
