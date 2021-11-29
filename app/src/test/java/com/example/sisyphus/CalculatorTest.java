/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */



package com.example.sisyphus;


import static org.junit.Assert.assertEquals;

import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.User;
import com.example.sisyphus.Model.habitFollowCalculator;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class designed to test the functionality of the habitFollowCalculator class.
 * Dates may theoretically break at some point, but should remain valid for several thousand years
 */
public class CalculatorTest {


    /**
     * Mock object representing the class to be tested
     * @return
     * the instance of the class
     */
    private habitFollowCalculator mockCalc(){
        habitFollowCalculator calculator = new habitFollowCalculator();
        return calculator;
    }


    /**
     * Tests whether a habit that only has one valid date returns the number correctly
     */
    @Test
    public void testCalculationOneValid(){
        habitFollowCalculator calculator = mockCalc();

        //creates a habit that starts today and occurs today, so only one possible event
        Date today = new Date();
        ArrayList<String> days = new ArrayList<>();
        String[] dayArray = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        days.add(dayArray[(today.getDay())]);
        Habit testHabit = new Habit("TestName", true, today, days, "reason", 1);
        int result = calculator.calculateCloseness(testHabit);
        assertEquals(1, result);
    }

    /**
     * Test whether the object handles cases where the returned value should not be valid
     * and correctly returns 1
     */
    @Test
    public  void testCalculationNonZero(){
        habitFollowCalculator calculator = mockCalc();

        //creates a habit that starts tomorrow, so no possible events.  Should return 1. (Progress
        //will always stay 0, since no valid events can be generated)

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date future = new Date(3000, 1, 1);
        ArrayList<String> days = new ArrayList<>();
        days.add("SUNDAY");
        Habit testHabit = new Habit("TestName", true, future, days, "reason", 1);
        int result = calculator.calculateCloseness(testHabit);
        assertEquals(1, result);
    }

    /**
     * Tests whether the calculator returns an accurate (>1) value for a habit with multiple
     * valid days.  Because new possible dates for habit events will become available in future,
     * it was necessary to only code a general statement to check against
     */
    @Test
    public void testCalculationManyValid(){
        habitFollowCalculator calculator = mockCalc();

        Date start = new Date(121, 10, 11);

        ArrayList<String> days = new ArrayList<>();
        String[] dayArray = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        days.add(dayArray[(start.getDay())]);
        Habit testHabit = new Habit("TestName", true, start, days, "reason", 1);
        int result = calculator.calculateCloseness(testHabit);
        assert result > 1;
    }

    /**
     * Test whether the calculator is able to properly handle any start date and index it correctly
     * to return the correct number of valid habit event days
     */
    @Test
    public void allStartDatesTest(){
        habitFollowCalculator calculator = mockCalc();

        //setting intial values for habit
        Date start = new Date(121, 10, 1);
        ArrayList<String> days = new ArrayList<>();

        //testing sunday
        days.add("SUNDAY");
        Habit testHabit = new Habit("TestName", true, start, days, "reason", 1);
        int result = calculator.calculateCloseness(testHabit);
        assert(result > 1);

        //testing monday
        days.clear();
        days.add("MONDAY");
        testHabit = new Habit("TestName", true, start, days, "reason", 1);
        result = calculator.calculateCloseness(testHabit);
        assert result > 1;
        days.clear();

        //testing tuesday
        days.add("TUESDAY");
        testHabit = new Habit("TestName", true, start, days, "reason", 1);
        result = calculator.calculateCloseness(testHabit);
        assert result > 1;

        //testing wednesday
        days.clear();
        days.add("WEDNESDAY");
        testHabit = new Habit("TestName", true, start, days, "reason", 1);
        result = calculator.calculateCloseness(testHabit);
        assert result > 1;

        //testing thursday
        days.clear();
        days.add("THURSDAY");
        testHabit = new Habit("TestName", true, start, days, "reason", 1);
        result = calculator.calculateCloseness(testHabit);
        assert result > 1;

        //testing friday
        days.clear();
        days.add("FRIDAY");
        testHabit = new Habit("TestName", true, start, days, "reason", 1);
        result = calculator.calculateCloseness(testHabit);
        assert result > 1;

        //testing saturday
        days.clear();
        days.add("SATURDAY");
        testHabit = new Habit("TestName", true, start, days, "reason", 1);
        result = calculator.calculateCloseness(testHabit);
        assert result > 1;


    }


}
