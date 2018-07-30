package com.github.client.account;

import com.github.client.api.model.Repository;
import com.github.client.common.ViewBase;

import java.util.List;

interface AccountView extends ViewBase {

    void updateRepositories(List<Repository> repositories);

    void toggleRepositoriesView();

    void onUserLogout();

    void showRepositoryDetails(Repository repository);

    void displayError();
}
