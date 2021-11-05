package com.example.sisyphus.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Individual Habit Event class
 */
public class HabitEvent implements Serializable {
    private Date date;
    private String location;
    private String comment;
    private String habitName;

    public HabitEvent(){}

    /**
     * Constructor to create a habit event
     * @param date
     *  date of habit event
     * @param location
     *  location of habit event
     * @param comment
     *  comment of habit event
     * @param habitName
     *  name of habit event
     */
    public HabitEvent(Date date, String location, String comment, String habitName) {
        this.date = date;
        this.location = location;
        this.comment = comment;
        this.habitName = habitName;
    }

    /**
     * function to get habit event date
     * @return
     *  habit event date
     */
    public Date getDate() {
        return date;
    }

    /**
     * function to set habit event date
     * @param date
     *  habit event date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * function to get habit event location
     * @return
     *  habit event location
     */
    public String getLocation() {
        return location;
    }

    /**
     * function to set habit event location
     * @param location
     *  habit event location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * function to get habit event comment
     * @return
     *  habit event comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * function to set habit event comment
     * @param comment
     *  habit event comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * function to get habit event name
     * @return
     *  habit event name
     */
    public String getHabitName() {
        return habitName;
    }

    /**
     * function to set habit event name
     * @param habitName
     *  habit event name to set
     */
    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }
}
