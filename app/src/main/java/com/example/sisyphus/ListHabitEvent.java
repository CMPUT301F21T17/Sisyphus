package com.example.sisyphus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ListHabitEvent extends AppCompatActivity {
    ListView listHabitEvent;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_habit_event);

        Intent intent = getIntent();
        //String currentUserID = intent.getStringExtra("currentUserID");
        String currentHabit = intent.getStringExtra("1");
        listHabitEvent= findViewById(R.id.list_habit_event);

        mAuth = FirebaseAuth.getInstance();

        habitEventDataList = new ArrayList<>();

        /*for(int i=0;i<habit_title.length;i++){
            habitDataList.add((new Habit(habit_title[i], habit_date[i],habit_reason[i])));
        }*/
        habitEventAdapter = new HabitEventListAdapter(this, habitEventDataList);
        listHabitEvent.setAdapter(habitEventAdapter);

        setUserHabitEvent(mAuth.getUid(), currentHabit);


        listHabitEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (ListHabitEvent.this,ViewHabitEvent.class);
                HabitEvent clickedHabitEvent = habitEventDataList.get(i);
                intent.putExtra("habit_event",clickedHabitEvent);
                //intent.putExtra("tag",currentTag);
                intent.putExtra("user",mAuth.getUid());
                startActivity(intent);
            }
        });


        final FloatingActionButton addHabitEventButton = findViewById(R.id.add_habit_event);
        addHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addHabit = new Intent(view.getContext(), AddHabitEvent.class);
                addHabit.putExtra("1", currentHabit);
                startActivity(addHabit);
            }
        });
    }

    public void setUserHabitEvent(String ID, String habitName){
        final CollectionReference habitEventRef = db.collection("Users").document(ID).collection("Habits").document(habitName).collection("HabitEvent");
        habitEventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                habitEventDataList.clear();
                for(QueryDocumentSnapshot doc:value){
                    HabitEvent result = doc.toObject(HabitEvent.class);
                    habitEventDataList.add(result);
                }
                habitEventAdapter.notifyDataSetChanged();
            }
        });
    }

}