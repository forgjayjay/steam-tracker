package eo.forg.steamtracker.model;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class DatabaseUpdater {

    private Logger logger = LoggerFactory.getLogger(GameParser.class);
    private LocalDate lastUpdateDate = LocalDate.now();
    private Timer timer = new Timer();
    private Calendar previous = Calendar.getInstance();

    @Autowired
    GameRepository gameRepository;

    @Autowired
    APIparser apiParser;

    @PostConstruct
    private void startUpdate(){
        updateGamesOnSchedule();
        logger.info("Starting timer from {}", lastUpdateDate.toString());
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
                if(existingGame==null){
                    game.setMinutes_played_today(0);
                    logger.info("Update games on load didn't find existing game, adding it to repository: {}", game.toString());
                    gameRepository.save(game);
                }else {
                    
                    existingGame.setPrevious_time(existingGame.getPlaytime_forever());
                    existingGame.setPlaytime_forever(game.getPlaytime_forever());
                    existingGame.setMinutes_played_today(0);
                    logger.info("Update games on load found existing game: {}", existingGame.toString());
                    gameRepository.save(existingGame);
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

}
