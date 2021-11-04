package com.example.sisyphus;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.HabitEvent;

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

}
