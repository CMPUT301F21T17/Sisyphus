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

    /**
     * Creates a mock habit for testing.
     * @return
     *      A habit with habit name "Sleeping", the current system date, a frequency of Tuesdays and Wednesdays,
     *      and reason "tired."
     */
    private Habit mockHabit(){
        Habit habit = new Habit();
        habit.setHabitName("Sleeping");
        habit.setStartDate(new Date());
        ArrayList<String> frequency = new ArrayList<>();
        frequency.add("TUESDAY");
        frequency.add("WEDNESDAY");
        habit.setFrequency(frequency);
        habit.setReason("Tired.");
        habit.setPrivate(true);
        habit.setPosition(1);
        return habit;
    }

    /**
     * Tests that the getHabitName correctly returns the name of the habit.
     */
    @Test
    public void testGetHabitName(){
        assertEquals(mockHabit().getHabitName(), "Sleeping");
    }

    /**
     * Tests that getFrequency correctly returns the frequency of the habit in ArrayList<String>
     *     format.
     */
    @Test
    public void testGetFrequency(){
        ArrayList<String> frequency = mockHabit().getFrequency();
        assertEquals(frequency.size(), 2);
        assertEquals(frequency.get(0), "TUESDAY");
        assertEquals(frequency.get(1), "WEDNESDAY");
    }

    /**
     * Tests whether setFrequency correctly replaces the current frequency of a given habit with a new
     * frequency array.
     */
    @Test
    public void testSetFrequency(){
        Habit habit = mockHabit();
        ArrayList<String> newFrequency = new ArrayList<>();
        newFrequency.add("MONDAY");
        newFrequency.add("FRIDAY");
        habit.setFrequency(newFrequency);
        ArrayList<String> fetchedFrequency = habit.getFrequency();
        assertEquals(newFrequency.get(0), fetchedFrequency.get(0));
        assertEquals(newFrequency.get(1), fetchedFrequency.get(1));
    }

    /**
     * Tests whether setHabitName correctly changes the name of a habit
     */
    @Test
    public void testSetHabitName(){
        String newName = "Wake up";
        Habit habit = mockHabit();
        habit.setHabitName(newName);
        assertEquals(habit.getHabitName(), newName);
    }

    /**
     * tests whether getStartDate correctly retrieves the start date of a habit.
     */
    @Test
    public void testGetStartDate(){
        Habit habit = mockHabit();
        Date currentDate = new Date();
        assertEquals(habit.getStartDate().toString(), currentDate.toString());
    }

    /**
     * Tests whether setStartDate correctly changes the current start date of a habit to another.
     */
    @Test
    public void testSetStartDate(){
        Habit habit = mockHabit();
        Date newDate = new Date(21, 3, 4);
        habit.setStartDate(newDate);
        assertEquals(habit.getStartDate().toString(), newDate.toString());
    }

    /**
     * Tests whether getReason fetches the reason string correctly from the habit.
     */
    @Test
    public void testGetReason(){
        Habit habit = mockHabit();
        assertEquals(habit.getReason(), "Tired.");
    }

    /**
     * Tests whether setReason correctly changes the reason for a habit.
      */
    @Test
    public void testSetReason(){
        Habit habit = mockHabit();
        String newReason = "Sleepy.";
        habit.setReason(newReason);
        assertEquals(habit.getReason(), newReason);
    }

    /**
     * Tests whether the function correctly gets the current position
     */
    @Test
    public void testGetPosition(){
        Habit habit = mockHabit();
        assertEquals(habit.getPosition(), 1);
    }

    /**
     * Tests whether the function correctly updates the current position
     */
    @Test
    public void testSetPosition(){
        Habit habit = mockHabit();
        habit.setPosition(500);
        assertEquals(habit.getPosition(), 500);
    }

    /**
     * Tests whether the function correctly gets the current privacy status
     */
    @Test
    public void testGetPrivacy(){
        Habit habit = mockHabit();
        assertEquals(habit.isPrivate(), true);
    }

    /**
     * Tests whether the function correctly updates the current privacy status
     */
    @Test
    public void testSetPrivacy(){
        Habit habit = mockHabit();
        habit.setPrivate(false);
        assertEquals(habit.isPrivate(), false);
    }

}
