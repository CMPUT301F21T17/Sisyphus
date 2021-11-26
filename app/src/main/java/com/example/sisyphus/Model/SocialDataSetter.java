/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

public class SocialDataSetter {

    private static HashMap<String, ArrayList<Habit>> expandableListDetail= new HashMap<>();
    private static ArrayList<Habit> followedHabitList = new ArrayList<>();;
    private static FirebaseFirestore db=FirebaseFirestore.getInstance();;
    private static FirebaseAuth mAuth =  FirebaseAuth.getInstance();;


    public static HashMap<String, ArrayList<Habit>> getData() {
        Log.d("get Data:", "inside"+mAuth.getUid());
        Log.d("insidesetFollowhabit", mAuth.getUid());
        final CollectionReference requestListRef = db.collection("Users").document(mAuth.getUid()).collection("Followed");
        requestListRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String followingUserID = document.getId().toString();
                                //getFollowedHabit(followingUserID);
                                Log.d("insidegetFollowhabit", followingUserID);
                                final CollectionReference habitRef = db.collection("Users").document(followingUserID).collection("Habits");
                                habitRef
                                        // get the habits in the correct order
                                        .orderBy("position")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                followedHabitList.clear();
                                                for(QueryDocumentSnapshot doc:value){
                                                    Habit result = doc.toObject(Habit.class);
                                                    //i++;
                                                    Log.d("get followhabitname","nameis:"+result.getHabitName());
                                                    followedHabitList.add(result);
                                                    Log.d("get after","nameis:"+followedHabitList.get(0).getHabitName());
                                                }
                                                Log.d(TAG,"habitlistis"+followedHabitList.get(0).getHabitName());
                                                expandableListDetail.put(followingUserID,followedHabitList);
                                                Log.d(TAG,"keysetis:"+expandableListDetail.keySet().toString());
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        Log.d("theDataIs:", "inside:"+expandableListDetail.isEmpty());
        return expandableListDetail;
    }

    /**
     * Insert all habit's events and their documentID into lists
     * @param currentID
     * The userID of the user to store data under
     *//*
    public void setFollowedHabit(String currentID){
        Log.d("insidesetFollowhabit", currentID);
        final CollectionReference requestListRef = db.collection("Users").document(currentID).collection("Followed");
        requestListRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String followingUserID = document.getId().toString();
                                //getFollowedHabit(followingUserID);
                                Log.d("insidegetFollowhabit", followingUserID);
                                final CollectionReference habitRef = db.collection("Users").document(followingUserID).collection("Habits");
                                habitRef
                                        // get the habits in the correct order
                                        .orderBy("position")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                followedHabitList.clear();
                                                for(QueryDocumentSnapshot doc:value){
                                                    Habit result = doc.toObject(Habit.class);
                                                    //i++;
                                                    Log.d("get followhabitname","nameis:"+result.getHabitName());
                                                    followedHabitList.add(result);
                                                    Log.d("get after","nameis:"+followedHabitList.get(0).getHabitName());
                                                }
                                                Log.d(TAG,"habitlistis"+followedHabitList.get(0).getHabitName());
                                                expandableListDetail.put(followingUserID,followedHabitList);
                                                Log.d(TAG,"keysetis:"+expandableListDetail.keySet().toString());
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }*/

    /**
     * Insert all habits from firebase into a list
     * @param ID
     * The userID of the user to store data under
     */
    /*public void getFollowedHabit(String ID){
        Log.d("insidegetFollowhabit", ID);
        final CollectionReference habitRef = db.collection("Users").document(ID).collection("Habits");
        habitRef
                // get the habits in the correct order
                .orderBy("position")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        habitList.clear();
                        for(QueryDocumentSnapshot doc:value){
                            Habit result = doc.toObject(Habit.class);
                            Log.d("get followhabitname","nameis:"+result.getHabitName());
                            habitList.add(result);
                            Log.d("get after","nameis:"+habitList.get(0).getHabitName());
                        }
                    }
                });
    }*/
}
