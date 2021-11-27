/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import static android.content.ContentValues.TAG;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.SocialListAdapter;
import com.example.sisyphus.Model.habitFollowCalculator;
import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SocialView extends AppCompatActivity {

    //setting UI elements and initializing storage/formatting for listview
    private ExpandableListView expandableListView;
    private SocialListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, ArrayList<Habit>> expandableListDetail;
    private HashMap<String,ArrayList<String>> expandableListPercents;


    //initializing firebase authentication (session) object and connecting to database
    private static FirebaseFirestore db=FirebaseFirestore.getInstance();;
    private static FirebaseAuth mAuth =  FirebaseAuth.getInstance();

    /**
     * Create a view to display all follow request
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_view);

        //attaching UI elements to variables
        expandableListView = (ExpandableListView) findViewById(R.id.socialExpandableListView);

        //initializing storage/formatting for listview
        expandableListDetail = new HashMap<>();
        expandableListTitle = new ArrayList<>();
        expandableListPercents = new HashMap<>();

        //Set all the following User'sHabit
        setFollowingUserHabitList();

        //Set adapter
        expandableListAdapter = new SocialListAdapter(this, expandableListTitle, expandableListDetail, expandableListPercents);
        expandableListView.setAdapter(expandableListAdapter);

        //Save for future development
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
                return false;
            }
        });


        //INTENTS FOR THE BOTTOM BAR!

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        //onClick listener to transfer user to habit list page
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialView.this, AllHabitListView.class);
                startActivity(intent);
            }
        });

        final Button button_calendar = findViewById(R.id.calendar_button);
        //onClick listener to transfer user to calendar page
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialView.this, CalendarActivity.class);
                startActivity(intent);
            }
        });


        final Button button_Home = findViewById(R.id.home_button);
        button_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialView.this, EmptyMainMenu.class);
                startActivity(intent);
            }
        });

        //currently sends to user search class.  May be worth changing later
        final Button button_Prof = findViewById(R.id.profile_button);
        button_Prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SocialView.this, UserSearch.class);
                startActivity(intent);
            }
        });

        //END OF INTENTS FOR THE BOTTOM BAR!
    }

    /**
     * Insert all following User's public habit into lists
     */
    private void setHabitList(){
        for(int i = 0;i<expandableListTitle.size();i++) {
            ArrayList<Habit> followedHabitList = new ArrayList<>();
            ArrayList<String> percents = new ArrayList<>();
            String ID =expandableListTitle.get(i);
            followedHabitList.clear();
            percents.clear();
            final CollectionReference habitRef = db.collection("Users").document(ID).collection("Habits");
            habitRef
                    // get the habits in the correct order
                    .orderBy("position")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (QueryDocumentSnapshot doc : value) {
                                Habit result = doc.toObject(Habit.class);

                                //Check private
                                if(!result.isPrivate()){
                                    followedHabitList.add(result);
                                    percents.add("0");
                                }
                            }
                            setHabitCompletion(followedHabitList, percents, ID);
                            //Set Habit List into a HashMap
                            expandableListDetail.put(ID,followedHabitList);
                            expandableListAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    /**
     * Insert all following User's ID  into lists
     */
    private void setFollowingUserHabitList() {
        Log.d(TAG, "Inside getData()--"+mAuth.getUid());
        final CollectionReference requestListRef = db.collection("Users").document(mAuth.getUid()).collection("Followed");
        requestListRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String followingUserID = document.getId();
                                expandableListTitle.add(followingUserID);
                                Log.d(TAG, "followingUserID:"+followingUserID);
                            }
                            Log.d(TAG,"TitleSize1:"+expandableListTitle.size());
                            setHabitList();
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }




    //method that polls each habit in the list and gets the completion result
    public void setHabitCompletion(ArrayList<Habit> habitDataList, ArrayList<String> percents, String ID){
        for(int i = 0; i < habitDataList.size(); i++){
            int finalI = i;
            final CollectionReference habitRef = db.collection("Users");
             habitRef.document(ID).collection("Habits").document(habitDataList.get(i).getHabitName()).collection("HabitEvent")
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
                                expandableListPercents.put(ID,percents);
                                expandableListAdapter.notifyDataSetChanged();
                                //habitAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

    }
}