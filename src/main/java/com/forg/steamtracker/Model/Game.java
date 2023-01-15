package com.forg.steamtracker.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "app_id", unique = false, nullable = false)
    private long appid;

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
    public long getAppid() {
        return appid;
    }
    public void setAppid(long appid) {
        this.appid = appid;
    }
    @Override
    public String toString() {
        return "[ Name: " +name + "; App ID: "+ appid + "; Owner ID: " + ownerID + "; Playtime total: "+ playtime_forever + "; Playtime 2 weeks: "+ playtime_weeks + "; Playtime today: " + minutes_played_today+" ]";
    }
    @Override
    public boolean equals(Object obj) {
        return String.valueOf(appid).equals(String.valueOf(obj));
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
