package com.forg.steamtracker.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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

import jakarta.annotation.PostConstruct;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Configurable
public class GameParser {
    private List<Game> gamesArray;
    private Logger logger = LoggerFactory.getLogger(GameParser.class);
    private LocalDate lastUpdateDate = LocalDate.now();
    private Timer timer = new Timer();
    private Calendar previous = Calendar.getInstance();
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
            logger.info("Return array: " + gamesArray);
            data = out.toByteArray();
            return new String("{\"games\":"+ new String(data) + "}");
        } catch (IOException exception) {
            logger.error("Error occurred: {}", exception.getMessage());
        }
        return "";
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
        logger.info("Checking games with provided user ID:" + globalUserID);
        Game savedGame;
        for (Game game : apiParser.parseAPIForUserId(globalUserID)) {
            savedGame = gameRepository.findByNameAndOwnerID(game.getName(), game.getOwnerID());
            if(savedGame==null) {
                logger.info("Game not found: " + game.toString());
                game.setMinutes_played_today(game.getPlaytime_weeks());
                game.setPrevious_time(0);
                gameRepository.save(game);
            } else {
                logger.info("Game found: " + savedGame.toString());
                game.setMinutes_played_today(Math.max(game.getPlaytime_forever()-savedGame.getPlaytime_forever(), savedGame.getMinutes_played_today()));
                savedGame.setMinutes_played_today(game.getMinutes_played_today());
                gameRepository.save(savedGame);
            }
            if(game.getMinutes_played_today()>0) returnList.add(game);
        }
        return returnList;
    }

    @PostConstruct
    private void startUpdate(){
        updateGamesOnSchedule();
        logger.info("Starting timer from " + lastUpdateDate.toString());
        dailyUpdateTimer();
    }
    
    private void updateGamesOnSchedule(){
        logger.info("Updating database on schedule");
        List<String> userArray = gameRepository.findAllUsers();
        if(userArray==null || userArray.size()<1) {
            logger.error("No users were found in the database");
            return;
        }
        Game existingGame;
        for (String localUserID : userArray) {
            for (Game game : apiParser.parseAPIForUserId(localUserID)) {
                game.setPrevious_time(game.getPlaytime_forever());
                existingGame = gameRepository.findByNameAndOwnerID(game.getName(), game.getOwnerID());
                if(existingGame!=null){
                    existingGame.setPrevious_time(existingGame.getPlaytime_forever());
                    existingGame.setPlaytime_forever(game.getPlaytime_forever());
                    existingGame.setMinutes_played_today(0);
                    logger.info("Update games on load found existing game: " + existingGame.toString());
                    gameRepository.save(existingGame);
                }else {
                    game.setPrevious_time(game.getPlaytime_forever());
                    game.setMinutes_played_today(0);
                    logger.info("Update games on load didn't find existing game, adding it to repository: " + game.toString());
                    gameRepository.save(game);
                }
            }   
        }
    }

    private void dailyUpdateTimer(){
        Calendar next = Calendar.getInstance();
        next.set(Calendar.HOUR_OF_DAY, next.get(Calendar.HOUR_OF_DAY) + 1); 
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar current = Calendar.getInstance();
                if (current.get(Calendar.DAY_OF_YEAR) != previous.get(Calendar.DAY_OF_YEAR)) {
                    previous = current;
                    updateGamesOnSchedule();
                    timer.cancel();
                    dailyUpdateTimer();
                }
            }
        }, next.getTime(), 60 * 60 * 1000);
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
