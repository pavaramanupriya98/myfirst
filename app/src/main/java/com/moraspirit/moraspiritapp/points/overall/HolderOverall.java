package com.moraspirit.moraspiritapp.points.overall;

import com.google.gson.annotations.SerializedName;
import com.moraspirit.moraspiritapp.points.University;

public class HolderOverall {
    @SerializedName("university")
    private University university;

    @SerializedName("rank")
    private int rank;

    @SerializedName("points")
    private float points;

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }
}
