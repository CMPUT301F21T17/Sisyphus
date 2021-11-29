/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.HabitEvent;
import com.example.sisyphus.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

//A class designed to contain all the basic storage methods for firebase
//Should require no parameters for creation, only for methods
public class FirebaseStore {
    //setting up default variables to connect to Firebase
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
        //stores given User object at the provided ID in the database
        collectionReference
                .document(userID).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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
        //Stores given habit under the user associated with the given ID
        collectionReference
                //habit given a database name the same as the title given to it by a user
                .document(userID).collection("Habits").document(habit.getHabitName()).set(habit)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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

        //stores the habit event under the given habit for the given userID
        collectionReference
                .document(userID).collection("Habits").document(habitName).collection("HabitEvent").document(eventName).set(habitEvent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
     * @param habitEventID
     * The habit event to be edited
     * @param newHabitEvent
     * The modified habit event to be stored
     */
    public void editHabitEvent(String userID, String habitName, String habitEventID,HabitEvent newHabitEvent){
        //updating the given habit event with new information stored in parameters
        String eventName = habitEventID;
        collectionReference
                .document(userID).collection("Habits").document(habitName).collection("HabitEvent").document(eventName).set(newHabitEvent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });
    }

    /**
     * Function that removes a selected habit from the database
     * @param userId
     * the userID of the user whose habit is to be deleted
     * @param habitName
     * the name (unique ID) of the habit that is to be deleted
     */
    public void deleteHabit(String userId,String habitName){
        //searches for the given habit in the database and deletes
        collectionReference
                //habit given a database name the same as the title given to it by a user
                .document(userId).collection("Habits").document(habitName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }

    /**
     * Function that will delete a habit event from the database.  Required to prevent duplication
     * of habit events upon editing due to habit events getting names based on realtime.
     * @param ID
     *  ID of user to delete habit event from
     * @param habitName
     *  name of habit to delete habit event from
     * @param eventID
     *  ID of habit event to be deleted
     */
    public void deleteHabitEvent(String ID, String habitName, String eventID){
        //getting the given habit and deleting
        collectionReference.document(ID).collection("Habits").document(habitName)
                .collection("HabitEvent").document(eventID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Deleted successfully!");
                    }
                });
    }





}
