package com.moraspirit.moraspiritapp.sports;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.moraspirit.moraspiritapp.points.Sport;
import com.moraspirit.moraspiritapp.points.University;

@Entity
public class Match {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "date")
    @SerializedName("date")
    private String date;

    @ColumnInfo(name = "time")
    @SerializedName("time")
    private String time;

    @ColumnInfo(name = "venue")
    @SerializedName("venue")
    private String venue;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    private String type;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "match_type")
    @SerializedName("match_type")
    private String matchType;

    @Embedded(prefix = "sport")
    @SerializedName("sport")
    private Sport sport;

    @Embedded(prefix = "team_a")
    @SerializedName("team_a")
    private University teamA;

    @Embedded(prefix = "team_b")
    @SerializedName("team_b")
    private University teamB;



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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
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
