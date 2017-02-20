package com.schibsted.blockapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.schibsted.blockapp.activity.MainActivity;
import com.schibsted.blockapp.model.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.schibsted.blockapp.AuthInstrumentedTest.waitFor;
import static com.schibsted.blockapp.utils.Global.INTENT_EXTRA_USER;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 */
@RunWith(AndroidJUnit4.class)
public class MainInstrumentedTest {

    final String BASIC_AUTH_TEST = "Basic c2FtbXJhMjI6ZGVmamFtMjI=";

    @Rule
    public ActivityTestRule<MainActivity> mActivityMainRule = new ActivityTestRule<MainActivity>(
            MainActivity.class, true, false);


    Intent launchIntent;

    @Before
    public void setup() throws Exception {
        GitApplication app = GitApplication.getInstance();
        app.setupRetrofit(BASIC_AUTH_TEST);
        launchIntent = new Intent();
        launchIntent.putExtra(INTENT_EXTRA_USER, new Gson().toJson(new User("test","test@test")));
    }


    @Test
    public void userAccount() throws Exception {
        mActivityMainRule.launchActivity(launchIntent);
        onView(isRoot()).perform(waitFor(3000));
        onView(withId(R.id.name)).check(matches(withText("test")));
        onView(withId(R.id.mail)).check(matches(withText("test@test")));
        onView(withText("TestRepo")).check(matches(isDisplayed()));
        onView(withId(R.id.repo_list)).perform(actionOnItemAtPosition(0, click()));
        onView(withText("TestRepo")).check(matches(isDisplayed()));
        onView(withText("Ok")).perform(click());
    }


    @Test
    public void sync() throws Exception {
        mActivityMainRule.launchActivity(launchIntent);
        onView(withId(R.id.sync)).perform(click());
        onView(withText(R.string.action_sync_repos)).check(matches(isDisplayed()));
    }


    @Test
    public void logout() throws Exception {
        mActivityMainRule.launchActivity(launchIntent);
        onView(withId(R.id.logout)).perform(click());
        onView(withId(R.id.login_form)).check(matches(isDisplayed()));
        assertNull(GitApplication.getInstance().getToken());
    }




}
