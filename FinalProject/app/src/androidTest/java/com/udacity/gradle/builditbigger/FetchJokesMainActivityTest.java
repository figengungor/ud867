package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by figengungor on 5/8/2018.
 */

//By default, Espresso synchronizes all view operations with the UI thread as well as AsyncTasks
//https://developer.android.com/reference/android/support/test/espresso/IdlingResource
//https://medium.com/@v.danylo/simple-way-to-test-asynchronous-actions-in-android-service-asynctask-thread-rxjava-etc-d43b0402e005

@RunWith(AndroidJUnit4.class)
public class FetchJokesMainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private static final String JOKE = "Joke";
    private static final String ERROR = "Error";

    @Test
    public void fetchJokesTest() {

        MainActivity mainActivity = mActivityTestRule.getActivity();
        mainActivity.setListener(new FetchJokeTask.Listener() {
            @Override
            public void onSuccess(String joke) {
                assertTrue(!TextUtils.isEmpty(joke));
            }

            @Override
            public void onError(String error) {
                assertTrue(!TextUtils.isEmpty(error));
            }
        });

        onView(withId(R.id.tellAJokeBtn)).perform(click());

    }

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
