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

    @Column(name = "app_id", unique = true, nullable = false)
    private long appid;

    @Column(name = "game_name", unique = true, nullable = false)
    private String name;
    
    @Column(name = "previous_time_played",  nullable = true)
    private int previousTime;
    
    @Column(name = "playtime_forever",  nullable = true)
    private int playtime_forever;

    @Column(name = "playtime_weeks",  nullable = true)
    private int playtime_weeks;

    public Game(String name, int previousTime, int playtime_forever){
        this.name = name;
        this.previousTime = previousTime;
        this.playtime_forever = playtime_forever;
    }

    public Game(){
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setPreviousTime(int previousTime) {
        this.previousTime = previousTime;
    }
    public int getPreviousTime() {
        return previousTime;
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
        return "[ Name: " +name + "; App ID: "+ appid + "; Playtime total: "+ playtime_forever + "; Playtime 2 weeks: "+ playtime_weeks + " ]";
    }
}
