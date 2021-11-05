package com.example.sisyphus;
import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;import java.util.Random;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;
import com.example.sisyphus.View.Entry;
import com.example.sisyphus.View.MainActivity;
import com.example.sisyphus.View.Register;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;



/**
 * Test class for SignIn and Register. All the UI tests are written here. Robotium test framework is
 used
 */

//@RunWith(AndroidJUnit4.class)
public class UserIdentifyTest {
    Random rand = new Random();
    Integer randomIn = rand.nextInt(100000);
    private Solo solo;
    private String randomEmail = randomIn.toString() + "@email.com";

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
     * Try to sign up with user's input.
     * Only valid on first time!!!
     * Try new registerEmail for new test or it will fail.
     */
    @Test
    public void signUp(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign Up");
        solo.enterText((EditText) solo.getView(R.id.registerFirstName),"Sign");
        solo.enterText((EditText) solo.getView(R.id.registerLastName),"Up");
        solo.enterText((EditText) solo.getView(R.id.registerEmail),randomEmail);
        solo.enterText((EditText) solo.getView(R.id.registerPassword),"123456");
        solo.enterText((EditText) solo.getView(R.id.registerPasswordConfirm),"123456");
        solo.clickOnButton("Confirm");
        assertTrue(solo.waitForText("Homepage",1,2000));
    }


    /**
     * Try to sign in by a given user information
     */
    @Test
    public void checkSignIn1(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"junrui@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        assertTrue(solo.waitForText("Homepage",1,2000));
    }

    /**
     * Try to sign in by a given user information
     */
    @Test
    public void checkSignIn2(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@email.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        assertTrue(solo.waitForText("Homepage",1,2000));
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}

