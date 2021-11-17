/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sisyphus.Model.AllHabitList_Adapter;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A class to display a selectable calendar to display habit events of a day
 */
public class CalendarActivity extends AppCompatActivity {
    //initializing firebase authentication (session) object
    private FirebaseAuth mAuth;
    private Calendar selectedDay;

    //setting UI elements
    CalendarView calendar;
    RecyclerView habitsView;
    ArrayList<Habit> data;
    AllHabitList_Adapter adapter;

    /**
     * Create view to display calendar and habits
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //attaching UI elements to variables
        calendar = findViewById(R.id.calendar);
        habitsView = findViewById(R.id.calendar_events);
        selectedDay = new GregorianCalendar();
        data = new ArrayList<>();
        adapter = new AllHabitList_Adapter(this, data);
        habitsView.setLayoutManager(new LinearLayoutManager(this));
        habitsView.setAdapter(adapter);
        // event listener to get up to date data for Habit list

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();
        update(mAuth.getUid(), selectedDay);
        // click listener for changes to calendar widget
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            // get events from database for day year month day and populate habitsView
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                selectedDay.set(year, (month - 1), day, 0, 0, 0);
                // compare date_string to date field of calendar ref to get the correct group
                update(mAuth.getUid(), selectedDay);
            }
        });

        final Button button_home = findViewById(R.id.calendar_home);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function to open Home when clicked
             */
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, EmptyMainMenu.class);
                startActivity(intent);
            }
        });

        final Button button_calendar = findViewById(R.id.calendar_calendar);
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function to open Calendar when clicked
             */
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        final Button button_allHabitList = findViewById(R.id.calendar_habitlist);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function to open AllHabits list when clicked
             */
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this,AllHabitListView.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Function to update the HabitList with the Habits starting after and repeating on selectedDay
     * @param userID
     *  The String userID returned buy mAuth
     * @param selectedDay
     *  Calendar Object to compare to start date and days repeated
     */
    private void update(String userID, Calendar selectedDay) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(userID)
                .collection("Habits")
                .whereGreaterThanOrEqualTo("startDate", selectedDay.getTime())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //clears list and pulls in new data to populate list for selected day (if data exists)
                        data.clear();
                        if (value != null) {
                            for (QueryDocumentSnapshot d: value) {
                                Habit temp = d.toObject(Habit.class);
                                boolean add = false;
                                // check if habit repeats on selected day
                                for (String e : temp.getFrequency()){
                                    if (e.equals(CalToStr(selectedDay))){
                                        add = true;
                                        break;
                                    }
                                }
                                if (add) {
                                    // add habit to list to be displayed
                                    data.add(temp);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Function that converts a Calendar Object to a string representation of the current day
     * @param selectedDay
     *  A calendar object
     * @return
     *  A string representation of the current day
     */
    private String CalToStr (Calendar selectedDay) {
            int i = selectedDay.get(Calendar.DAY_OF_WEEK);
            switch (i) {
                case Calendar.FRIDAY:
                    return "MONDAY";
                case Calendar.SATURDAY:
                    return "TUESDAY";
                case Calendar.SUNDAY:
                    return "WEDNESDAY";
                case Calendar.WEDNESDAY:
                    return "SATURDAY";
                case Calendar.MONDAY:
                    return "THURSDAY";
                case Calendar.TUESDAY:
                    return "FRIDAY";
                case Calendar.THURSDAY:
                    return "SUNDAY";
            }
        return "ERROR";
    }
}
