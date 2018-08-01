package com.github.client.account;

import com.github.client.api.AccountService;
import com.github.client.api.model.Repository;
import com.github.client.common.PresenterBase;
import com.github.client.storage.Storage;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


class AccountPresenter extends PresenterBase<AccountView> {

    private final AccountService accountService;
    private final Storage dataStorage;
    private CompositeDisposable compositeDisposable;

    AccountPresenter(AccountView v, AccountService accountService, Storage dataStorage) {
        super(v);
        this.accountService = accountService;
        this.dataStorage = dataStorage;
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * API fetch all repositories for the user
     **/
    void fetchRepositories() {
        compositeDisposable.add(accountService.getRepositories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleRepositoriesResponse, throwable -> {// connection problem, keep displaying the cached repositories
                    view.toggleRepositoriesView();
                }));
    }

    void fetchRepositoryDetails(Repository repository) {
        String[] nameParts = repository.getFullName().split("/");
        String owner = nameParts[0];
        String name = nameParts[1];
        // API fetch repository details
        compositeDisposable.add(accountService.getRepoDetails(owner, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleRepositoryDetailsResponse, throwable -> view.displayError()));
    }

    public void logoutUser() {
        dataStorage.clearAccount();
        view.onUserLogout();
    }

    private void handleRepositoriesResponse(Response<List<Repository>> response) {
        if (response.body() != null) {
            view.updateRepositories(response.body());
        } else if (response.code() == 401 || response.code() == 403) {// Authentication issue (access revoked) -- automatic logout
            logoutUser();
        }
        view.toggleRepositoriesView();
    }

    private void handleRepositoryDetailsResponse(Response<Repository> response) {
        if (response.body() != null) {
            view.showRepositoryDetails(response.body());
        } else {
            view.displayError();
            if (response.code() == 401 || response.code() == 403) {// Access problem - Logout
                logoutUser();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }
}
