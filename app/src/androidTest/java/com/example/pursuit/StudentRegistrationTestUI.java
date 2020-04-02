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
public class StudentRegistrationTestUI {
    @Rule
    public ActivityTestRule<StudentRegistration> activityRule = new ActivityTestRule<>(StudentRegistration.class);

    @Test
    public void test_register_student(){
        onView(withId(R.id.txtFirstName)).perform(typeText("first"), closeSoftKeyboard());
        onView(withId(R.id.txtLastName)).perform(typeText("last"), closeSoftKeyboard());
        onView(withId(R.id.txtUniversity)).perform(typeText("uni"), closeSoftKeyboard());
        onView(withId(R.id.txtMajor)).perform(typeText("major"), closeSoftKeyboard());
        onView(withId(R.id.txtMinor)).perform(typeText("minor"), closeSoftKeyboard());
        onView(withId(R.id.txtStudentGPA)).perform(typeText("gpa"), closeSoftKeyboard());
        onView(withId(R.id.txtBio)).perform(typeText("bio"), closeSoftKeyboard());
        //must change username and email below after each test
        onView(withId(R.id.txtEmail)).perform(typeText("student11@google.com"), closeSoftKeyboard());
        onView(withId(R.id.txtUsername)).perform(typeText("usernamestudent11"), closeSoftKeyboard());
        onView(withId(R.id.txtPassword)).perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.txtReEnterPassword)).perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.currentUserName)).check(matches(isDisplayed()));
    }


}