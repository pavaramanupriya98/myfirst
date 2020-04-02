package com.moraspirit.moraspiritapp.api;

import com.google.gson.annotations.SerializedName;
import com.moraspirit.moraspiritapp.article.Pagination;

import java.util.ArrayList;

public class ApiData<T> {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private ArrayList<T> data;

    @SerializedName("pagination")
    private Pagination pagination;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
