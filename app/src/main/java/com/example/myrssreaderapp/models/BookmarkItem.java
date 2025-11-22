package com.example.myrssreaderapp.models;

public class BookmarkItem {
    private String url;
    private int userId;

    public BookmarkItem(String url, int userId) {
        this.url = url;
        this.userId = userId;
    }

    public String getUrl() { return url; }
    public int getUserId() { return userId; }
    public void setUrl(String url) { this.url = url; }
    public void setUserId(int userId) { this.userId = userId; }
}

