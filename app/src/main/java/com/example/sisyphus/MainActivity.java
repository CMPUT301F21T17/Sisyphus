package com.example.sisyphus;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class MainActivity extends AppCompatActivity {
    /*final String TAG = "Sample";
    String userID = "Junrui's Test";
    String []habit_title ={"Eating","Working out","Sleeping","Flying","Studying"};
    String []habit_date ={"2000/12/03","2000/12/04","2000/12/05","2000/12/06","2000/12/07"};
    String []habit_reason ={"Eating","Working out","Sleeping","Flying","Studying"};*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this,Entry.class);
        startActivity(intent);

        /*final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AllHabitListView.class);
                intent.putExtra("currentUserID",userID);
                intent.putExtra("currentTag",TAG);
                startActivity(intent);
            }
        });*/


        //Intent intent = new Intent(getApplicationContext(), Entry.class);
        //startActivity(intent);


        //final String TAG = "Sample";
        //addHabitEvent(TAG);



        //Date testDate = new Date();
        //FirebaseStore store = new FirebaseStore();
        //String dummyUID = "111111111";
        // User testUser = new User("New user", "New user last", testDate);
        //Habit testHabit = new Habit("Testing automated add class", "To see if it works", 1, testDate);
        //HabitEvent testEvent = new HabitEvent("did this work?", testDate);



        //store.storeUser(dummyUID, testUser);
        //store.storeHabit(dummyUID, testHabit);
        //store.storeHabitEvent(dummyUID, testHabit.getName(), testEvent);





        //Intent testSignIn = new Intent(this, DummySignIn.class);
        //startActivity(testSignIn);

    }


    //Leah's
    //        Intent intent = new Intent(getApplicationContext(), Entry.class);
    //        startActivity(intent);

    //New thing that I added for testing push now!

}