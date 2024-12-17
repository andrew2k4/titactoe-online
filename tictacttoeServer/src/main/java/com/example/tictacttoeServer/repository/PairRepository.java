package com.example.tictacttoeServer.repository;

import com.example.tictacttoeServer.entity.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PairRepository extends JpaRepository<Pair, Long> {
}
