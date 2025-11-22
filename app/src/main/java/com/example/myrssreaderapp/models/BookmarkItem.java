package com.example.myrssreaderapp.models;

public class BookmarkItem {
    private String url;
    private int userId;
    private int bookMarkId;



    public BookmarkItem(String url, int userId) {
        this.url = url;
        this.userId = userId;
    } // bookMarkId là tự tăng lên không cần khởi tạo

    public String getUrl() { return url; }
    public int getUserId() { return userId; }
    public int getBookMarkId() { return bookMarkId; }
    public void setBookMarkId(int bookMarkId) { this.bookMarkId = bookMarkId; }
    public void setUrl(String url) { this.url = url; }
    public void setUserId(int userId) { this.userId = userId; }
}

