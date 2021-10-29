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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AllHabitListView extends AppCompatActivity {
    ListView allhabitListView;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;
    final String TAG = "Sample";
    String []habit_title ={"Swimming","Working out","Sleeping","Flying","Studying"};
    String []habit_date ={"2000/12/03","2000/12/04","2000/12/05","2000/12/06","2000/12/07"};
    String []habit_reason ={"Swimming","Working out","Sleeping","Flying","Studying"};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit_list);

        Intent intent = getIntent();
        allhabitListView= findViewById(R.id.allhabit_list);

        habitDataList = new ArrayList<>();

        for(int i=0;i<habit_title.length;i++){
            habitDataList.add((new Habit(habit_title[i], habit_date[i],habit_reason[i])));
        }

        habitAdapter = new AllHabitList_Adapter(this, habitDataList);
        allhabitListView.setAdapter(habitAdapter);

        /*-----------------------------------Test searching--------------------------------------------------*/
        //Add some habits for test
        for(int i = 0;i<habit_title.length;i++){
            addHabit(TAG,i);
        }
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc:value){
                    Log.d(TAG,String.valueOf(doc.getData()));
                    String habit = doc.getId();
                    //String province = (String) doc.getData()
                }
            }
        });

        /*---------------------------------------------------------------------------------------------------*/

        allhabitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (AllHabitListView.this,ViewHabit.class);
                intent.putExtra("title",habit_title[i]);
                intent.putExtra("date",habit_date[i]);
                intent.putExtra("reason",habit_reason[i]);
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

    public void addHabit(String TAG,int index) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        String userID = "Junrui's Test";
        Habit testHabit = new Habit(habit_title[index],habit_reason[index],habit_date[index]);
        collectionReference
                .document(userID).collection("Habits").document(habit_title[index])
                .set(testHabit)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Habit has been added successfully");
                    }
                });
    }



}