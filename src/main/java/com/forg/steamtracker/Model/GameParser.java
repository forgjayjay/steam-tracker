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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Configurable
public class GameParser {

    private String link;

    @Autowired
    GameRepository gameRepository;

    public String parse(String userID, String key){
        /*if(!updated)*/ update(userID, key);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        byte[] data;
        try {
            List<Game> games = gameRepository.findAll();
            List<Game> gamesToReturn = new ArrayList<>();

            for (Game game : games) {
                if(game.getOwnerID().equals(userID)) {
                    gamesToReturn.add(game);
                }
            }
            mapper.writeValue(out, gamesToReturn);
            data = out.toByteArray();
            return new String(data);
        } catch (IOException e) {
            System.out.println("Error occurred: "+e.getMessage());
        }
        // for (Game game : gameRepository.findAll()) {
        //     try {
        //         mapper.writeValueAsString(game);
        //     } catch (JsonProcessingException e) {
        //         System.out.println("Error occurred: "+e.getMessage());
        //     }
        // }
        //System.out.println(link);
        return "";
    }

    public void update(String userID, String key){
        System.out.println("Updating database . . .");
        link = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?count=10&key="+key+"&steamid="+userID;
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
              sb.append(line);
            }
            rd.close();
            //System.out.println(sb.toString());
            JSONObject jsonResponse = new JSONObject(sb.toString());
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
                game.setPreviousTime(game.getPlaytime_forever());
                game.setOwnerID(userID);
                Game existingGame = gameRepository.findByName(game.getName());
                if(existingGame!=null){
                    existingGame.setMinutes_played_yesterday(existingGame.getPlaytime_forever() - existingGame.getPreviousTime());
                    existingGame.setPreviousTime(existingGame.getPlaytime_forever());
                    existingGame.setPlaytime_forever(game.getPlaytime_forever());
                    existingGame.setOwnerID(userID);
                    existingGame.setMinutes_played_today(existingGame.getPlaytime_forever() - existingGame.getPreviousTime());
                    gameRepository.save(existingGame);
                }else {
                    gameRepository.save(game);
                }
                    System.out.println(game.toString());
                // System.out.println(jsonObject.toString());
            }
        } catch (Exception e) {
            System.out.println("Error occurred: "+e.getMessage());
        }
    }
}
