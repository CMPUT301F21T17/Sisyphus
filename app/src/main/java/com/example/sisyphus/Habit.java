package com.example.sisyphus;

import android.text.Editable;

import java.util.ArrayList;

public class Habit {
    private String habitName;
    private Editable startDate;
    private ArrayList<String> frequency;
    private String reason;


    public Habit(String habitName, Editable startDate, ArrayList<String> frequency, String reason) {
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

    public Editable getStartDate() {
        return startDate;
    }

    public void setStartDate(Editable startDate) {
        this.startDate = startDate;

    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}


