package com.example.myrssreaderapp.models;

public class BookmarkItem {
    private String url;
    private int userId;
    private int bookMarkId;
    private String title;
    private String imageUrl;
    private String description;

    public BookmarkItem(String url, int userId, String title, String imageUrl, String description) {
        this.url = url;
        this.userId = userId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;

    } // bookMarkId là tự tăng lên không cần khởi tạo

    public String getUrl() { return url; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public int getBookMarkId() { return bookMarkId; }
    public void setBookMarkId(int bookMarkId) { this.bookMarkId = bookMarkId; }
    public void setUrl(String url) { this.url = url; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
}

