package com.example.sisyphus;
import java.io.Serializable;

public class Habit implements Serializable {
    private String title;
    private String reason;
    private String date;

    public Habit(){
    }

    public Habit(String title,String date,String reason){
        this.title = title;
        this.reason = reason;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
