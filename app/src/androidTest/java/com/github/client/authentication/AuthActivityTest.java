package com.github.client.authentication;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.client.R;
import com.github.client.storage.Storage;
import com.github.client.storage.StorageImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class AuthActivityTest {

    @Rule
    public ActivityTestRule<AuthActivity> mActivityAuthRule = new ActivityTestRule<>(
            AuthActivity.class, true, false);

    private Storage storage;

    @Before
    public void setup() {
        storage = new StorageImpl(InstrumentationRegistry.getTargetContext());
        storage.clearAccount();
        mActivityAuthRule.launchActivity(new Intent());
    }

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.github.client", appContext.getPackageName());
    }

    @Test
    public void authenticationMissing() {
        onView(withId(R.id.username)).perform(typeText("name"), closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());

        onView(withText(R.string.error_missing_credentials)).check(matches(isDisplayed()));
        onView(withText("Ok")).perform(click());
        assertNull(storage.fetchToken());
    }

    @Test
    public void authenticationFailure() {
        onView(withId(R.id.username)).perform(typeText("name"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());

        onView(withText(R.string.error_invalid_auth_body_1)).check(matches(isDisplayed()));
        onView(withText("Ok")).perform(click());
        assertNull(storage.fetchToken());
    }


}
