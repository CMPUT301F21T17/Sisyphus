package com.example.sisyphus;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class deleteHabitEvent extends DialogFragment {
    final String deleteTAG = "Sample";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Bundle bundle = getArguments();
        String deleteTitle = bundle.getString("selectedHabit","");
        String deleteDate= bundle.getString("selectedEventDate","");
        String deleteUser = bundle.getString("selectedUser","");
        String eventName = deleteTitle + " event " + deleteDate;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        return builder
                .setView(getView())
                .setTitle("Delete this habit and all related habit events?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users").document(deleteUser).collection("Habits").document(deleteTitle).collection("HabitEvent").document(eventName)
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

                        Intent intent = new Intent(getContext(), ListHabitEvent.class);
                        intent.putExtra("1",deleteTitle);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .create();
    }
}