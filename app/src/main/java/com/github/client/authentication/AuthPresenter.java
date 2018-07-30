package com.github.client.authentication;

import android.util.Base64;

import com.github.client.api.AuthService;
import com.github.client.api.model.AuthQuery;
import com.github.client.api.model.AuthToken;
import com.github.client.api.network.ErrorCode;
import com.github.client.common.PresenterBase;
import com.github.client.storage.Storage;
import com.github.client.utils.Global;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by Sam on 4/18/17.
 */
class AuthPresenter extends PresenterBase<AuthView> {

    private final AuthService authService;
    private final Storage dataStorage;
    private List<Subscription> subscriptions = new ArrayList<>();

    AuthPresenter(AuthView v, AuthService authService, Storage dataStorage) {
        super(v);
        this.authService = authService;
        this.dataStorage = dataStorage;
    }

    /**
     * API user authentication and acquire the associated authorization token
     **/
    void authenticateUser(String username, String password, boolean hasPrivateAccess) {
        dataStorage.storeBasicCredentials(createBasicAuthCredentials(username, password));
        AuthQuery query = new AuthQuery(Global.APP_KEY, Global.APP_SECRET);
        query.addScope(hasPrivateAccess ? Global.SCOPE_ALL_REPO : Global.SCOPE_PUBLIC_REPO);
        subscriptions.add(authService.authenticate(query)
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                            AuthToken authToken = response.body();
                            if (authToken != null) { // Store token and attempt to fetch user account info
                                dataStorage.storeToken(authToken.getToken());
                                loadUserAccount();
                            } else {  // failed to acquire the authorization token
                                view.toggleSignInForm(true);
                                view.showAlertForResponseCode(ErrorCode.parse(response.code()));
                            }
                        }, throwable -> {
                            view.toggleSignInForm(true);
                            view.showAlertForResponseCode(ErrorCode.NO_CONNECTION);
                        }
                ));
    }

    /**
     * API user account information
     **/
    void loadUserAccount() {
        subscriptions.add(authService.getUser()
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    if (response.body() != null) { // Pass along the user account to the Main activity
                        view.launchMainActivity(response.body());
                    } else if (response.code() == 401 || response.code() == 403) { // Revoked or expired Token
                        dataStorage.clearAccount();
                        view.toggleSignInForm(true);
                    } else {
                        view.showAlertForResponseCode(ErrorCode.UNKNOWN);
                    }
                }, throwable -> {
                    view.showAlertForResponseCode(ErrorCode.NO_CONNECTION);
                    view.toggleSignInForm(true);
                }));
    }

    // Create a Basic authentication credential for the user
    String createBasicAuthCredentials(String username, String password) {
        String credentials = username + ":" + password;
        return String.format("Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP).replace("\n", ""));
    }

    @Override
    public void onStop() {
        super.onStop();
        for (Subscription subscription : subscriptions) {
            subscription.unsubscribe();
        }
    }
}
