package com.example.ready2eat;


import androidx.test.espresso.Espresso;

import androidx.test.espresso.action.TypeTextAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.ready2eat.View.Home;
import com.example.ready2eat.View.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnSignIn)).perform(click());
        intended(hasComponent(Home.class.getName()));

    }

    @After
    public void end() throws Exception{
        Intents.release();
    }
}
