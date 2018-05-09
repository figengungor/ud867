package com.udacity.gradle.builditbigger;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by figengungor on 5/8/2018.
 */

//By default, Espresso synchronizes all view operations with the UI thread as well as AsyncTasks
//https://developer.android.com/reference/android/support/test/espresso/IdlingResource

@RunWith(AndroidJUnit4.class)
public class FetchJokesMainActivityTest {

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @Test
    public void clickTellAJokeBtn_DisplayInterstitialAdTest() {

        //Click joke button
        onView(withId(R.id.tellAJokeBtn)).perform(click());

        // Click close interstitial button
        ViewInteraction closeInterstitialButton = onView(
                allOf(withContentDescription("Interstitial close button"), isDisplayed()));
        closeInterstitialButton.perform(click());

        // Confirm that we're on the second activity
        onView(withId(R.id.jokeTv))
                .check(matches(isDisplayed()));
    }

}
