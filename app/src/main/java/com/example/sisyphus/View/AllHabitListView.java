/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sisyphus.Model.AllHabitList_Adapter;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.habitFollowCalculator;
import com.example.sisyphus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A class to display all Habits in a views list
 */
public class AllHabitListView extends AppCompatActivity {
    //setting UI elements and initializing storage/formatting for listview
    private ListView allhabitListView;
    private ArrayAdapter<Habit> habitAdapter;
    private ArrayList<Habit> habitDataList;

    //initializing firebase authentication (session) object and establishing database connection
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    final CollectionReference collectionReference = db.collection("Users");

    /**
     * create view to display all habit events
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit_list);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String currentUserID = mAuth.getUid();

        //attaching UI elements, and formatting listview boxes as well as storage array for habits
        allhabitListView= findViewById(R.id.allhabit_list);
        habitDataList = new ArrayList<>();

        setUserHabit(currentUserID);

        habitAdapter = new AllHabitList_Adapter(this, habitDataList);
        allhabitListView.setAdapter(habitAdapter);





        allhabitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /**
             * function to open ViewHabit when Habits clicked
             */
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                habitFollowCalculator c = new habitFollowCalculator();
                int a = c.calculateCloseness(habitDataList.get(i), mAuth.getUid());

                Intent intent = new Intent (AllHabitListView.this, ViewHabit.class);
                //storing selected habit in intent
                Habit clickedHabit = habitDataList.get(i);
                intent.putExtra("habit",clickedHabit);
                startActivity(intent);
            }
        });

        final Button button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function to open Home when clicked
             */
            public void onClick(View v) {
                Intent intent = new Intent(AllHabitListView.this, EmptyMainMenu.class);
                startActivity(intent);
            }
        });

        final Button button_calendar = findViewById(R.id.calendar_button);
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function to open Calendar when clicked
             */
            public void onClick(View v) {
                Intent intent = new Intent(AllHabitListView.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function to open AllHabits list when clicked
             */
            public void onClick(View view) {
                Intent intent = new Intent(AllHabitListView.this,AllHabitListView.class);
                startActivity(intent);
            }
        });


        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function to open AddHabit
             */
            public void onClick(View view) {
                Intent toAddHabit = new Intent(AllHabitListView.this, AddHabit.class);
                startActivity(toAddHabit);
            }
        });
    }

    /**
     * Insert all habits from firebase into a list
     * @param ID
     * The userID of the user to store data under
     */
    public void setUserHabit(String ID){
        final CollectionReference habitRef = db.collection("Users").document(ID).collection("Habits");
        habitRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //clears data so list can be updated, then gets all habit data from firebase to display
                habitDataList.clear();
                for(QueryDocumentSnapshot doc:value){
                    Habit result = doc.toObject(Habit.class);
                    habitDataList.add(result);
                }
                habitAdapter.notifyDataSetChanged();
            }
        });
    }

}