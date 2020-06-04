package com.example.ready2eat;

import android.content.Context;

import androidx.annotation.ContentView;
import androidx.test.espresso.action.TypeTextAction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<SignIn> rule = new ActivityTestRule(SignIn.class);
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.ready2eat", appContext.getPackageName());
    }

    @Test
    public void user_can_enter_name() {
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0"));
    }

    @Test
    public void log_in_successful() {
        onView(withId(R.id.edtPhone)).perform(new TypeTextAction("0"));
        onView(withId(R.id.edtPassword)).perform(new TypeTextAction("test"));
    }
}
