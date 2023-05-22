package eo.forg.steamtracker.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository < Game, Long > {
    @Query(value = "SELECT * FROM game WHERE game_name = :game_name AND owner_id = :owner_id", nativeQuery = true)
    public Game findByNameAndOwnerID(@Param(value = "game_name") String name, @Param(value = "owner_id") String ownerID);
    @Query(value = "SELECT DISTINCT owner_id FROM game", nativeQuery = true)
    public List<String> findAllUsers();
}
