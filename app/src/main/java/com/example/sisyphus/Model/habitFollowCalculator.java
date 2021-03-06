/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

/**
 * Class that takes in a habit and computes the closeness with which the user
 * is following that habit by referencing the habit events associated with each habit as well
 * as the total number of days the habit should have been completed on.
 */
package com.example.sisyphus.Model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class that performs a calculation based on give habit info to determine the correct
 * number of days that the habit occurred on.  This permits the number of habit events to be referenced
 * against this number to allow display of habit event completion for each habit
 */
public class habitFollowCalculator {


    //date format for parsing and comparison
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Default constructor
     */
    public habitFollowCalculator(){

    }

    /**
     * Function that takes a habit and references the database to determine how
     * closely the user is following the habit.  Does this by computing the start date
     * and then taking in the number of habit events.  ASSUMES THAT NO DUPLICATE HABIT
     * EVENTS EXIST, ONlY 1 EVENT PER DAY, NO EVENTS IN THE FUTURE OR BEFORE THE START DATE.
     * These conditions are enforced in addHabitEvent and editHabitEvent
     * @param habit
     * the habit to perform the calculation on
     * @return
     * the integer representation of the percent of days the habit was performed on (ie 25 = 25%)
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int calculateCloseness(Habit habit){


        //Get number of days from habit
        String startDate = format.format( habit.getStartDate());

        //getting the start and end of the habit period
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.now();

        //creating period to get total number of days between start and end
        Period sinceStart = Period.between(start, end);



        //when start = end, period = 0, so return 1 (the current day) as the only possible
        //date
        if(sinceStart.getDays() == 0){
            return 1;
        }



        //getting day of week to start
        int startEncode = habit.getStartDate().getDay();


        ArrayList<String> dates = habit.getFrequency();

        //the encoding (0-6 starting at sunday) of the date currently being checked
        int currentEncode = 0;

        //counter to keep track of days where habit event should occur
        int activeDaysCounter = 0;

        //check all dates stored that habit should occur on.  For each weekday, calculate total num
        //of times weekday occurs from start to current date, then add that number to the total
        for(int i = 0; i < dates.size(); i++){

            //encoding date based on string value (since array indices in firebase are not
            //consistent.  If wednesday is the only date the habit occurs on, it will be in the 0'th index
            // when it should be encoded 3)
            if(dates.get(i).equals("SUNDAY")){
                currentEncode = 0;
            } else if (dates.get(i).equals("MONDAY")){
                currentEncode = 1;
            } else if (dates.get(i).equals("TUESDAY")){
                currentEncode = 2;
            } else if (dates.get(i).equals("WEDNESDAY")){
                currentEncode = 3;
            } else if (dates.get(i).equals("THURSDAY")){
                currentEncode = 4;
            } else if (dates.get(i).equals("FRIDAY")){
                currentEncode = 5;
            } else if (dates.get(i).equals("SATURDAY")){
                currentEncode = 6;
            }





            //formula that adjusts the remaining days based on the start position of the habit
            //and the current date being checked for. Start day will always be re-encoded as 0,
            //the day after as 1, and so on.  Can produce negative values, handled later
            int reEncode = (6- startEncode)+currentEncode - 6;

            //if negative value produced above, then the start date encoding is greater
            //than the current date encoding. So instead of subtracting by 6, we add one to wrap
            //the value around and get the proper encoding.  For example, if startEncode = 1 and
            //currentEncode = 0, then 6-1 +0 = 5, and 5 -6 = -1. But Sunday is the 6th day after
            //monday (our start date), so 5+1 = 6.  Pattern works in all cases
            if(reEncode < 0) {
                reEncode = (6- startEncode)+currentEncode + 1;
            }

            //setting the number of days we need to check for
            int iterateCounter = sinceStart.getDays()+1;


            //loop that iterates the counter by 1 so long as the day can be reached.  If
            //not, then we know the day does not come up again
            while(iterateCounter > reEncode){
                activeDaysCounter += 1;

                //"advancing time" by 1 week
                iterateCounter -= 7;
            }


        }


        //prevents possibility of divide by 0 error if habit has no valid days that
        //it could have occurred on yet (ie. Added on a Saturday but occurs on Wednesdays)
        if(activeDaysCounter == 0){
            return 1;
        }

        return activeDaysCounter;


    }
}
