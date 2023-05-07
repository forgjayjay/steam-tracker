package com.forg.steamtracker.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.forg.steamtracker.Model.GameParser;
import com.forg.steamtracker.Model.GameRepository;

@Controller
@SessionAttributes("gameParser")
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GameParser gameParser;
    
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
    public String displayMyGamesJSONGET(@RequestParam String userID){
        return gameParser.parse(userID);
    }
    @PostMapping("/my-games-json")
    @ResponseBody
    public String displayMyGamesJSONPOST(@RequestParam String userID){
        return gameParser.parse(userID);
    }
}
