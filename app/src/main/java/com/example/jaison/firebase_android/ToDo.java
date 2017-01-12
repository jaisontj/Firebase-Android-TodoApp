package com.example.jaison.firebase_android;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by jaison on 11/01/17.
 */

public class ToDo {

    private String description;
    private String created_at;
    private Boolean is_complete;

    public ToDo() {

    }

    public ToDo(String description) {
        this.description = description;
        this.created_at = new Date().toString();
        this.is_complete = false;
    }

    public void toggleCompletion() {
        this.is_complete = !is_complete;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Boolean getIs_complete() {
        return is_complete;
    }

    private HashMap<String,Object> toMapObject() {
        HashMap<String,Object> todo =  new HashMap<>();
        todo.put("description", description);
        todo.put("created_at", created_at);
        todo.put("is_complete", is_complete);
        return todo;
    }

    public HashMap<String,Object> toFirebaseObject(String key) {
        HashMap<String,Object> todo =  new HashMap<>();
        todo.put(key,toMapObject());
        return todo;
    }
}
