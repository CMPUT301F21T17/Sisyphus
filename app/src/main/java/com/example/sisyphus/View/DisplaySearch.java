/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sisyphus.Model.AllHabitList_Adapter;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.SimpleUser;
import com.example.sisyphus.Model.User;
import com.example.sisyphus.Model.UserSearchAdapter;
import com.example.sisyphus.Model.followProtocol;
import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Class that exists to display the results of searches for users and enable
 * other users to send follow requests.  Will not display users who have already
 * had been sent requests, or those who are already being followed.
 */
public class DisplaySearch extends AppCompatActivity {
    String TAG = "Query Display";

    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;

    //setting UI elements and initializing storage/formatting for listview
    ListView listUsers;
    ArrayAdapter<SimpleUser> userSearchAdapter;
    ArrayList<SimpleUser> searchedUserList;

    //initializing firebase authentication (session) object and establishing database connection
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");

    //name of the searched user
    String first;
    String last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);

        //getting search parameters from intent
        Intent intent = getIntent();
        first = intent.getStringExtra("fName");
        last = intent.getStringExtra("lName");

        //setting up auth object
        mAuth = FirebaseAuth.getInstance();

        //attaching UI elements, and formatting listview boxes as well as storage array for habits
        listUsers = findViewById(R.id.user_list);
        searchedUserList = new ArrayList<>();

        //properly filling array with information for display
        setUserInfo();

        userSearchAdapter = new UserSearchAdapter(this, searchedUserList);
        listUsers.setAdapter(userSearchAdapter);


        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        //onClick listener to transfer user to habit list page
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaySearch.this, AllHabitListView.class);
                startActivity(intent);
            }
        });

        final Button button_calendar = findViewById(R.id.calendar_button);
        //onClick listener to transfer user to calendar page
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaySearch.this, CalendarActivity.class);
                startActivity(intent);
            }
        });


        final Button button_Home = findViewById(R.id.home_button);
        button_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaySearch.this, EmptyMainMenu.class);
                startActivity(intent);
            }
        });

        //currently sends to user search class.  May be worth changing later
        final Button button_Prof = findViewById(R.id.profile_button);
        button_Prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaySearch.this, UserSearch.class);
                startActivity(intent);
            }
        });





    }

    /**
     * Function that populates the array of search results with all users matching the given name.
     * Excludes users that the current user is already following or has already sent requests to in order
     * to prevent duplicate friends and request spam.
     * Ideally would be part of a different object, but the asynchronous nature of firebase calls requires nesting of
     * subsequent calls to permit proper timing
     */
    public void setUserInfo() {
        //setting up query to pull all users with matching names
        Query namedUsers = db.collection("Users").whereEqualTo("first", first).whereEqualTo("last", last);
        namedUsers
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //for each result, adding them temporarily to item list with properly formatted data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                //getting object, reformatting, and storing
                                User result = document.toObject(User.class);
                                SimpleUser simple = new SimpleUser(result.getFirst(), result.getLast(), document.getId());
                                searchedUserList.add(simple);
                            }

                            //checking to see if user has already sent request to each person in list by searching their outgoing requests
                            for(SimpleUser current : searchedUserList){

                                DocumentReference docRef = db.collection("Users").document(mAuth.getUid()).collection("Outgoing")
                                        .document(current.getId());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();

                                            //if user has sent request, remove search result from list to prevent display
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                searchedUserList.remove(current);
                                                userSearchAdapter.notifyDataSetChanged();

                                            } else {
                                                //otherwise do nothing!  Result should stay in list
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }

                            //checking to see if user has already followed each person in list
                            for(SimpleUser current : searchedUserList){

                                DocumentReference docRef = db.collection("Users").document(mAuth.getUid()).collection("Followed")
                                        .document(current.getId());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();

                                            //if search result exists as a followed user, delete the result from list!
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                //deleting and updating
                                                searchedUserList.remove(current);
                                                userSearchAdapter.notifyDataSetChanged();

                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }

                            //prevent user from showing up in their own search results
                            for(SimpleUser current : searchedUserList){

                                if(current.getId().equals(mAuth.getUid())){
                                    searchedUserList.remove(current);
                                }
                            }



                            //when done, update
                            userSearchAdapter.notifyDataSetChanged();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });
    }
}