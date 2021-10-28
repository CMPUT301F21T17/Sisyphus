package com.example.sisyphus;

public class Habit {
    private String title;
    private String reason;
    private String date;

    public Habit(String title,String date,String reason){
        this.title = title;
        this.reason = reason;
        this.date = date;
    }

    String getHabitTitle(){ return this.title; }

    String getHabitReason(){ return this.reason; }

    String getHabitDate(){return this.date; }
}
