package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class EmptyMainMenu extends AppCompatActivity {
    final String TAG = "Sample";
    String []habit_title ={"Eating","Working out","Sleeping","Flying","Studying"};
    String []habit_date ={"2000/12/03","2000/12/04","2000/12/05","2000/12/06","2000/12/07"};
    String []habit_reason ={"Eating","Working out","Sleeping","Flying","Studying"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_main_menu);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        //Adding habit by hand
        /*
        for(int i = 0;i<habit_title.length;i++){
            FirebaseStore fb = new FirebaseStore();
            Habit testHabit = new Habit(habit_title[i],habit_date[i],habit_reason[i]);
            fb.storeHabit(userID,testHabit);
        }*/
        //System.out.println(mAuth.getCurrentUser().getUid());

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this,AllHabitListView.class);
                intent.putExtra("currentUserID",userID);
                intent.putExtra("currentTag",TAG);
                startActivity(intent);
            }
        });

        //FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //System.out.println(mAuth.getCurrentUser().getUid());

    }

    /*public void addHabit(String TAG,int index,String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        Habit testHabit = new Habit(habit_title[index],habit_date[index],habit_reason[index]);
        collectionReference
                .document(userID).collection("Habits").document(habit_title[index])
                .set(testHabit)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Habit has been added successfully");
                    }
                });

    }*/
}