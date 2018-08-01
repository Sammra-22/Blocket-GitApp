package com.github.client.account;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.client.R;
import com.github.client.api.AccountService;
import com.github.client.api.ApiManager;
import com.github.client.api.model.Repository;
import com.github.client.api.model.User;
import com.github.client.storage.Storage;
import com.github.client.storage.StorageImpl;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.client.utils.Global.INTENT_EXTRA_USER;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class AccountActivityTest {

    @Rule
    public ActivityTestRule<AccountActivity> mActivityMainRule = new ActivityTestRule<>(
            AccountActivity.class, true, false);

    private AccountService accountServiceMock;
    private Storage storage;
    private List<Repository> mockedRepositories = Arrays.asList(
            new Repository("testRepo1", "owner/testRepo1"),
            new Repository("testRepo2", "owner/testRepo2"),
            new Repository("testRepo3", "owner/testRepo3")
    );

    private Intent getLaunchIntent() {
        return new Intent().putExtra(INTENT_EXTRA_USER, new Gson()
                .toJson(new User("test", "test@test")));
    }

    @Before
    public void setup() {
        storage = new StorageImpl(InstrumentationRegistry.getTargetContext());
        accountServiceMock = mock(AccountService.class);
        ApiManager.getInstance(storage).setAccountService(accountServiceMock);
        when(accountServiceMock.getRepositories())
                .thenReturn(Single.just(Response.success(mockedRepositories)));
        mActivityMainRule.launchActivity(getLaunchIntent());
    }

    @Test
    public void testUserAccount() {
        onView(withId(R.id.name)).check(matches(withText("test")));
        onView(withId(R.id.mail)).check(matches(withText("test@test")));
        onView(withText(mockedRepositories.get(0).getName())).check(matches(isDisplayed()));
        onView(withText(mockedRepositories.get(1).getName())).check(matches(isDisplayed()));
        onView(withText(mockedRepositories.get(2).getName())).check(matches(isDisplayed()));
    }

    @Test
    public void testRepositoryDetails() {
        int itemPosition = 0;
        when(accountServiceMock.getRepoDetails(anyString(), anyString()))
                .thenReturn(Single.just(Response.success(mockedRepositories.get(itemPosition))));

        onView(withId(R.id.repo_list)).perform(actionOnItemAtPosition(itemPosition, click()));
        onView(allOf(withId(R.id.value), withText(mockedRepositories.get(itemPosition).getFullName())))
                .check(matches(isDisplayed()));
        onView(withText("Ok")).perform(click());
        onView(withId(R.id.name)).check(matches(isDisplayed()));
        onView(withId(R.id.mail)).check(matches(isDisplayed()));
    }

    @Test
    public void testSyncRepositories() {
        onView(withId(R.id.sync)).perform(click());
        onView(withText(R.string.action_sync_repos)).check(matches(isDisplayed()));
        verify(accountServiceMock, atLeastOnce()).getRepositories();
    }

    @Test
    public void testLogoutUser() {
        onView(withId(R.id.logout)).perform(click());
        onView(withId(R.id.login_form)).check(matches(isDisplayed()));
        assertNull(storage.fetchToken());
    }

}
