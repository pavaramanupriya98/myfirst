package com.moraspirit.moraspiritapp.sports;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.moraspirit.moraspiritapp.points.Sport;
import com.moraspirit.moraspiritapp.points.University;

@Entity
public class PastMatch {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "date")
    @SerializedName("date")
    private String date;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    private String type;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @Embedded(prefix = "sport")
    @SerializedName("sport")
    private Sport sport;

    @Embedded(prefix = "team_a")
    @SerializedName("team_a")
    private University teamA;

    @Embedded(prefix = "team_b")
    @SerializedName("team_b")
    private University teamB;

    @ColumnInfo(name = "won")
    @SerializedName("won")
    private String won;


    public String getWon() {
        return won;
    }

    public void setWon(String won) {
        this.won = won;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public University getTeamA() {
        return teamA;
    }

    public void setTeamA(University teamA) {
        this.teamA = teamA;
    }

    public University getTeamB() {
        return teamB;
    }

    public void setTeamB(University teamB) {
        this.teamB = teamB;
    }
}
