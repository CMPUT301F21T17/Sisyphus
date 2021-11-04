package com.example.sisyphus;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.sisyphus.Model.Habit;

import java.util.ArrayList;
import java.util.Date;

/**
 * A test class to test the 'Habit' class.
 */
public class HabitClassTest {

    private Habit mockHabit(){
        Habit habit = new Habit();
        habit.setHabitName("Sleeping");
        habit.setStartDate(new Date());
        ArrayList<String> frequency = new ArrayList<>();
        frequency.add("TUESDAY");
        frequency.add("WEDNESDAY");
        habit.setFrequency(frequency);
        habit.setReason("Tired.");
        return habit;
    }

    @Test
    public void testGetHabitName(){
        assertEquals(mockHabit().getHabitName(), "Sleeping");
    }

    @Test
    public void testGetFrequency(){
        ArrayList<String> frequency = mockHabit().getFrequency();
        assertEquals(frequency.get(0), "TUESDAY");
        assertEquals(frequency.get(1), "WEDNESDAY");
    }



}
