package com.example.taskmaster;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo
    String title;
    @ColumnInfo
    String body;
    @ColumnInfo
    String state;
    @ColumnInfo
    String picTask;

    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    @Ignore
    public Task(String title, String body, String state, String picTask) {
        this.title = title;
        this.body = body;
        this.state = state;
        this.picTask = picTask;
    }

    public String getTitle() {
        return title;
    }

    public String getPicTask() {
        return picTask;
    }

    public String getBody() {
        return body;
    }

    public String getState() {
        return state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setId(Long id) {

        this.id = id;

    }


}


