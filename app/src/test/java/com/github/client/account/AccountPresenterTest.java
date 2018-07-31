package com.github.client.account;

import com.github.client.TestUtils;
import com.github.client.api.AccountService;
import com.github.client.api.model.Repository;
import com.github.client.storage.Storage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AccountPresenterTest {

    @Mock
    private
    AccountService mockAccountService;
    @Mock
    private
    Storage mockStorage;
    @Mock
    private
    AccountView mockView;

    private AccountPresenter presenter;
    private Repository mockRepository = new Repository("owner/repo");

    @Before
    public void setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
        presenter = new AccountPresenter(mockView, mockAccountService, mockStorage);
    }

    @Test
    public void testFetchRepositoriesFail() {
        when(mockAccountService.getRepositories()).
                thenReturn(Single.just(TestUtils.mockServerError()));

        presenter.fetchRepositories();

        verify(mockView).toggleRepositoriesView();
    }

    @Test
    public void testFetchRepositoriesUnauthorized() {
        when(mockAccountService.getRepositories()).
                thenReturn(Single.just(TestUtils.mockUnauthorizedError()));

        presenter.fetchRepositories();

        verify(mockStorage).clearAccount();
        verify(mockView).onUserLogout();
    }

    @Test
    public void testFetchRepositoriesSuccess() {
        when(mockAccountService.getRepositories()).
                thenReturn(Single.just(Response.success(new ArrayList<>())));

        presenter.fetchRepositories();

        verify(mockView).updateRepositories(anyListOf(Repository.class));
    }


    @Test
    public void testFetchRepositoryDetailsFail() {
        when(mockAccountService.getRepoDetails(anyString(), anyString())).
                thenReturn(Single.just(TestUtils.mockServerError()));

        presenter.fetchRepositoryDetails(mockRepository);

        verify(mockView).displayError();
    }

    @Test
    public void testFetchRepositoryDetailsUnauthorized() {
        when(mockAccountService.getRepoDetails(anyString(), anyString())).
                thenReturn(Single.just(TestUtils.mockUnauthorizedError()));

        presenter.fetchRepositoryDetails(mockRepository);

        verify(mockStorage).clearAccount();
        verify(mockView).onUserLogout();
    }

    @Test
    public void testFetchRepositoryDetailsSuccess() {
        when(mockAccountService.getRepoDetails(anyString(), anyString())).
                thenReturn(Single.just(Response.success(mockRepository)));

        presenter.fetchRepositoryDetails(mockRepository);

        verify(mockView).showRepositoryDetails(any(Repository.class));
    }

}