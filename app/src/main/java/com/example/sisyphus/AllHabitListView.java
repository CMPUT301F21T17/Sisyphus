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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AllHabitListView extends AppCompatActivity {
    ListView allhabitListView;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit_list);

        Intent intent = getIntent();
        String currentUserID = intent.getStringExtra("currentUserID");
        String currentTag = intent.getStringExtra("currentTag");
        allhabitListView= findViewById(R.id.allhabit_list);

        habitDataList = new ArrayList<>();

        /*for(int i=0;i<habit_title.length;i++){
            habitDataList.add((new Habit(habit_title[i], habit_date[i],habit_reason[i])));
        }*/
        habitAdapter = new AllHabitList_Adapter(this, habitDataList);
        allhabitListView.setAdapter(habitAdapter);

//-----------------------------------------Test searching-----------------------------------------------------------------------------------

        final CollectionReference habitRef = db.collection("Users").document(currentUserID).collection("Habits");
        habitRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc:value){
                    Habit result = doc.toObject(Habit.class);
                    habitDataList.add(result);
                }
                habitAdapter.notifyDataSetChanged();
            }
        });
//------------------------------------------------------------------------------------------------------------------------------------------





        allhabitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (AllHabitListView.this,ViewHabit.class);
                Habit clickedHabit = habitDataList.get(i);
                intent.putExtra("habit",clickedHabit);
                intent.putExtra("tag",currentTag);
                intent.putExtra("user",currentUserID);
                startActivity(intent);
            }
        });


        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent);
            }
        });
    }



}