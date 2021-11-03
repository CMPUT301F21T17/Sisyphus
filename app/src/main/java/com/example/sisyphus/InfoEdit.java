package com.example.sisyphus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//
// This activity represents the area where users can edit their information
//
// Users are sent from the settings activity, with an extra from the intent that labels
// which bit of user information is being corrected.
// User will be able to see the current iteration of this information in a textview
// Then under the new section, they can edit the information.
// Upon pressing confirm, firebase is updated with the new information and user is returned to
// the settings activity.
//


public class InfoEdit extends AppCompatActivity {

    TextView infoCurrentItem;
    EditText infoEditItem;
    Button editCancel;
    Button editConfirm;
    FirebaseAuth mAuth;
    User activeUser;
    String TAG = "editUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoedit);

        // Initializing Text and Edit Views
        infoCurrentItem = findViewById(R.id.infoCurrentItem);
        infoEditItem = findViewById(R.id.infoEditItem);
        // Initializing Buttons
        editCancel = findViewById(R.id.editCancel);
        editConfirm = findViewById(R.id.editConfirm);

        Intent intent = getIntent();
        String item = intent.getStringExtra(Settings.item);


        mAuth = FirebaseAuth.getInstance();
        System.out.println(mAuth.getCurrentUser().getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                activeUser = documentSnapshot.toObject(User.class);
                if (item.equals("fName")) {
                    infoCurrentItem.setText(activeUser.getFirst());
                } else if (item.equals("lName")) {
                    infoCurrentItem.setText(activeUser.getLast());
                } else if (item.equals("email")) {
                    infoCurrentItem.setText(mAuth.getCurrentUser().getEmail());
                }

            }
        });




        // Abandons the changes and just returns back to settings
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelInt = new Intent(getApplicationContext(), Settings.class);
                startActivity(cancelInt);
            }
        });

        editConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Eventually add error checking here
                // Send data to database that changes the item specified
                // If item is first name, then replace databases first name, and so on
                if (item.equals("fName") && infoEditItem.getText().toString() != "") {
                    //below is straight from firebase docs
                    DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());

                    userRef
                            .update("first", infoEditItem.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                } else if (item.equals("lName") && infoEditItem.getText().toString() != "") {
                    DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());

                    userRef
                            .update("last", infoEditItem.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                } else if (item.equals("email") && infoEditItem.getText().toString() != "") {
                    mAuth.getCurrentUser().updateEmail(infoEditItem.getText().toString());
                }
                // Then eventually we switch back to settings
                Intent confirmInt = new Intent(getApplicationContext(), Settings.class);
                startActivity(confirmInt);
            }
        });
    }
}