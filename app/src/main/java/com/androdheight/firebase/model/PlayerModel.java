package com.androdheight.firebase.model;

import java.io.Serializable;

/**
 * Created by prabhakar on 11/04/17.
 */

public class PlayerModel  implements Serializable{
    private  String playerImage;
    private  String parentid;
    private  int p_id;
    private String player_name;
    private String player_match;
    private String player_inning;
    private String player_run;
    private String player_wicket;
    private String player_bestRun;
    private String player_bestBowling;
    private String average;


    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getPlayer_match() {
        return player_match;
    }

    public void setPlayer_match(String player_match) {
        this.player_match = player_match;
    }

    public String getPlayer_inning() {
        return player_inning;
    }

    public void setPlayer_inning(String player_inning) {
        this.player_inning = player_inning;
    }

    public String getPlayer_run() {
        return player_run;
    }

    public void setPlayer_run(String player_run) {
        this.player_run = player_run;
    }

    public String getPlayer_wicket() {
        return player_wicket;
    }

    public void setPlayer_wicket(String player_wicket) {
        this.player_wicket = player_wicket;
    }

    public String getPlayer_bestRun() {
        return player_bestRun;
    }

    public void setPlayer_bestRun(String player_bestRun) {
        this.player_bestRun = player_bestRun;
    }

    public String getPlayer_bestBowling() {
        return player_bestBowling;
    }

    public void setPlayer_bestBowling(String player_bestBowling) {
        this.player_bestBowling = player_bestBowling;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(String playerImage) {
        this.playerImage = playerImage;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
}
