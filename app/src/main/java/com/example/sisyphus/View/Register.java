/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sisyphus.Model.FirebaseStore;
import com.example.sisyphus.R;
import com.example.sisyphus.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

/**
 * A class for registering new users
 */
public class Register extends AppCompatActivity {

    EditText registerFirstName;
    EditText registerLastName;
    EditText registerEmail;
    EditText registerPassword;
    EditText registerPasswordConfirm;
    Button registerConfirm;

    private FirebaseAuth mAuth;

    private static final String TAG = "EmailPassword";

    /**
     * Create a view to get information to create a new user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        EditText registerFirstName = findViewById(R.id.registerFirstName);
        EditText registerLastName = findViewById(R.id.registerLastName);
        EditText registerEmail = findViewById(R.id.registerEmail);
        EditText registerPassword = findViewById(R.id.registerPassword);
        EditText registerPasswordConfirm = findViewById(R.id.registerPasswordConfirm);
        Button registerConfirm = findViewById(R.id.registerConfirm);

        mAuth = FirebaseAuth.getInstance();

        registerConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String testNorm = registerPassword.getText().toString();
                String testConfirm = registerPasswordConfirm.getText().toString();

                if (testNorm.equals(testConfirm)) {
                    // Here is where you can tie the registering new user
                    // Data threads: registerEmail as new users email
                    // registerPassword as new users password
                    // registerPasswordConfirm is to strictly reinforce users password choice


                    String emailStore;
                    String passStore;
                    emailStore = registerEmail.getText().toString();
                    passStore = registerPassword.getText().toString();

                    System.out.println(emailStore);
                    System.out.println(passStore);

                    //getting data entry fields
                    //TO-DO once UI complete!
                    String userFirst = registerFirstName.getText().toString();
                    String userLast = registerLastName.getText().toString();



                    //Validating data entry fields
                    //TO-DO once UI complete

                    //NO VALIDATION NEEDED AT THIS STEP AS OF CURRENT



                    //creating user with valid data

                    Date currentDate = new Date();
                    User registeredUser = new User(userFirst, userLast, currentDate);





                    //if username and password non-null
                    //can be modified to add security constraints in future
                    if(emailStore != "" && passStore != ""){

                        //WHOLE SECTION BELOW COPIED WITH VERY LITTLE EDITING FROM FIREBASE DOCUMENTATION!! CITE!!
                        mAuth.createUserWithEmailAndPassword(emailStore, passStore)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            FirebaseStore store = new FirebaseStore();
                                            store.storeUser(user.getUid(), registeredUser);

                                            // PASS INTENT TO MAIN MENU HERE!
                                            Intent startEmptyMain = new Intent(view.getContext(), EmptyMainMenu.class);
                                            startActivity(startEmptyMain);

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(Register.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
}