package com.forg.steamtracker.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.forg.steamtracker.Model.GameParser;
import com.forg.steamtracker.Model.GameRepository;

@Controller
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GameParser gameParser;
    
    @Value("${steam.key}")
    private String key;
    
    @GetMapping("")
    public String mainPage(){
        return "index";
    }

    @GetMapping("/my-games")
    public String displayMyGames(@RequestParam String userID){
        gameParser.parse(userID, key);
        return "my_games";
    }
}
