/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sisyphus.Model.SimpleUser;
import com.example.sisyphus.Model.User;
import com.example.sisyphus.Model.UserSearchAdapter;
import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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


        mAuth = FirebaseAuth.getInstance();

        //attaching UI elements, and formatting listview boxes as well as storage array for habits
        listUsers = findViewById(R.id.user_list);
        searchedUserList = new ArrayList<>();

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
        final Button button_Prof = findViewById(R.id.social_button);
        button_Prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaySearch.this, UserSearch.class);
                startActivity(intent);
            }
        });





    }

    public void setUserInfo() {
        System.out.println("Got here!");
        Query namedUsers = db.collection("Users").whereEqualTo("first", first).whereEqualTo("last", last);
        namedUsers
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println("SUCCEEDED");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User result = document.toObject(User.class);
                                SimpleUser simple = new SimpleUser(result.getFirst(), result.getLast(), document.getId());
                                searchedUserList.add(simple);
                            }

                            //checking to see if user has already sent request to each person in list
                            for(SimpleUser current : searchedUserList){
                                System.out.println(current.getId());
                                DocumentReference docRef = db.collection("Users").document(mAuth.getUid()).collection("Outgoing")
                                        .document(current.getId());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
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
                            final String[] test = {"Bad"};
                            //checking to see if user has already followed each person in list
                            for(SimpleUser current : searchedUserList){
                                System.out.println(current.getId());
                                DocumentReference docRef = db.collection("Users").document(mAuth.getUid()).collection("Followed")
                                        .document(current.getId());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                searchedUserList.remove(current);
                                                userSearchAdapter.notifyDataSetChanged();
                                                System.out.println(current.getId());
                                                test[0] = "Good!";
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }

                            //can't search for yourself!
                            for(SimpleUser current : searchedUserList){
                                System.out.println("iterated!");
                                System.out.println(current.getId());
                                if(current.getId().equals(mAuth.getUid())){
                                    searchedUserList.remove(current);
                                }
                            }


                            System.out.println(test[0]);

                            System.out.println(searchedUserList.size());
                            //when done, update
                            userSearchAdapter.notifyDataSetChanged();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            System.out.println("FAILED");
                        }
                    }
                });
    }
}