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
import android.widget.Button;
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
        setUserHabit(currentUserID);

        habitAdapter = new AllHabitList_Adapter(this, habitDataList);
        allhabitListView.setAdapter(habitAdapter);




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

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllHabitListView.this,AllHabitListView.class);
                intent.putExtra("currentUserID",currentUserID);
                intent.putExtra("currentTag",currentTag);
                startActivity(intent);
            }
        });


        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddHabit = new Intent(AllHabitListView.this,AddHabit.class);
                startActivity(toAddHabit);
            }
        });
    }


    public void setUserHabit(String ID){
        final CollectionReference habitRef = db.collection("Users").document(ID).collection("Habits");
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
    }

}