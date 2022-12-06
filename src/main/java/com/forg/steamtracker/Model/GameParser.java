package com.forg.steamtracker.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

@Service
@Configurable
public class GameParser {

    private String link;

    @Autowired
    GameRepository gameRepository;

    public void parse(String count, String userID, String key){
        link = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?count="+count+"&key="+key+"&steamid="+userID;
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
                gameRepository.save(game);
                System.out.println(game.toString());
                // System.out.println(jsonObject.toString());
            }
           
        } catch (Exception e) {
            System.out.println("Error occurred: "+e.getMessage());;
        }
        //System.out.println(link);
    }
}
