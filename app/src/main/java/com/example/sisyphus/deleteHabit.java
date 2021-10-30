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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class deleteHabit extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
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

                        Intent intent = new Intent(getContext(),AllHabitListView.class);
                        intent.putExtra("currentUserID",delectUser);
                        intent.putExtra("currentTag",delectTAG);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .create();
    }

}
