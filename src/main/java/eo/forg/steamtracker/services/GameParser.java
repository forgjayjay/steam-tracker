package eo.forg.steamtracker.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.ScopedProxyMode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eo.forg.steamtracker.exceptions.UserNotFoundException;
import eo.forg.steamtracker.model.Game;
import eo.forg.steamtracker.model.GameRepository;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Configurable
public class GameParser {
    private List<Game> gamesArray;
    private Logger logger = LoggerFactory.getLogger(GameParser.class);
    private boolean APItimeout = false;
    private String globalUserID;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    APIparser apiParser;
    public GameParser (){
        
    }

    public String parse(String userID){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        byte[] data;
        globalUserID = userID;
        gamesArray = cachedGames();
        try {
            mapper.writeValue(out, gamesArray);
            logger.info("Return array: {}", gamesArray);
            data = out.toByteArray();
            return new String("{\"games\":"+ new String(data) + "}");
        } catch (IOException exception) {
            logger.error("Error occurred: {}", exception.getMessage());
        }
        return "";
    }

    public String parseRaw(String userID) throws UserNotFoundException{
        return apiParser.parseAPIForUserIdRaw(userID);
    }
    
    private List<Game> cachedGames(){
        List<Game> cachedGames = gamesArray;
        if(!APItimeout){
            APItimeout = !APItimeout;
            APItimer();
            cachedGames = checkGames();
        }
        return cachedGames;
    }

    private List<Game> checkGames(){
        List<Game> returnList = new ArrayList<>();
        logger.info("Checking games with provided user ID: {}", globalUserID);
        Game savedGame;
        for (Game game : apiParser.parseAPIForUserId(globalUserID)) {
            savedGame = gameRepository.findByNameAndOwnerID(game.getName(), game.getOwnerID());
            logger.info("Looking for {} in the database", game);
            Date lastUpdate = new Date();
            if(savedGame==null) {
                logger.info("Game not found: {}", game.toString());
                game.setMinutes_played_today(game.getPlaytime_weeks());
                game.setPrevious_time(0);
                game.setPrevious_update_date(lastUpdate);
                save(game);
            } else {
                logger.info("Game found: {}", savedGame.toString());
                savedGame.setMinutes_played_today(Math.max(game.getPlaytime_forever()-savedGame.getPlaytime_forever(), savedGame.getMinutes_played_today()));
                save(savedGame);
                game = savedGame;
            }
            if(game.getMinutes_played_today()>0) returnList.add(game);
        }
        return returnList;
    }

    private void save(Game game){
        gameRepository.save(game);
    }

    private void APItimer(){
        Timer APItimer = new Timer();

        APItimer.schedule(new TimerTask() {
            @Override
            public void run() {
                APItimeout = !APItimeout;   
            }
        }, 10 * 60 * 1000);
    }
}
