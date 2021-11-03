package com.example.sisyphus;

import static java.util.List.of;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "Sample";
    private FirebaseAuth mauth;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Date now = new Date();
        List<String> daysRepeated = new ArrayList<>();
        daysRepeated.add("TUESDAY");
        daysRepeated.add("WEDNESDAY");
        Habit habit = new Habit("Kaelen", now, "test", daysRepeated);

        mauth = FirebaseAuth.getInstance();
        mauth.signInWithEmailAndPassword("ktbrown@ualberta.ca", "123456");
        String user = mauth.getUid();

        FirebaseFirestore.getInstance().collection("Users")
                //habit given a database name the same as the title given to it by a user
                .document(user).collection("Habits").document(habit.getName()).set(habit)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }

                });
        Intent i = new Intent(MainActivity.this, CalendarActivity.class);
        startActivity(i);


    }
}