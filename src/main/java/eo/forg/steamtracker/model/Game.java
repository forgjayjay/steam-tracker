package eo.forg.steamtracker.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "app_id", unique = false, nullable = false)
    private long app_id;

    @Column(name = "game_name", unique = false, nullable = false)
    private String name;
    
    @Column(name = "previous_time_played",  nullable = true)
    private int previous_time;
    
    @Column(name = "playtime_forever",  nullable = true)
    private int playtime_forever;

    @Column(name = "playtime_weeks",  nullable = true)
    private int playtime_weeks;

    @Column(name = "owner_id",unique = false, nullable = false)
    private String ownerID;

    @Column(name = "minutes_played_today",unique = false, nullable = true)
    private int minutes_played_today;

    @Temporal(TemporalType.DATE)
    Date previous_update_date;

    public Game(){}

    public Game(String name, int playtime_forever, int playtime_weeks, long app_id, String ownerID){
        // game.setName(jsonGame.getString("name"));
        // game.setPlaytime_forever(jsonGame.getInt("playtime_forever"));
        // game.setPlaytime_weeks(jsonGame.getInt("playtime_2weeks"));
        // game.setApp_id(jsonGame.getLong("app_id"));
        // game.setOwnerID(userID);
        this.name = name;
        this.playtime_forever = playtime_forever;
        this.playtime_weeks = playtime_weeks;
        this.app_id = app_id;
        this.ownerID = ownerID;
    }



    public String getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
    public void setMinutes_played_today(int minutes_played_today) {
        this.minutes_played_today = minutes_played_today;
    }
    public int getMinutes_played_today() {
        return minutes_played_today;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setPrevious_time(int previous_time) {
        this.previous_time = previous_time;
    }
    public int getPrevious_time() {
        return previous_time;
    }
    public void setPlaytime_forever(int playtime_forever) {
        this.playtime_forever = playtime_forever;
    }
    public int getPlaytime_forever() {
        return playtime_forever;
    }
    public int getId() {
        return id;
    }
    public void setPlaytime_weeks(int playtime_weeks) {
        this.playtime_weeks = playtime_weeks;
    }
    public int getPlaytime_weeks() {
        return playtime_weeks;
    }
    public long getApp_id() {
        return app_id;
    }
    public void setApp_id(long app_id) {
        this.app_id = app_id;
    }
    public void setPrevious_update_date(Date previous_update_date) {
        this.previous_update_date = previous_update_date;
    }
    public Date getPrevious_update_date() {
        return previous_update_date;
    }
    @Override
    public String toString() {
        return "[ Name: " +name + "; App ID: "+ app_id + "; Owner ID: " + ownerID + "; Playtime total: "+ playtime_forever + " Previous playtime total: "+ previous_time + "; Playtime 2 weeks: "+ playtime_weeks + "; Playtime today: " + minutes_played_today+" ]";
    }
    @Override
    public boolean equals(Object obj) {
        return String.valueOf(app_id).equals(String.valueOf(((Game) obj).getApp_id())) && ownerID.equals(((Game) obj).getOwnerID());
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
