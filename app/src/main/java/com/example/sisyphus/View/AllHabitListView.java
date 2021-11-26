/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sisyphus.Model.AllHabitList_Adapter;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.User;
import com.example.sisyphus.Model.habitFollowCalculator;
import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private ArrayList<String> percents;

    //initializing firebase authentication (session) object and establishing database connection
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    final CollectionReference collectionReference = db.collection("Users");
    String TAG = "firebase habitEvent load";

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

        percents = new ArrayList<>();

        setUserHabit(currentUserID);

        habitAdapter = new AllHabitList_Adapter(this, habitDataList, percents);
        allhabitListView.setAdapter(habitAdapter);





        allhabitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /**
             * function to open ViewHabit when Habits clicked
             */
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                habitFollowCalculator c = new habitFollowCalculator();
                //int a = c.calculateCloseness(habitDataList.get(i), mAuth.getUid());

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
                    percents.add("0");
                }
                habitAdapter.notifyDataSetChanged();
                setHabitCompletion();
            }
        });
    }

    //method that polls each habit in the list and gets the completion result
    public void setHabitCompletion(){
        for(int i = 0; i < habitDataList.size(); i++){
            int finalI = i;
            collectionReference.document(mAuth.getUid()).collection("Habits").document(habitDataList.get(i).getHabitName()).collection("HabitEvent")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            final int currentIndex = finalI;
                            if (task.isSuccessful()) {
                                int counter = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    counter += 1;

                                }

                                System.out.println("Counted habit events: " + counter);
                                habitFollowCalculator calc = new habitFollowCalculator();
                                int totalDays = calc.calculateCloseness(habitDataList.get(currentIndex));

                                System.out.println(counter/totalDays);
                                System.out.println((100* counter/totalDays));


                                int percentClose = (int) Math.floor((100*counter/totalDays));

                                //should never happen, but sets completion % to 100 just
                                //in case value exceeds days of occurrence
                                if(percentClose > 100){
                                    percentClose = 100;
                                }

                                System.out.println("Calculated total = " + totalDays);
                                percents.set(currentIndex, String.valueOf(percentClose));

                                System.out.println("Actual val to set to: " + percentClose);
                                System.out.println("ArrayList Val at index: " + percents.get(currentIndex));

                                System.out.println("ran this");
                                habitAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

    }

}