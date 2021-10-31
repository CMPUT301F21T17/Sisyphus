package com.example.sisyphus;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

//A class designed to contain all the basic storage methods for firebase
//Should require no parameters for creation, only for methods
public class FirebaseStore {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");
    private final String TAG = "Sample";

    public FirebaseStore(){
        //Default constructor
    }

    /**
     * Stores a user to firebase
     * @param userID
     * the userID to store the data under
     * @param user
     * The user data to be stored
     */
    public void storeUser(String userID, User user){
        collectionReference
                .document(userID).set(user)
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

    /**
     * Stores a habit to firebase
     * @param userID
     * The userID of the user to store data under
     * @param habit
     * The data of the habit to be stored
     */
    public void storeHabit(String userID, Habit habit){
        collectionReference
                //habit given a database name the same as the title given to it by a user
                .document(userID).collection("Habits").document(habit.getName()).set(habit)
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

    /**
     * Stores a habit event to firebase
     * @param userID
     * The userID of the user to store data under
     * @param habitName
     * The name of the habit to which the event belongs
     * @param habitEvent
     * The habit event to be stored
     */
    public void storeHabitEvent(String userID, String habitName, HabitEvent habitEvent){
        // creating a unique name for the habit event based on current date and time
        Date today = new Date();
        String eventName = habitName + " event " + today;
        collectionReference
                .document(userID).collection("Habits").document(habitName).collection("HabitEvent").document(eventName).set(habitEvent)
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