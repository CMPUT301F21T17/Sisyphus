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
import android.widget.TextView;

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
    TextView topBarTitle;
    ArrayList<String> habitEventID;
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
        String currentHabit = intent.getStringExtra("1");
        listHabitEvent= findViewById(R.id.list_habit_event);
        topBarTitle = findViewById(R.id.habit_event_title);

        mAuth = FirebaseAuth.getInstance();

        habitEventDataList = new ArrayList<>();
        habitEventID = new ArrayList<>();
        habitEventAdapter = new HabitEventListAdapter(this, habitEventDataList);
        listHabitEvent.setAdapter(habitEventAdapter);

        topBarTitle.setText("HabitEvents of [" + currentHabit+"]");

        setUserHabitEvent(mAuth.getUid(), currentHabit);



        listHabitEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (ListHabitEvent.this,ViewHabitEvent.class);
                HabitEvent clickedHabitEvent = habitEventDataList.get(i);
                intent.putExtra("habit_eventID", habitEventID.get(i));
                //intent.putExtra("tag",currentTag);
                intent.putExtra("user",mAuth.getUid());
                intent.putExtra("habit_event",clickedHabitEvent);
                startActivity(intent);
            }
        });

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHabitEvent.this,AllHabitListView.class);
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

    /**
     * Insert all habit's events and their documentID into lists
     * @param ID
     * The userID of the user to store data under
     * @param habitName
     * The habitName of the user to store habit events under
     */
    public void setUserHabitEvent(String ID, String habitName){
        final CollectionReference habitEventRef = db.collection("Users").document(ID).collection("Habits").document(habitName).collection("HabitEvent");
        habitEventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                habitEventDataList.clear();
                habitEventID.clear();
                for(QueryDocumentSnapshot doc:value){
                    HabitEvent result = doc.toObject(HabitEvent.class);
                    habitEventID.add(doc.getId());
                    habitEventDataList.add(result);
                }
                habitEventAdapter.notifyDataSetChanged();
            }
        });
    }

}