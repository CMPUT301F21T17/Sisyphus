package com.example.sisyphus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Habit implements Serializable {
    private String habitName;
    private Date startDate;
    private ArrayList<String> frequency;
    private String reason;

    public Habit() {
    }

    public Habit(String habitName, Date startDate, ArrayList<String> frequency, String reason) {
        setHabitName(habitName);
        setStartDate(startDate);
        setFrequency(frequency);
        setReason(reason);
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


