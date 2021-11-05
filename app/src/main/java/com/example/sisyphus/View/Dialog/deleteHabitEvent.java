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

import com.example.sisyphus.View.ListHabitEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

//Class that acts as a menu for deleting habit events after a user has confirmed their choice
public class deleteHabitEvent extends DialogFragment {
    //log message
    final String deleteTAG = "Sample";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        //Setting up fragment
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Bundle bundle = getArguments();
        String deleteTitle = bundle.getString("selectedHabit","");
        String deleteID= bundle.getString("selectedEventID","");
        String deleteUser = bundle.getString("selectedUser","");
        //String eventName = deleteTitle + " event " + deleteDate;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        return builder
                .setView(getView())
                .setTitle("Delete this habit event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //on "Yes" click, connect to database and attempt to delete selected habit event
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users").document(deleteUser).collection("Habits").document(deleteTitle).collection("HabitEvent").document(deleteID)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(deleteTAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(deleteTAG, "Error deleting document", e);
                                    }
                                });
                        //return to previous menu, as habit event is deleted.
                        Intent intent = new Intent(getContext(), ListHabitEvent.class);
                        intent.putExtra("1",deleteTitle);
                        startActivity(intent);
                    }
                })
                //on "No" click, close window
                .setNegativeButton("No", null)
                .create();
    }
}
