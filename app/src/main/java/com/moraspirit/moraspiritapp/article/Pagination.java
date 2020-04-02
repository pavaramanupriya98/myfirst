package com.moraspirit.moraspiritapp.article;

import com.google.gson.annotations.SerializedName;
public class Pagination {

    @SerializedName("object_count")
    private int objectCount;

    @SerializedName("page_number")
    private String pageNumber;

    @SerializedName("page_size")
    private int pageSize;

    @SerializedName("page_count")
    private int pageCount;

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
