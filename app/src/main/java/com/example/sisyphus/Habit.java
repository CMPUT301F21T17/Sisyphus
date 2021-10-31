package com.example.sisyphus;
import java.io.Serializable;

public class Habit implements Serializable {
    private String name;
    private String reason;
    private String date;

    public Habit(){
    }

    public Habit(String name,String date,String reason){
        this.name = name;
        this.reason = reason;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
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
