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

/**
 * Test class for AllHabitListView,AddHabit and HabitController. All the UI tests are written here. Robotium test framework is
 used
 */
public class HabitListTest {
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
     * Try to add a habit from the habit list view
     */
    @Test
    public void addHabit(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@email.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        solo.clickOnView(solo.getView(R.id.allhabitlist_button));
        assertTrue(solo.waitForText("My habits",1,2000));
        solo.clickOnView(solo.getView(R.id.add_habit_button));
        assertTrue(solo.waitForText("Add/Edit Habit",1,2000));
        solo.enterText((EditText) solo.getView("habitNameInput"), "addHabitTest");
        solo.enterText((EditText) solo.getView("startDate"), "01/11/2021");
        solo.clickOnButton("OK");
        solo.enterText((EditText) solo.getView("reasonInput"), "addHabitTest");
        int[] butPosition = new int[2];
        View check = solo.getView("checkButton");
        check.getLocationOnScreen(butPosition);
        solo.clickOnScreen(butPosition[0],butPosition[1]);
        assertTrue(solo.waitForText("addHabitTest",1,2000));
    }

    /**
     * Try to edit a habit from the habit list view
     */
    @Test
    public void editHabit(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@email.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        solo.clickOnView(solo.getView(R.id.allhabitlist_button));
        assertTrue(solo.waitForText("My habits",1,2000));
        ListView habitList = (ListView) solo.getView(R.id.allhabit_list);
        View item  = habitList.getChildAt(0);
        solo.clickOnView(item);
        solo.clickOnButton("Edit Habit");
        solo.enterText((EditText) solo.getView("reason"), "edit");
        solo.clickOnButton("Confirm");
        assertTrue(solo.waitForText("edit",1,2000));
    }

    /**
     * Try to delete a habit from the habit list view
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
        View item  = habitList.getChildAt(0);
        solo.clickOnView(item);
        solo.clickOnButton("Delete Habit");
        solo.clickOnButton("Yes");
        assertFalse(solo.searchText("addHabitTest1"));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
