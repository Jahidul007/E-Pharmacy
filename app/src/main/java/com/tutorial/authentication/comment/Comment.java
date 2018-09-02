package com.tutorial.authentication.comment;

import java.util.Date;

public class Comment {

    String id;
    String user;
    String message;
    String date;

    public Comment() {
    }

    public Comment(String id, String user, String message,String date) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}
