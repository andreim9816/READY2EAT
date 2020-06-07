package com.example.ready2eat;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.TypeTextAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class SendAnOrderTest{

    @Before
    public void setUp() throws Exception {
        Intents.init();

    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Test
    public void sendAnOrderSuccessfully() {

        // sign in
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnSignIn)).perform(click());

        // choose a category and an item
        intended(hasComponent(Home.class.getName()));
        onView(withId(R.id.recycler_menu)).perform(click());
        intended(hasComponent(FoodList.class.getName()));
        onView(withId(R.id.recycler_food)).perform(click());
        intended(hasComponent(FoodDetail.class.getName()));

        // select the quantity
        onView(withText("+")).perform(click());
        onView(withText("+")).perform(click());
        onView(withText("+")).perform(click());
        onView(withId(R.id.btnCart)).perform(click());

        // back to menu
        Espresso.pressBack();
        Espresso.pressBack();

        // press the cart button
        onView(withId(R.id.fab)).perform(click());
        intended(hasComponent(Cart.class.getName()));
        onView(withId(R.id.btnPlaceOrder)).perform(click());

        onView(withText(R.string.last_step)).check(matches(isDisplayed()));

        // calculate the pickup time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleformat = new SimpleDateFormat("HH:mm");
        calendar.add(Calendar.HOUR_OF_DAY, + 2);

        Date finishTime = calendar.getTime();
        String pickUpTime = simpleformat.format(finishTime);

        ViewInteraction editText = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.custom),
                                childAtPosition(
                                        withId(R.id.customPanel),
                                        0)),
                        0),
                        isDisplayed()));

        editText.perform(replaceText(pickUpTime), closeSoftKeyboard());

        onView(withText(R.string.confirm_hour)).perform(scrollTo(), click());
    }

    @Test
    public void sendAnOrderIncompatibleFormat() {

        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnSignIn)).perform(click());
        intended(hasComponent(Home.class.getName()));

        onView(withId(R.id.recycler_menu)).perform(click());
        intended(hasComponent(FoodList.class.getName()));
        onView(withId(R.id.recycler_food)).perform(click());
        intended(hasComponent(FoodDetail.class.getName()));

        onView(withText("+")).perform(click());
        onView(withText("+")).perform(click());
        onView(withId(R.id.btnCart)).perform(click());

        Espresso.pressBack();
        Espresso.pressBack();

        onView(withId(R.id.fab)).perform(click());
        intended(hasComponent(Cart.class.getName()));
        onView(withId(R.id.btnPlaceOrder)).perform(click());

        onView(withText(R.string.last_step)).check(matches(isDisplayed()));

        ViewInteraction editText = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.custom),
                                childAtPosition(
                                        withId(R.id.customPanel),
                                        0)),
                        0),
                        isDisplayed()));
        editText.perform(replaceText("9"), closeSoftKeyboard());

        onView(withText(R.string.confirm_hour)).perform(scrollTo(), click());

        onView(withText("Formatul orei este incorect!")).inRoot(withDecorView(
                not(rule.getActivity().
                        getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void sendAnOrderUnsuccessful() {

        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnSignIn)).perform(click());
        intended(hasComponent(Home.class.getName()));

        onView(withId(R.id.recycler_menu)).perform(click());
        intended(hasComponent(FoodList.class.getName()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recycler_food),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                1)));

        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add_btn), withText("+"),
                        childAtPosition(
                                allOf(withId(R.id.layout),
                                        childAtPosition(
                                                withId(R.id.number_button),
                                                0)),
                                2)));

        appCompatButton3.perform(scrollTo(), click());
        onView(withId(R.id.btnCart)).perform(click());

        Espresso.pressBack();
        Espresso.pressBack();

        onView(withId(R.id.fab)).perform(click());
        intended(hasComponent(Cart.class.getName()));
        onView(withId(R.id.btnPlaceOrder)).perform(click());

        onView(withText(R.string.last_step)).check(matches(isDisplayed()));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleformat = new SimpleDateFormat("HH:mm");
        calendar.add(Calendar.HOUR_OF_DAY, - 3);
        Date finishTime = calendar.getTime();
        String pickUpTime = simpleformat.format(finishTime);

        ViewInteraction editText = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.custom),
                                childAtPosition(
                                        withId(R.id.customPanel),
                                        0)),
                        0),
                        isDisplayed()));

        editText.perform(replaceText(pickUpTime), closeSoftKeyboard());

        onView(withText(R.string.confirm_hour)).perform(scrollTo(), click());
    }

    @After
    public void end() throws Exception {
        Intents.release();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}