package com.example.sisyphus;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.HabitEvent;
import com.google.common.hash.HashingInputStream;

import java.util.ArrayList;
import java.util.Date;

/**
 * A test class to test the 'HabitEvent' class.
 */
public class HabitEventClassTest {

    /**\
     * Creates a mock habitEvent object for testing.
     * @return
     *      A habit event for the habit named "Sleeping", with the current system date, the comment
     *      "Slept for 8 hours", and location "Edmonton"
     */
    private HabitEvent mockHabitEvent(){
        HabitEvent habitEvent = new HabitEvent();
        habitEvent.setHabitName("Sleeping");
        habitEvent.setDate(new Date());
        habitEvent.setComment("Slept for 8 hours.");
        habitEvent.setLocation("Edmonton");
        return habitEvent;
    }

    /**
     * Tests whether getDate returns the correct date.
     */
    @Test
    public void testGetDate(){
        HabitEvent habitEvent = mockHabitEvent();
        Date currentDate = new Date();
        assertEquals(habitEvent.getDate().toString(), currentDate.toString());
    }

    /**
     * Tests whether setDate correctly assigns a new date to the habit event.
     */
    @Test
    public void testSetDate(){
        HabitEvent habitEvent = mockHabitEvent();
        Date newDate = new Date(21, 10, 3);
        habitEvent.setDate(newDate);
        assertEquals(habitEvent.getDate().toString(), newDate.toString());
    }

    /**
     * Tests whether getLocation correctly fetches the location of the habit event.
     */
    @Test
    public void testGetLocation(){
        HabitEvent habitEvent = mockHabitEvent();
        assertEquals(habitEvent.getLocation(), "Edmonton");
    }

    /**
     * Tests whether setLocation correctly changes the location of a habit event.
     */
    @Test
    public void testSetLocation(){
        HabitEvent habitEvent = mockHabitEvent();
        String newLocation = "Calgary";
        habitEvent.setLocation(newLocation);
        assertEquals(habitEvent.getLocation(), newLocation);
    }

    /**
     * Tests whether getComment correctly fetches the comment for a habit event.
     */
    @Test
    public void testGetComment(){
        HabitEvent habitEvent = mockHabitEvent();
        assertEquals(habitEvent.getComment(), "Slept for 8 hours.");
    }

    /**
     * Tests whether setComment correctly changes the comment for a habit event.
     */
    @Test
    public void testSetComment(){
        HabitEvent habitEvent = mockHabitEvent();
        String newComment = "Didn't sleep well.";
        habitEvent.setComment(newComment);
        assertEquals(habitEvent.getComment(), newComment);
    }

    /**
     * Tests whether getHabitName correctly fetches the habit name for a habit event.
     */
    @Test
    public void testGetHabitName(){
        HabitEvent habitEvent = mockHabitEvent();
        assertEquals(habitEvent.getHabitName(), "Sleeping");
    }

    /**
     * Tests whether setHabitName correctly changes the habit name for a habit event.
     */
    @Test
    public void testSetHabitName(){
        HabitEvent habitEvent = mockHabitEvent();
        String newHabitName = "Walking";
        habitEvent.setHabitName(newHabitName);
        assertEquals(habitEvent.getHabitName(), newHabitName);
    }

}
