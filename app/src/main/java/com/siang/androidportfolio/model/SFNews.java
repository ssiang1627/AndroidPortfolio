package com.siang.androidportfolio.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SFNews {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("newsSite")
    @Expose
    private String newsSite;

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNewsSite() {
        return newsSite;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setNewsSite(String newsSite) {
        this.newsSite = newsSite;
    }

    @Override
    public String toString() {
        return "SFNews{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", newsSite='" + newsSite + '\'' +
                '}';
    }


}
