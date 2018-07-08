package com.github.client;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.github.client.activity.AuthActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 */
@RunWith(AndroidJUnit4.class)
public class AuthInstrumentedTest {

    @Rule
    public ActivityTestRule<AuthActivity> mActivityAuthRule = new ActivityTestRule<>(
            AuthActivity.class, true, false);


    @Before
    public void setup(){
        GitApplication.getInstance().logout();
    }


    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.schibsted.blockapp", appContext.getPackageName());
    }

    @Test
    public void authenticationMissing() throws Exception {
        mActivityAuthRule.launchActivity(new Intent());
        onView(isRoot()).perform(waitFor(3000));
        onView(withId(R.id.username)).perform(typeText("name"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());

        onView(withText(R.string.error_missing_credentials)).check(matches(isDisplayed()));
        onView(withText("Ok")).perform(click());
        assertNull(GitApplication.getInstance().getToken());
    }

    @Test
    public void authenticationFailure() throws Exception {
        mActivityAuthRule.launchActivity(new Intent());
        onView(isRoot()).perform(waitFor(3000));
        onView(withId(R.id.username)).perform(typeText("name"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("pass"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());

        onView(withText(R.string.error_invalid_auth_body_1)).check(matches(isDisplayed()));
        onView(withText("Ok")).perform(click());
        assertNull(GitApplication.getInstance().getToken());
    }



    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }


}
