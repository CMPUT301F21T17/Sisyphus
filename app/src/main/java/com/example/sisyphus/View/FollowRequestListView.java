/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sisyphus.Model.FollowRequestListAdapter;
import com.example.sisyphus.Model.HabitEvent;
import com.example.sisyphus.Model.HabitEventListAdapter;
import com.example.sisyphus.Model.User;
import com.example.sisyphus.Model.followProtocol;
import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FollowRequestListView extends AppCompatActivity {

    //setting UI elements and initializing storage/formatting for listview
    ListView listFollowRequest;
    ArrayAdapter<String> followRequestAdapter;
    ArrayList<String> followRequestList;
    Button back;

    //initializing firebase authentication (session) object and connecting to database
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    FirebaseAuth mAuth;
    followProtocol test = new followProtocol();

    /**
     * Create a view to display all follow request
     * @param savedInstanceState
     *  state of previous instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request_list_view);

        //attaching UI elements to variables
        listFollowRequest= findViewById(R.id.list_follow_request);
        back = findViewById(R.id.back);
        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();

        //setting up listview and getting follow request properly stored in array
        followRequestList = new ArrayList<>();
        followRequestAdapter = new FollowRequestListAdapter(this,followRequestList);
        listFollowRequest.setAdapter(followRequestAdapter);
        setFollowRequest(mAuth.getUid());


        back.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when back button clicked
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /**
     * Insert all follow request and the follower ID into lists
     * @param ID
     * The userID of the user to store data under
     */
    public void setFollowRequest(String ID){
        followProtocol test = new followProtocol();
        final CollectionReference requestListRef = db.collection("Users").document(ID).collection("Incoming");
        requestListRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**
                     * function called when query completes
                     * @param task
                     *  query to run
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String result = document.getId();
                                followRequestList.add(result);
                                listFollowRequest.setAdapter(followRequestAdapter);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        followRequestAdapter.notifyDataSetChanged();
                    }
                });
    }
}