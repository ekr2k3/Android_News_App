package com.example.myrssreaderapp.models;

public class Comment {

    private int idComment;
    private String urlArticle;
    private String email;
    private String content;

    public Comment() {}

    public Comment(int idComment, String urlArticle, String email, String content) {
        this.idComment = idComment;
        this.urlArticle = urlArticle;
        this.email = email;
        this.content = content;
    }

    public Comment(String urlArticle, String email, String content) {
        this.urlArticle = urlArticle;
        this.email = email;
        this.content = content;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getUrlArticle() {
        return urlArticle;
    }

    public void setUrlArticle(String urlArticle) {
        this.urlArticle = urlArticle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
