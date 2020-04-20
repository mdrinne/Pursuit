package com.example.pursuit;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class aboutPursuitActivityTestUI {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test_about_pursuit() {
        onView(withId(R.id.txtUsernameEmail)).perform(typeText("big@company.com"), closeSoftKeyboard());
        onView(withId(R.id.txtPassword)).perform(typeText("bigcompany"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.aboutPursuitBtn)).perform(click());
        onView(withId(R.id.backToLandingBtn)).check(matches(isDisplayed()));
    }

}