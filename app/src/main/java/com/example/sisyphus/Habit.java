package com.example.sisyphus;

import android.text.Editable;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;

public class Habit implements Serializable {
    private String name;
    private String reason;
    private Calendar date;
    private EnumSet<DayOfWeek> daysRepeated;

    public Habit() {
    }

    public Habit(String name, Calendar date, String reason, EnumSet<DayOfWeek> daysRepeated) {
        this.name = name;
        this.reason = reason;
        this.date = date;
        this.daysRepeated = daysRepeated;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        String result = date.getDisplayName(Calendar.YEAR, Calendar.ALL_STYLES, Locale.CANADA);
        result += "-";
        result += date.getDisplayName(Calendar.MONTH, Calendar.ALL_STYLES, Locale.CANADA);
        result += "-";
        result += date.getDisplayName(Calendar.DATE, Calendar.ALL_STYLES, Locale.CANADA);
        return result;
    }

    public EnumSet<DayOfWeek> getDaysRepeated() {
        return daysRepeated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setDaysRepeated(EnumSet<DayOfWeek> daysRepeated) {
        this.daysRepeated = daysRepeated;
    }
}