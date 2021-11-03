package com.example.sisyphus;

import java.io.Serializable;
import java.util.Date;

public class HabitEvent implements Serializable {
    private Date date;
    private String location;
    private String comment;
    private String habitName;

    public HabitEvent(){}

    public HabitEvent(Date date, String location, String comment, String habitName) {
        this.date = date;
        this.location = location;
        this.comment = comment;
        this.habitName = habitName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }
}
