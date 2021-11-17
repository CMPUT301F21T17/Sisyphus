/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Individual Habit class
 */
public class Habit implements Serializable {
    private String habitName;
    private Date startDate;
    private ArrayList<String> frequency;
    private String reason;
    private int position;

    public Habit() {
    }

    /**
     * Constructor to create a habit
     * @param habitName
     *  Name of habit to be added
     * @param startDate
     *  start date of habit to be added
     * @param frequency
     *  frequency habit is repeated
     * @param reason
     *  reason for habit
     */
    public Habit(String habitName, Date startDate, ArrayList<String> frequency, String reason) {
        setHabitName(habitName);
        setStartDate(startDate);
        setFrequency(frequency);
        setReason(reason);
    }
    
    /**
     * function to get Habit name
     * @return
     *  name of habit
     */
    public String getHabitName() {
        return habitName;
    }

    /**
     * function to get Habit frequency
     * @return
     *  habit frequency
     */
    public ArrayList<String> getFrequency() {
        return frequency;
    }

    /**
     * function to set habit frequency
     * @param frequency
     *  frequency to set habit to
     */
    public void setFrequency(ArrayList<String> frequency) {
        this.frequency = frequency;
    }

    /**
     * function to set a habit name
     * @param habitName
     *  new name to set for habit
     */
    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    /**
     * functon to get habit start date
     * @return
     *  habit start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * function to set habit start date
     * @param startDate
     *  start date to set habit
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * function to get habit reason
     * @return
     *  reason of habit
     */
    public String getReason() {
        return reason;
    }

    /**
     * function to set habit reason
     * @param reason
     *  reason to set habit
     */
    public void setReason(String reason) {
        this.reason = reason;
    }


    /**
     * function to get the position of the Habit
     * @return
     *  position of habit
     */
    public int getPosition() {
        return position;
    }

    /**
     * function to set the position of the Habit
     * @param position
     *  the position of the habit to set
     */
    public void setPosition(int position) {
        this.position = position;
    }
}


