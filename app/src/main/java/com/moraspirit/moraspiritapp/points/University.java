package com.moraspirit.moraspiritapp.points;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class University {
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "image")
    @SerializedName("image")
    private String image;

    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "marks")
    @SerializedName("marks")
    private String marks;

    public University(String name, int id, String image){
        this.name = name;
        this.id = id;
        this.image = image;
        this.marks= "";
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
