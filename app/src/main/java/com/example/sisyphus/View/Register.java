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
import com.example.sisyphus.View.Dialog.errorFragment;
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
    //setting UI elements
    EditText registerFirstName;
    EditText registerLastName;
    EditText registerEmail;
    EditText registerPassword;
    EditText registerPasswordConfirm;
    Button registerConfirm;

    //initializing firebase authentication (session) object and setting up log message
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

        //attaching UI elements to variables
        EditText registerFirstName = findViewById(R.id.registerFirstName);
        EditText registerLastName = findViewById(R.id.registerLastName);
        EditText registerEmail = findViewById(R.id.registerEmail);
        EditText registerPassword = findViewById(R.id.registerPassword);
        EditText registerPasswordConfirm = findViewById(R.id.registerPasswordConfirm);
        Button registerConfirm = findViewById(R.id.registerConfirm);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();

        //onClick listener that takes user info from text-entry fields and registers (creates user
        //and authenticates new user) the current user
        registerConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting user input
                String testNorm = registerPassword.getText().toString();
                String testConfirm = registerPasswordConfirm.getText().toString();

                //validating passwords match
                if (testNorm.equals(testConfirm)) {
                    // Here is where you can tie the registering new user
                    // Data threads: registerEmail as new users email
                    // registerPassword as new users password
                    // registerPasswordConfirm is to strictly reinforce users password choice

                    //getting email and password
                    String emailStore;
                    String passStore;
                    emailStore = registerEmail.getText().toString();
                    passStore = registerPassword.getText().toString();


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





                    //if username, email and password non-null
                    //can be modified to add security constraints in future
                    if(emailStore.equals("") || passStore.equals("") || userFirst.equals("") || userLast.equals("")) {
                        new errorFragment("Please ensure all fields are filled out!").show(getSupportFragmentManager(), "Display_Error");
                    } else {
                        //attempting user authentication with given data
                        mAuth.createUserWithEmailAndPassword(emailStore, passStore)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign up worked, store new user and pass to main menu
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            FirebaseStore store = new FirebaseStore();
                                            store.storeUser(user.getUid(), registeredUser);

                                            // PASS INTENT TO MAIN MENU HERE!
                                            Intent startEmptyMain = new Intent(view.getContext(), EmptyMainMenu.class);
                                            startActivity(startEmptyMain);

                                        } else {
                                            // Failed sign in, display message informing user
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            new errorFragment("Please ensure email is a valid format, and password is 6 or more characters! (eg. JohnDoe@email.com, 123456)").show(getSupportFragmentManager(), "Display_Error");
                                        }
                                    }
                                });
                    }
                } else {
                    new errorFragment("Passwords do not match!").show(getSupportFragmentManager(), "Display_Error");
                }
            }
        });
    }
}