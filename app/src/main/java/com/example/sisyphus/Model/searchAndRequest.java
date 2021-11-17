/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */


package com.example.sisyphus.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Class that serves as an object for sending and retrieving requests to follow from the firebase
 * database. Should not require any parameters to create, and only requires string input data
 */
public class searchAndRequest {
    //setting up default variables to connect to Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");
    private final String TAG = "Sample";

    public searchAndRequest(){
        //Default constructor
    }

    public boolean userValid(String id){
        final User[] retirevedUser = new User[1];
        //firebase documentation with a few edits
        DocumentReference userRef = collectionReference.document(id);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Data retrieved! Valid user");
                        retirevedUser[0] = document.toObject(User.class);
                    } else {
                        Log.d(TAG, "No such user in database!");
                        retirevedUser[0] = null;
                    }
                } else {
                    Log.d(TAG, "Throwing exception on failure, exception type: ", task.getException());
                    retirevedUser[0] = null;
                }
            }
        });

        if (retirevedUser[0] == null){
            return true;
        } else {
            return false;
        }
    }


}
