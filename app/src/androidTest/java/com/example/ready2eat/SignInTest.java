package com.example.ready2eat;

import androidx.test.espresso.action.TypeTextAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SignInTest {
    @Before
    public void setUp() throws Exception{
        Intents.init();
    }
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);
    @Test
    public void successfulLogin1() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0"));
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("test"));
        onView(withId(R.id.btnSignIn)).perform(click());
        intended(hasComponent(Home.class.getName()));
    }
    @Test
    public void loginAdmin() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0784310009"));
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("aplicatie"));
        onView(withId(R.id.btnSignIn)).perform(click());
        intended(hasComponent(AdminHome.class.getName()));
    }

    @Test
    public void unsuccessfulLogin1() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction(""));
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction(""));
        onView(withId(R.id.btnSignIn)).perform(click());
        //checks if Tost.makeText appears
        onView(withText("Introdu mai intai numar si parola")).inRoot(withDecorView(not(rule.getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void unsuccessfulLogin2() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.btnSignIn)).perform(click());
        //checks if Tost.makeText appears
        onView(withText("Introdu mai intai numar si parola")).inRoot(withDecorView(not(rule.getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void unsuccessfulLogin3() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0727874060"));
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("laura"));
        onView(withId(R.id.btnSignIn)).perform(click());
        //checks if Tost.makeText appears
        onView(withText("Parola gresita")).inRoot(withDecorView(not(rule.getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }
    @After
    public void end() throws Exception{
        Intents.release();
    }
}
