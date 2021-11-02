package com.example.sisyphus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Habit implements Serializable {
    private String habitName;
    private Date startDate;
    private ArrayList<String> frequency;
    private String reason;

    public Habit(){

    }


    public Habit(String habitName, Date startDate, ArrayList<String> frequency, String reason) {
        this.habitName = habitName;
        this.startDate = startDate;
        this.frequency = frequency;
        this.reason = reason;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ArrayList<String> getFrequency() {
        return frequency;
    }

    public void setFrequency(ArrayList<String> frequency) {
        this.frequency = frequency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}


