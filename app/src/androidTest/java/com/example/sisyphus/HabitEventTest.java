package com.example.sisyphus;
import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sisyphus.View.AllHabitListView;
import com.example.sisyphus.View.Entry;
import com.example.sisyphus.View.ListHabitEvent;
import com.example.sisyphus.View.MainActivity;
import com.example.sisyphus.View.Register;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

public class HabitEventTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Try to add habit from the habit list view
     */
    @Test
    public void addHabitEvent(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@email.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        solo.clickOnView(solo.getView(R.id.allhabitlist_button));
        assertTrue(solo.waitForText("My habits",1,2000));
        ListView habitList = (ListView) solo.getView(R.id.allhabit_list);
        View habitItem  = habitList.getChildAt(0);
        solo.clickOnView(habitItem);
        assertTrue(solo.waitForText("Habit's detail"));
        solo.clickOnButton("Add Habit Event");
        assertTrue(solo.waitForText("PHOTO TO GO HERE!",1,2000));
        solo.enterText((EditText) solo.getView("editTextLocation"), "testHome");
        solo.enterText((EditText) solo.getView("editTextComment"), "testComment");
        solo.enterText((EditText) solo.getView("editTextDate"), "01/11/2021");
        solo.clickOnButton("OK");
        solo.clickOnButton("Add");
        assertTrue(solo.waitForText("testHome",1,2000));
    }


    /**
     * Try to edit habit from the habit list view
     */
    @Test
    public void editHabitEvent(){
        addHabitEvent();
        solo.assertCurrentActivity("Wrong Activity",ListHabitEvent.class);
        ListView habitEventList = (ListView) solo.getView(R.id.list_habit_event);
        View eventItem  = habitEventList.getChildAt(0);
        solo.clickOnView(eventItem);
        solo.clickOnButton("Edit");
        solo.enterText((EditText) solo.getView("editTextComment"), "EditComment");
        solo.clickOnButton("Add");
        assertTrue(solo.waitForText("EditComment",1,2000));
    }

    /**
     * Try to delete a habit event from the habit list view
     */
    @Test
    public void deleteHabit(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@email.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        solo.clickOnView(solo.getView(R.id.allhabitlist_button));
        assertTrue(solo.waitForText("My habits",1,2000));
        ListView habitList = (ListView) solo.getView(R.id.allhabit_list);
        View habitItem  = habitList.getChildAt(0);
        solo.clickOnView(habitItem);
        assertTrue(solo.waitForText("Habit's detail"));
        solo.clickOnButton("View Habit Event");
        ListView habitEventList = (ListView) solo.getView(R.id.list_habit_event);
        View eventItem  = habitList.getChildAt(0);
        solo.clickOnView(eventItem);
        solo.clickOnButton("Delete");
        solo.clickOnButton("Yes");
        assertFalse(solo.searchText("EditComent"));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
