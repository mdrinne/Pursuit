package com.example.pursuit;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class CompanyRegistrationTestUI {

    @Rule
    public ActivityTestRule<CompanyRegistration> activityRule = new ActivityTestRule<>(CompanyRegistration.class);

    @Test
    public void test_register_company() {
        onView(withId(R.id.companyField)).perform(typeText("field"), closeSoftKeyboard());
        onView(withId(R.id.companyPassword)).perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.companyName)).perform(typeText("CompanyName"), closeSoftKeyboard());
        //must change email below after each test
        onView(withId(R.id.companyEmail)).perform(typeText("CompanyEmail14@google.com"), closeSoftKeyboard());
        onView(withId(R.id.companyReEnterPassword)).perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.currentUserName)).check(matches(isDisplayed()));
    }


}