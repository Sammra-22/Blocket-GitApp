package com.github.client.account;

import com.github.client.api.AccountService;
import com.github.client.api.model.Repository;
import com.github.client.common.PresenterBase;
import com.github.client.storage.Storage;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.schedulers.Schedulers;

public class AccountPresenter extends PresenterBase<AccountView> {

    private final Storage dataStorage;
    private final AccountService accountService;
    private List<Subscription> subscriptions = new ArrayList<>();

    AccountPresenter(AccountView v, AccountService accountService, Storage dataStorage) {
        super(v);
        this.accountService = accountService;
        this.dataStorage = dataStorage;
    }

    /**
     * API fetch all repositories for the user
     **/
    public void fetchRepositories() {
        subscriptions.add(accountService.getRepositories()
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    if (response.body() != null) {
                        view.updateRepositories(response.body());
                    } else if (response.code() == 401 || response.code() == 403) {
                        logoutUser(); // Authentication issue (access revoked) -- automatic logout
                    }
                    view.toggleRepositoriesView();
                }, throwable -> {
                    // connection problem, keep displaying the cached repositories
                    view.toggleRepositoriesView();
                }));
    }

    public void fetchRepositoryDetails(Repository repository) {
        String[] nameParts = repository.getFullName().split("/");
        String owner = nameParts[0];
        String name = nameParts[1];
        // API fetch repository details
        subscriptions.add(accountService.getRepoDetails(owner, name)
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    if (response.body() != null) {
                        view.showRepositoryDetails(response.body());
                    } else {
                        view.displayError();
                        if (response.code() == 401 || response.code() == 403) {
                            logoutUser(); // Access problem - Logout
                        }
                    }
                }));
    }

    public void logoutUser() {
        dataStorage.clearAccount();
        view.onUserLogout();
    }

    public void onStop() {
        super.onStop();
        for (Subscription subscription : subscriptions) {
            subscription.unsubscribe();
        }
    }
}
