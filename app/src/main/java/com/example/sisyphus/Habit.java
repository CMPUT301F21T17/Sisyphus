package com.example.sisyphus;

import java.util.Date;

public class Habit {

    private String name;
    private String reason;
    private int schedule;
    private Date startDate;

    public Habit(){
        //Default constructor
    }

    public Habit(String name, String reason, int schedule, Date startDate){
        this.name = name;
        this.reason = reason;
        this.schedule = schedule;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public String getReason(){
        return reason;
    }

    public int getSchedule(){
        return schedule;
    }

    public Date getStartDate(){
        return startDate;
    }
}
