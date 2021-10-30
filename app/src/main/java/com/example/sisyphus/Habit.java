package com.example.sisyphus;

import java.util.Calendar;
import java.util.EnumSet;

public class Habit {
    private EnumSet<DaysRepeated> daysRepeated;
    private Calendar dateStarted;
    private String name;

    public Habit(Calendar dateStarted, EnumSet<DaysRepeated> daysRepeated) {
        this.dateStarted = dateStarted;
        this.daysRepeated = daysRepeated;
    }

    public String getName() {
        return name;
    }

    public EnumSet<DaysRepeated> getDaysRepeated() {
        return daysRepeated;
    }

    public Calendar getDateStarted() {
        return this.dateStarted;
    }
}
