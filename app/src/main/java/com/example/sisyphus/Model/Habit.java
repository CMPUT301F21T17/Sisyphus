package com.example.sisyphus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Habit implements Serializable {
    private String habitName;
    private boolean isPrivate;
    private Date startDate;
    private ArrayList<String> frequency;
    private String reason;

    public Habit() {
    }

    public Habit(String habitName,boolean isPrivate, Date startDate, ArrayList<String> frequency, String reason) {
        setHabitName(habitName);
        setPrivate(isPrivate);
        setStartDate(startDate);
        setFrequency(frequency);
        setReason(reason);
    }


    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        this.isPrivate = aPrivate;
    }

    public String getHabitName() {
        return habitName;
    }

    public ArrayList<String> getFrequency() {
        return frequency;
    }

    public void setFrequency(ArrayList<String> frequency) {
        this.frequency = frequency;
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
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}


