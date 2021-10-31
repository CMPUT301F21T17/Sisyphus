package com.example.sisyphus;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        final String TAG = "Sample";
        //addHabitEvent(TAG);



        Date testDate = new Date();
        FirebaseStore store = new FirebaseStore();
        String dummyUID = "111111111";
        User testUser = new User("New user", "New user last", testDate);
        Habit testHabit = new Habit("Testing automated add class", "To see if it works", 1, testDate);
        HabitEvent testEvent = new HabitEvent("did this work?", testDate);



        store.storeUser(dummyUID, testUser);
        store.storeHabit(dummyUID, testHabit);
        store.storeHabitEvent(dummyUID, testHabit.getName(), testEvent);





        Intent testSignIn = new Intent(this, DummySignIn.class);
        startActivity(testSignIn);

    }

    /**
     * A dummy function meant to test if firebase is capable of adding to the deepest nested
     * strucutre in the current database layout
     */
    public void addHabitEvent(String TAG) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        String userID = "jqrBkABHBSWzCKv4DoP5";
        String habitID = "s7iYT2nQSDg52bngNE8y";
        Date testDate = new Date();
        HabitEvent newEvent = new HabitEvent("Testing", testDate);
        User testUser = new User("Not Sean", "Not Paetz", testDate);
        Habit testHabit = new Habit("Testing Firebase", "Please work!", 1234567, testDate);
        collectionReference
                //.document(userID).collection("Habits").document(habitID).collection("HabitEvent").document("temp").set(newEvent)
                //.document("Temp User").set(testUser)
                //.document(userID).collection("Habits").document("TestHabit").set(testHabit)
                .document(userID).collection("Habits").document("TestHabit").collection("HabitEvent").document("NestedTest").set(newEvent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }

                });

    }

}