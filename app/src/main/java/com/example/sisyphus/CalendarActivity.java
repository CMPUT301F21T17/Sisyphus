package com.example.sisyphus;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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


        data = new ArrayList<Habit>();
        adapter = new AllHabitList_Adapter(this, data);
        habitsView.setAdapter(adapter);

        // event listener to get up to date data for Habit list
        mauth = FirebaseAuth.getInstance();
        mauth.signInWithEmailAndPassword("ktbrown@ualberta.ca", "123456");
        update(mauth.getUid(), selectedDay);

        // click listener for changes to calendar widget
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            // get events from database for day year month day and populate habitsView
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                selectedDay.set(year, month - 1, day, 0, 0, 0);
                // compare date_string to date field of calendar ref to get the correct group
                update(mauth.getUid(), selectedDay);
            }
        });
    }

    public void update(String userID, Calendar selectedDay) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(userID)
                .collection("Habits")
                .whereGreaterThanOrEqualTo("dateSort", selectedDay.getTime())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        data.clear();
                        if (value != null) {
                            for (QueryDocumentSnapshot d: value) {
                                // TODO adapt data to Habit list
                                Habit temp = d.toObject(Habit.class);
                                boolean add = false;
                                for (String e : temp.getDaysRepeated()){
                                    if (e.equals(CalToStr(selectedDay)));
                                        add = true;
                                        break;
                                }
                                if (add) {
                                    data.add(temp);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public String CalToStr (Calendar selectedDay) {
            int i = selectedDay.get(Calendar.DAY_OF_WEEK);
            switch (i) {
                case Calendar.SATURDAY:
                    return "SATURDAY";
                case Calendar.SUNDAY:
                    return "SUNDAY";
                case Calendar.MONDAY:
                    return "MONDAY";
                case Calendar.TUESDAY:
                    return "TUESDAY";
                case Calendar.WEDNESDAY:
                    return "WEDNESDAY";
                case Calendar.THURSDAY:
                    return "THURSDAY";
                case Calendar.FRIDAY:
                    return "FRIDAY";
            }
        return "ERROR";
    }
}
