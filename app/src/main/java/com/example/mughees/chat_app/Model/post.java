package com.example.mughees.chat_app.Model;

public class post {
    String title;
    String description;

    public post(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public post() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
