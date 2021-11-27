package com.example.sisyphus;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.annotation.FractionRes;
import androidx.annotation.NonNull;

import com.example.sisyphus.Model.FirebaseStore;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;



public class FirebaseStoreClassTest {
    //private FirebaseAuth mAuth = FirebaseAuth.getInstance()

    String PROJECT_ID = "sisyphus-846e7";

    private User testUser(){
        User user = new User();
        user.setFirst("Test");
        user.setLast("User");
        user.setDateJoined(new Date());
        return user;
    }

    @Test
    public void testStoreUser(){

        FirebaseStore store = new FirebaseStore();

        User testUser = testUser();
        Random random = new Random();
        String randEmail = Integer.toString(random.nextInt());
        String randPass = Integer.toString(random.nextInt());

        store.storeUser(randEmail, testUser);


        /*mAuth.signInWithEmailAndPassword(randEmail, randPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseStore store = new FirebaseStore();
                            store.storeUser(user.getUid(), testUser);

                        }
                    }
                });
         */


    }

}
