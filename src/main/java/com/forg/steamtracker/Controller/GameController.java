package com.forg.steamtracker.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String displayMyGamesChart(@RequestParam String userID){
        return "my_games";
    }
    @GetMapping("/my-games-json")
    @ResponseBody
    public String displayMyGamesJSON(@RequestParam String userID){
        return gameParser.parse(userID, key);
    }
}
