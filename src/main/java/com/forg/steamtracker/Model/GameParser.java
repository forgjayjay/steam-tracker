package com.forg.steamtracker.Model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Service
@Configurable
public class GameParser {
    @Value("${steam.key}") public String key;
    private ArrayList<Game> gameArray = new ArrayList<>();
    private String link = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?count=10&key="+key+"&steamid=";
    private Logger logger = LoggerFactory.getLogger(GameParser.class);

    

    @Autowired
    GameRepository gameRepository;
  
    public GameParser (){
        
    }
    public String parse(String userID){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        byte[] data;
        gameArray.clear();
        checkGames(userID);
        try {
            mapper.writeValue(out, gameArray);
            logger.info("Return array: " + gameArray);
            data = out.toByteArray();
            return new String("{\"games\":"+ new String(data) + "}");
        } catch (IOException e) {
            logger.error("Error occurred: "+e.getMessage());
        }
        return "";
    }
   
    public void checkGames(String userID){
        logger.info("Checking games with provided user ID:" + userID);
        Game savedGame;
        for (Game game :parseApiForUserId(userID)) {
            savedGame = gameRepository.findByNameAndOwnerID(game.getName(), game.getOwnerID());
            if(savedGame==null) {
                savedGame = gameRepository.save(game);
                game.setMinutes_played_today(game.getPlaytime_weeks());
            } else {
                game.setMinutes_played_today(game.getPlaytime_forever()-savedGame.getPlaytime_forever());
            }
            if(game.getMinutes_played_today()>0) gameArray.add(game);
        }
    }
    
    @PostConstruct
    public void updateGamesOnLoad(){
        logger.info("Updating database with link " + link);
        List<String> userArray = gameRepository.findAllUsers();
        if(userArray==null || userArray.size()<1) {
            logger.error("No users were found in the database");
            return;
        }
        Game existingGame;
        for (String userID : userArray) {
            for (Game game : parseApiForUserId(userID)) {
                game.setPrevious_time(game.getPlaytime_forever());
                existingGame = gameRepository.findByNameAndOwnerID(game.getName(), game.getOwnerID());
                if(existingGame!=null){
                    existingGame.setPrevious_time(existingGame.getPlaytime_forever());
                    existingGame.setPlaytime_forever(game.getPlaytime_forever());
                    gameRepository.save(existingGame);
                }else {
                    game.setMinutes_played_today(game.getPlaytime_forever()-game.getPlaytime_weeks());
                    gameRepository.save(game);
                }
            }   
        }
    }
    public List<Game> parseApiForUserId(String userID){
        List<Game> returnList = new ArrayList<>();
        String apiresponse = checkAPI(userID);
        JSONObject jsonResponse = new JSONObject(apiresponse.toString());
        JSONObject jsonObject = jsonResponse.getJSONObject("response");
        JSONArray jsonArray = jsonObject.getJSONArray("games");
        Game game;
        for (int i = 0; i < jsonArray.length(); i++) {
            game = new Game();
            jsonObject = jsonArray.getJSONObject(i);
            game.setName(jsonObject.getString("name"));
            game.setPlaytime_forever(jsonObject.getInt("playtime_forever"));
            game.setPlaytime_weeks(jsonObject.getInt("playtime_2weeks"));
            game.setAppid(jsonObject.getLong("appid"));
            game.setOwnerID(userID);
            returnList.add(game);
        }
        return returnList;
    }
    public String checkAPI(String userID){
        link = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?count=10&key="+key+"&steamid="+userID;
        logger.info("Checking API with provided link:" + link);
        String steamJSON="";
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
              sb.append(line);
            }
            rd.close();
            steamJSON = sb.toString();
        } catch (Exception e) {
            logger.error("Error occurred: "+e.getMessage());
       }
       return steamJSON;
    }
}
