/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sisyphus.Model.HabitEvent;
import com.example.sisyphus.Model.HabitEventListAdapter;
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
 * A class to list all habit events of a habit
 */
public class ListHabitEvent extends AppCompatActivity {
    //setting UI elements and initializing storage/formatting for listview
    ListView listHabitEvent;
    TextView topBarTitle;
    ArrayList<String> habitEventID;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventDataList;

    //initializing firebase authentication (session) object and connecting to database
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    FirebaseAuth mAuth;

    /**
     * Create a view to display all Habit events of a Habit
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_habit_event);

        //getting name of habit to display events for from intent
        Intent intent = getIntent();
        String currentHabit = intent.getStringExtra("1");

        //attaching UI elements to variables
        listHabitEvent= findViewById(R.id.list_habit_event);
        topBarTitle = findViewById(R.id.topbarText);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();

        //setting up listview and getting habitEvents properly stored in array
        habitEventDataList = new ArrayList<>();
        habitEventID = new ArrayList<>();
        habitEventAdapter = new HabitEventListAdapter(this, habitEventDataList);
        listHabitEvent.setAdapter(habitEventAdapter);

        topBarTitle.setText(currentHabit+" Events");

        setUserHabitEvent(mAuth.getUid(), currentHabit);



        listHabitEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /**
             * A function to view a habit event
             */
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (ListHabitEvent.this, ViewHabitEvent.class);
                HabitEvent clickedHabitEvent = habitEventDataList.get(i);
                //stores habit event in intent and passes to detail view menu
                intent.putExtra("habit_eventID", habitEventID.get(i));
                intent.putExtra("user",mAuth.getUid());
                intent.putExtra("habit_event",clickedHabitEvent);
                startActivity(intent);
            }
        });

        final Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * A function to switch to all habit list
             */
            public void onClick(View view) {
                finish();
            }
        });


        final FloatingActionButton addHabitEventButton = findViewById(R.id.add_habit_event);
        addHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * A function to switch to add habit event view
             */
            public void onClick(View view) {
                Intent addHabit = new Intent(view.getContext(), AddHabitEvent.class);
                addHabit.putExtra("1", currentHabit);
                startActivity(addHabit);
            }
        });
    }

    /**
     * Insert all habit's events and their documentID into lists
     * @param ID
     * The userID of the user to store data under
     * @param habitName
     * The habitName of the user to store habit events under
     */
    public void setUserHabitEvent(String ID, String habitName){
        final CollectionReference habitEventRef = db.collection("Users").document(ID).collection("Habits").document(habitName).collection("HabitEvent");
        habitEventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //clears data list and gathers new information from firebase for display
                habitEventDataList.clear();
                habitEventID.clear();
                for(QueryDocumentSnapshot doc:value){
                    HabitEvent result = doc.toObject(HabitEvent.class);
                    habitEventID.add(doc.getId());
                    habitEventDataList.add(result);
                }
                habitEventAdapter.notifyDataSetChanged();
            }
        });
    }

}