package com.example.myrssreaderapp.models;

public class ArticleItem {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;

    private int type;
    private String content;

    public ArticleItem(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}