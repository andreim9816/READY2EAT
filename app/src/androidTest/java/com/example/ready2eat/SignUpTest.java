package com.example.ready2eat;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.TypeTextAction;
import androidx.test.espresso.intent.Intents;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

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
public class SignUpTest {

    String number = "0734567899";

    @Before
    public void setUp() throws Exception{
        Intents.init();
    }
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule(MainActivity.class);

    @Test
    public void successfulSigup() {

        String userName = "George Bonea";
        String password = "George";

        typeFields(userName, password, number); // types in the fields for the Sign Up part

        onView(withId(R.id.edtPhone)).perform(new TypeTextAction(number));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.edtPassword)).perform(new TypeTextAction(password));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.btnSignIn)).perform(click());

        intended(hasComponent(Home.class.getName()));
    }

    @Test
    public void wrongNumberOfDigits()
    {
        String userName = "George Bonea";
        String password = "Parola";
        String number = "067";

        typeFields(userName, password, number);
        onView(withText("Numarul trebuie sa aiba 10 cifre!")).inRoot(withDecorView(not(rule
                .getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void wrongFirstDigits()
    {
        String userName = "George Bonea";
        String password = "Parola";
        String number = "0041130693";

        typeFields(userName, password, number);
        onView(withText("Numarul trebuie sa inceapa cu 07...")).inRoot(withDecorView(not(rule
                .getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void wrongCharacters()
    {
        String userName = "George Bonea";
        String password = "Parola";
        String number = "07--13./93";

        typeFields(userName, password, number);
        onView(withText("Numarul trebuie sa contina doar cifre!")).inRoot(withDecorView(not(rule
                .getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void numberNotInserted()
    {
        String userName = "George Bonea";
        String password = "Parola";
        String number = "";

       typeFields(userName, password, number);
        onView(withText("Introdu un numar de telefon!")).inRoot(withDecorView(not(rule
                .getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void nameNotInserted()
    {
        String userName = "";
        String password = "Parola";
        String number = "0741130202";

        typeFields(userName, password, number);
        onView(withText("Introdu un nume!")).inRoot(withDecorView(not(rule
                .getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void passwordNotInserted()
    {
        String userName = "George Bonea";
        String password = "";
        String number = "0741130202";

        typeFields(userName, password, number);
        onView(withText("Introdu o parola!")).inRoot(withDecorView(not(rule
                .getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void shortPassword()
    {
        String userName = "George Bonea";
        String password = "14ss-";
        String number = "0741130202";

        typeFields(userName, password, number);
        onView(withText("Introdu o parola de cel putin 6 caractere!")).inRoot(withDecorView(not(rule
                .getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void shortName()
    {
        String userName = "Geor";
        String password = "parola";
        String number = "0741130202";

        typeFields(userName, password, number);
        onView(withText("Introdu un nume de cel putin 6 litere!")).inRoot(withDecorView(not(rule
                .getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }


    @After
    public void end() {
        Intents.release();
        cleanUp();
    }


    public void typeFields(String userName, String password, String number)
    {
        onView(withId(R.id.btnSignUp)).perform(click());
        onView(withId(R.id.edtName)).perform(new TypeTextAction(userName));

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.edtPhone)).perform(new TypeTextAction(number));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.edtPassword)).perform(new TypeTextAction(password));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.btnSignUp)).perform(click());
    }


    public void cleanUp()
    {
        // removes the registred user from database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference requests = database.getReference("User");
        requests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for(DataSnapshot d : dataSnapshot.getChildren())
                    if(d.getKey().equals(number))
                        d.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}


