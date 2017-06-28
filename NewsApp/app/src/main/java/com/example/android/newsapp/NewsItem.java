package com.example.android.newsapp;

/**
 * Created by Haiyan on 6/27/17.
 */

public class NewsItem {
    private String title;
    private String author;
    private String description;
    private String url;
    private String url_image;
    private String timestamp;

    public NewsItem(String title, String description, String url, String timestamp ){
        this.title = title;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
