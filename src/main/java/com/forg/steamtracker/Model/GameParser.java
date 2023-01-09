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
    //@Value("${steam.key}")
    public String key = "E263C1AAD903AFDD9DA35B8DEA8C1638";
    private String steamJSON;
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
            data = out.toByteArray();
            return new String("{\"games\":"+ new String(data) + "}");
        } catch (IOException e) {
            logger.error("Error occurred: "+e.getMessage());
        }
        return "";
    }
   
    public void checkGames(String userID){
        logger.info("Checking games with provided user ID:" + userID);
        checkAPI(userID);
        JSONObject jsonResponse = new JSONObject(steamJSON.toString());
        JSONObject jsonObject = jsonResponse.getJSONObject("response");
        JSONArray jsonArray = jsonObject.getJSONArray("games");
        Game savedGame;
        Game jsonGame;
        Game returnGame;
        ArrayList<Game> tempGameArray = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonGame = new Game();
            jsonObject = jsonArray.getJSONObject(i);
            jsonGame.setName(jsonObject.getString("name"));
            jsonGame.setPlaytime_forever(jsonObject.getInt("playtime_forever"));
            jsonGame.setPlaytime_weeks(jsonObject.getInt("playtime_2weeks"));
            jsonGame.setAppid(jsonObject.getLong("appid"));
            jsonGame.setOwnerID(userID);
            tempGameArray.add(jsonGame);
        }
        for (Game game : tempGameArray) {
            savedGame = gameRepository.findByNameAndOwnerID(game.getName(), game.getOwnerID());
            returnGame  = new Game();
            returnGame.setName(game.getName());
            if(savedGame==null) {
                savedGame = gameRepository.save(game);
                returnGame.setMinutes_played_today(game.getPlaytime_weeks());
            }
            if(returnGame.getMinutes_played_today()>0) gameArray.add(returnGame);
        }
    }
    public String checkAPI(String userID){
        link = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?count=10&key="+key+"&steamid="+userID;
        logger.info("Checking API with provided link:" + link);
        steamJSON="";
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
    @PostConstruct
    public void updateGames(){
        logger.info("Updating database with link " + link);
        List<String> userArray = gameRepository.findAllUsers();
        if(userArray==null || userArray.size()<1) {
            logger.error("No users were found in the database");
            return;
        }
        for (String userID : userArray) {
            checkAPI(userID);
            JSONObject jsonResponse = new JSONObject(steamJSON.toString());
            JSONObject jsonObject = jsonResponse.getJSONObject("response");
            JSONArray jsonArray = jsonObject.getJSONArray("games");
            Game game;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                game = new Game();
                game.setName(jsonObject.getString("name"));
                game.setAppid(jsonObject.getLong("appid"));
                game.setPlaytime_forever(jsonObject.getInt("playtime_forever"));
                game.setPlaytime_weeks(jsonObject.getInt("playtime_2weeks"));
                game.setPrevious_time(game.getPlaytime_forever());
                game.setOwnerID(userID);
                Game existingGame = gameRepository.findByNameAndOwnerID(game.getName(), game.getOwnerID());
                if(existingGame!=null){
                    existingGame.setPrevious_time(existingGame.getPlaytime_forever());
                    existingGame.setPlaytime_forever(game.getPlaytime_forever());
                    existingGame.setOwnerID(userID);
                    existingGame.setMinutes_played_today(existingGame.getPlaytime_forever() - existingGame.getPrevious_time());
                    game = existingGame;
                }else {
                    game.setMinutes_played_today(game.getPlaytime_forever()-game.getPlaytime_weeks());
                }
                gameRepository.save(game);
            }   
        }
    }
}
