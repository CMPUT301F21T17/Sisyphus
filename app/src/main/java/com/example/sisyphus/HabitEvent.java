package com.example.sisyphus;

import java.util.Date;

public class HabitEvent {

    private String message;
    private Date eventDate;


    public HabitEvent(){
        //Default constructor
    }

    public HabitEvent(String message, Date eventDate){
        this.message = message;
        this.eventDate = eventDate;
    }

    public String getMessage(){
        return message;
    }

    public Date getDate(){
        return eventDate;
    }

}
