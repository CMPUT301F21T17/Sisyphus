package com.example.sisyphus;

import android.text.Editable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

public class Habit implements Serializable {
    private String name;
    private String reason;
    private Date date;
    private long dateSort;
    private List<String> daysRepeated;

    public Habit() {
    }

    public Habit(String name, Date date, String reason, List<String> daysRepeated) {
        this.name = name;
        this.reason = reason;
        this.date = date;
        this.dateSort = date.getTime();
        this.daysRepeated = daysRepeated;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public Date getDate() {
        return this.date;
    }

    public long getDateSort() {
        return dateSort;
    }

    public void setDateSort(long dateSort) {
        this.dateSort = dateSort;
    }

    public List<String> getDaysRepeated() {
        return daysRepeated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDate(Date date) {
        this.date = date;
        this.dateSort = date.getTime();
    }


    public void setDaysRepeated(List<String> daysRepeated) {
        this.daysRepeated = daysRepeated;
    }
}