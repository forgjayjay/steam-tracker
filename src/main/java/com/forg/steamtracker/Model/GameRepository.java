package com.forg.steamtracker.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository < Game, Long > {
    // @Query(value = "SELECT g FROM game g WHERE g.game_name = :game_name AND g.owner_id = :owner_id", nativeQuery = true)
    // public Game findByOwnerID(@Param(value = "game_name") String name, @Param(value = "owner_id") String ownerID);
    // @Query(name = "SELECT g FROM game g WHERE g.game_name = ?1", nativeQuery = true)
    // public Game findByName(String name);
    @Query(value = "SELECT * FROM game WHERE game_name = :game_name AND owner_id = :owner_id", nativeQuery = true)
    public Game findByNameAndOwnerID(@Param(value = "game_name") String name, @Param(value = "owner_id") String ownerID);
}
