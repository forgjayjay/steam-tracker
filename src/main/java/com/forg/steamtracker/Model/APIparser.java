package com.forg.steamtracker.Model;
import java.io.BufferedReader;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class APIparser{
    @Value("${steam.key}") public String key;
    private Logger logger = LoggerFactory.getLogger(APIparser.class);
    
    public List<Game> parseAPIForUserId(String userID){
        List<Game> returnList = new ArrayList<>();
        String APIresponse = checkAPI(userID);
        if(APIresponse != ""){
            JSONArray jsonArray = new JSONObject(APIresponse.toString()).getJSONObject("response").getJSONArray("games");
            Game game;
            JSONObject jsonGame;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonGame = jsonArray.getJSONObject(i);
                game = new Game(
                    jsonGame.getString("name"),
                    jsonGame.getInt("playtime_forever"),
                    jsonGame.getInt("playtime_2weeks"),
                    jsonGame.getLong("appid"),
                    userID
                );
                logger.debug(game.toString());
                returnList.add(game);
            }
        }
        return returnList;
    }

    private String checkAPI(String userID){
        final String link = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?count=10&key="+key+"&steamid="+userID;
        logger.info("Checking API");
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) { 
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                }
            } else {
                logger.error("API returned error: " + conn.getResponseCode() + " " + conn.getResponseMessage());
            }
        } catch (IOException e) {
            logger.error("Error occurred: "+e.getMessage());
        }
        return sb.toString();
    }
}