package com.example.pursuit;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MessagesActivityTestUI {
    @Rule
    public ActivityTestRule<MessagesActivity> activityRule = new ActivityTestRule<>(MessagesActivity.class);

    @Test
    public void test_message_button(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.sendMessageBtn)).check(matches(isDisplayed()));
    }

}