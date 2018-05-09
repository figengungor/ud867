package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by figengungor on 5/9/2018.
 */
//https://medium.com/@v.danylo/simple-way-to-test-asynchronous-actions-in-android-service-asynctask-thread-rxjava-etc-d43b0402e005
@RunWith(AndroidJUnit4.class)
public class AsyncTaskCallbackMainActivityTest {

    private static final String JOKE = "Joke";
    private static final String ERROR = "Error";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void fetchJokesOnSuccess_JokeIsDisplayedInNewActivity() {

        MainActivity mainActivity = mActivityTestRule.getActivity();
        mainActivity.listener.onSuccess(JOKE);

        onView(withId(R.id.jokeTv)).check(matches(withText(JOKE)));

    }

    @Test
    public void fetchJokesOnError_ErrorIsDisplayedWithToast() {

        MainActivity mainActivity = mActivityTestRule.getActivity();
        mainActivity.listener.onError(ERROR);

        onView(withText(ERROR)).inRoot(withDecorView((is(not(mainActivity.getWindow().getDecorView()))))).check(matches(isDisplayed()));

    }
}
