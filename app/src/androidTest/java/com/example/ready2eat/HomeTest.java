package com.example.ready2eat;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;

import androidx.annotation.ContentView;
import androidx.test.espresso.action.TypeTextAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.hamcrest.Matcher;
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
import static org.junit.Assert.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class HomeTest {
    @Before
    public void setUp() throws Exception{
        Intents.init();
    }
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Test
    public void successfulLogin() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0"));
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("test"));
        onView(withId(R.id.btnSignIn)).perform(click());
        intended(hasComponent(Home.class.getName()));

    }

    @After
    public void end() throws Exception{
        Intents.release();
    }
}
