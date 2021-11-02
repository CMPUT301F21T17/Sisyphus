package com.example.sisyphus.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sisyphus.Model.AllHabitList_Adapter;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AllHabitListView extends AppCompatActivity {
    private ListView allhabitListView;
    private ArrayAdapter<Habit> habitAdapter;
    private ArrayList<Habit> habitDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    final CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit_list);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String currentUserID = mAuth.getUid();
        allhabitListView= findViewById(R.id.allhabit_list);
        habitDataList = new ArrayList<>();

        setUserHabit(currentUserID);

        habitAdapter = new AllHabitList_Adapter(this, habitDataList);
        allhabitListView.setAdapter(habitAdapter);




        allhabitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (AllHabitListView.this, ViewHabit.class);
                Habit clickedHabit = habitDataList.get(i);
                intent.putExtra("habit",clickedHabit);
                startActivity(intent);
            }
        });

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllHabitListView.this,AllHabitListView.class);
                startActivity(intent);
            }
        });


        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddHabit = new Intent(AllHabitListView.this, AddHabit.class);
                startActivity(toAddHabit);
            }
        });
    }

    /**
     * Insert all habits from firebase into a list
     * @param ID
     * The userID of the user to store data under
     */
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