package com.moraspirit.moraspiritapp.year;

import com.google.gson.annotations.SerializedName;

public class Year {

    @SerializedName("id")
    private int id;

    @SerializedName("year")
    private String year;

    @SerializedName("active")
    private String active;

    @SerializedName("slug")
    private String slug;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
