package com.forg.steamtracker.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository < Game, Long > {
    @Query(name = "SELECT g FROM game g WHERE g.game_name = ?1", nativeQuery = true)
    public Game findByName(String email);
}
