package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AllHabitListView extends AppCompatActivity {
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit_list);

        Intent intent = getIntent();
        habitList = findViewById(R.id.habit_list);

        String []habit_title ={"Swimming","Working out","Sleeping","Flying","Studying"};
        String []habit_date ={"2000/12/03","2000/12/04","2000/12/05","2000/12/06","2000/12/07"};
        String []habit_reason ={"Swimming","Working out","Sleeping","Flying","Studying"};

        habitDataList = new ArrayList<>();

        for(int i=0;i<habit_title.length;i++){
            habitDataList.add((new Habit(habit_title[i], habit_date[i],habit_reason[i])));
        }

        habitAdapter = new AllHabitList_Adapter(this, habitDataList);

        habitList.setAdapter(habitAdapter);


        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent);
            }
        });
    }



}