package com.example.mughees.chat_app.Model;

public class User {
    private String id;
    private String User_name;
    private String imageUrl;
    private String status;
    private String search;

    public User() {
    }

    public User(String id, String user_name, String imageUrl,String status,String search) {
        this.id = id;
        User_name = user_name;
        this.imageUrl = imageUrl;
        this.status = status;
        this.search = search;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
