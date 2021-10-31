package com.example.sisyphus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;



//DEAD CLASS!! CURRENTLY ONLY EXISTS AS A CODE REPOSITORY FROM PREVIOUS TESTS
public class DummySignIn extends AppCompatActivity {

    //IMportant note about mAuth: needs to be passed around in order to allow other activities
    // to use the same auth object!
    private FirebaseAuth mAuth;

    private static final String TAG = "EmailPassword";

    Button signIn;
    Button register;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signIn = findViewById(R.id.buttonSignIn);
        register = findViewById(R.id.buttonRegister);
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);


        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String emailStore;
                String passStore;
                emailStore = email.getText().toString();
                passStore = password.getText().toString();

                //if username and password non-null
                //can be modified to add security constraints in future
                if(emailStore != "" && passStore != ""){
                    mAuth.signInWithEmailAndPassword(emailStore, passStore)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(DummySignIn.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String emailStore;
                String passStore;
                emailStore = email.getText().toString();
                passStore = password.getText().toString();


                //getting data entry fields
                //TO-DO once UI complete!




                //Validating data entry fields
                //TO-DO once UI complete




                //creating user with valid data





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

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(DummySignIn.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    //an important method that will grab the UID from the auth object.
    public String getUserID(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser.getUid();
    }

    public void changeEmail(String email){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUser.updateEmail(email);
    }

    public void changePassword(String password){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUser.updatePassword(password);
    }


}