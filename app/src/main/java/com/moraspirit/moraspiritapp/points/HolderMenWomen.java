package com.moraspirit.moraspiritapp.points;

import com.google.gson.annotations.SerializedName;
import com.moraspirit.moraspiritapp.points.Sport;
import com.moraspirit.moraspiritapp.points.University;

public class HolderMenWomen {
    @SerializedName("type")
    private String type;

    @SerializedName("university")
    private University university;

    @SerializedName("sport")
    private Sport sport;

    @SerializedName("id")
    private int id;

    @SerializedName("points")
    private float points;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }
}
