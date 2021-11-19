/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that will contain protocol methods for handling how users
 * follow other users, send requests, get updates, etc.  Does not require
 * parameters.  This class assumes implicitly that other locations handle the prevention of
 * duplicate followers/followees by preventing the initial sending of requests, but this class
 * does handle requests that are attempted to be sent if a duplicate request exists
 */
public class followProtocol {

    //setting up default variables to connect to Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");
    private final String TAG = "Sample";
    private FirebaseAuth mAuth;

    public followProtocol(){
        //Default constructor
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * A method that sends a request to follow to a user, updating firebase to
     * reflect this state for both users
     * @param ID
     * the ID of the user being sent the request
     */
    public void sendRequest(String ID){
        if (ID.equals(mAuth.getUid())){
            System.out.println("You can't send requests to yourself!");
            return;
        }

        ArrayList<Boolean> requestExists = new ArrayList<Boolean>();


        //pulling from firebase to see if request already exists (has been stored in outgoing for this user)
        DocumentReference docRef = collectionReference.document(mAuth.getUid()).collection("Outgoing").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                //System.out.print("did this");
                if (task.isSuccessful()) {
                  //  System.out.println("and this");
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //requestExists.add(true);
                        Log.d(TAG, "Document exists");
                        System.out.println("Request already sent! Cannot send another until resolved!");
                    } else {
                        Log.d(TAG, "No such document");

                        //setting data for storage
                        Map<String, Object> data = new HashMap<>();
                        data.put("UserID", ID);

                        //stores ID of user who request was sent to in "outgoing" collection of sender to prevent repeats
                        collectionReference
                                .document(mAuth.getUid()).collection("Outgoing").document(ID).set(data)
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


                        Map<String, Object> authData = new HashMap<>();
                        data.put("UserID", mAuth.getUid());
                        //stores ID of user who sent request in "incoming" collection for user request was sent to
                        collectionReference
                                .document(ID).collection("Incoming").document(mAuth.getUid()).set(authData)
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
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }



    //meant to occur when request handled, positively or negatively.
    //since the request exists to be handled, this method automatically assumes that the request
    //must exist in the respective outgoing and incoming collections. Since both are added to at once,
    //and since this is the only method to remove data from them, this should always hold

    /**
     * Method that changes the state of a request to resolved by removing the request from the
     * correct "outgoing" and "incoming" collections in firebase
     * @param sentID
     * the ID of the user who the request was sent from, and who will have the request in their "outgoing" collection
     */
    public void handleRequest(String sentID){

        collectionReference.document(mAuth.getUid()).collection("Incoming").document(sentID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Incoming request resolved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        collectionReference.document(sentID).collection("Outgoing").document(mAuth.getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Outgoing request resolved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    //meant to occur when request accepted, after handleRequest()

    /**
     * A method that registers a user as following another user, and lists the new follower
     * as an actual follower of the current user.
     * @param followerID
     * the ID of the user who will be added as a follower to the current user
     */
    public void commitFollow(String followerID){

        //setting data for storage
        Map<String, Object> followerData = new HashMap<>();
        followerData.put("UserID", followerID);

        //stores ID of user who request was sent to in "outgoing" collection of sender to prevent repeats
        collectionReference
                .document(mAuth.getUid()).collection("Followers").document(followerID).set(followerData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "New follower added!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }

                });


        Map<String, Object> followedData = new HashMap<>();
        followedData.put("UserID", mAuth.getUid());

        //stores ID of user who sent request in "incoming" collection for user request was sent to
        collectionReference
                .document(followerID).collection("Followed").document(mAuth.getUid()).set(followedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "Now following " + followerID + "!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }

                });
    }
}
