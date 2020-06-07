package com.example.ready2eat;

import androidx.test.espresso.Espresso;
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

@RunWith(AndroidJUnit4.class)
public class AddToCartTest {
    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Test
    public void addToCart() {
        //log in, add a product in cart and checks if the message is displayed
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("test"));
        onView(withId(R.id.btnSignIn)).perform(click());
        intended(hasComponent(Home.class.getName()));
        onView(withId(R.id.recycler_menu)).perform(click());
        intended(hasComponent(FoodList.class.getName()));
        onView(withId(R.id.recycler_food)).perform(click());
        intended(hasComponent(FoodDetail.class.getName()));
        onView(withText("+")).perform(click());
        onView(withId(R.id.btnCart)).perform(click());
        onView(withText("Adaugat in cos")).inRoot(withDecorView(not(rule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @After
    public void end() throws Exception {
        Intents.release();
    }
}