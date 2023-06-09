package eo.forg.steamtracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import eo.forg.steamtracker.exceptions.UserNotFoundException;
import eo.forg.steamtracker.model.GameParser;
import eo.forg.steamtracker.model.GameRepository;

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
        return "my-games";
    }
    @GetMapping("/api/v1/my-games")
    @ResponseBody
    public String displayMyGamesJSONGET(@RequestParam String userID){
        return gameParser.parse(userID);
    }
    @PostMapping("/api/v1/my-games")
    @ResponseBody
    public String displayMyGamesJSONPOST(@RequestParam String userID){
        return gameParser.parse(userID);
    }
    @GetMapping("/api/v1/my-games-raw")
    @ResponseBody
    public String displayMyGamesRaw(@RequestParam String userID){
        try {
            return gameParser.parseRaw(userID);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
