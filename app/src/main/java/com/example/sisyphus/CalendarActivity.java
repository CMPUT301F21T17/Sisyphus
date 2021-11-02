package com.example.sisyphus;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {
    private FirebaseAuth mauth;
    private Timestamp index;
    private Calendar selectedDay;

    CalendarView calendar;
    ListView habitsView;
    ArrayList<Habit> data;
    ArrayAdapter<Habit> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_page);

        calendar = findViewById(R.id.calendar);
        habitsView = findViewById(R.id.calendar_events);

        selectedDay = new GregorianCalendar();
        selectedDay.set(Calendar.HOUR,0);
        selectedDay.set(Calendar.MINUTE,0);
        selectedDay.set(Calendar.SECOND, 0);
        index = new Timestamp(selectedDay.getTime());

        data = new ArrayList<Habit>();
        adapter = new AllHabitList_Adapter(this, data);
        habitsView.setAdapter(adapter);

        // event listener to get up to date data for Habit list
        mauth = FirebaseAuth.getInstance();
        mauth.signInWithEmailAndPassword("ktbrown@ualberta.ca", "123456");
        update(mauth.getUid());

        // click listener for changes to calendar widget
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            // get events from database for day year month day and populate habitsView
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                selectedDay.set(year, month - 1, day, 0, 0, 0);
                // compare date_string to date field of calendar ref to get the correct group
                update(mauth.getUid());
            }
        });
    }

    public void update(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(mauth.getUid())
                .collection("Habits")
                .whereGreaterThanOrEqualTo("date", index)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        data.clear();
                        if (value != null) {
                            for (QueryDocumentSnapshot d: value) {
                                // TODO adapt data to Habit list
                                Habit temp = d.toObject(Habit.class);
                                if (temp.getDaysRepeated().contains(DayOfWeek.of(selectedDay.get(Calendar.DAY_OF_WEEK)))) {
                                    data.add(temp);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
