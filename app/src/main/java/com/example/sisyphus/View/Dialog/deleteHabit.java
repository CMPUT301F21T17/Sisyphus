/*
 * Copyright (c) 2021. 
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.User;
import com.example.sisyphus.View.AllHabitListView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

//Fragment that provides the user with option to delete a chose habit after confirming their choice
public class deleteHabit extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        //Setting up fragment
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Bundle bundle = getArguments();
        String delectTitle = bundle.getString("selectedTitle","");
        String delectUser = bundle.getString("selectedUser","");
        String delectTAG = bundle.getString("selectedTag","");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        return builder
                .setView(getView())
                .setTitle("Delete this habit and all related habit events?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //on "Yes" click, establishes database connection and then attempts to delete selected habit
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users").document(delectUser).collection("Habits").document(delectTitle)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(delectTAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(delectTAG, "Error deleting document", e);
                                    }
                                });
                        //returning to previous menu, as habit is deleted
                        Intent intent = new Intent(getContext(), AllHabitListView.class);
                        startActivity(intent);
                    }
                })
                //on "No" click, close menu
                .setNegativeButton("No", null)
                .create();
    }

}
