package com.github.client.authentication;

import android.util.Base64;

import com.github.client.api.AuthService;
import com.github.client.api.model.AuthQuery;
import com.github.client.api.model.AuthToken;
import com.github.client.api.model.User;
import com.github.client.api.network.ErrorCode;
import com.github.client.common.PresenterBase;
import com.github.client.storage.Storage;
import com.github.client.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * Created by Sam on 4/18/17.
 */
class AuthPresenter extends PresenterBase<AuthView> {

    private final AuthService authService;
    private final Storage dataStorage;
    private CompositeDisposable compositeDisposable;

    AuthPresenter(AuthView v, AuthService authService, Storage dataStorage) {
        super(v);
        this.authService = authService;
        this.dataStorage = dataStorage;
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * API user authentication with new credentials - acquire the associated authorization token
     **/
    void authenticateUser(String username, String password, boolean hasPrivateAccess) {
        dataStorage.storeBasicCredentials(createBasicAuthCredentials(username, password));
        authenticateUser(hasPrivateAccess);
    }

    /**
     * API user authentication with 2 factor authentication code - acquire the associated authorization token
     **/
    void authenticateUser(String authCode, boolean hasPrivateAccess) {
        dataStorage.store2faCode(authCode);
        authenticateUser(hasPrivateAccess);
    }

    /**
     * API user authentication with existing credentials - acquire the associated authorization token
     **/
    void authenticateUser(boolean hasPrivateAccess) {
        AuthQuery query = new AuthQuery(Global.APP_KEY, Global.APP_SECRET);
        query.addScope(hasPrivateAccess ? Global.SCOPE_ALL_REPO : Global.SCOPE_PUBLIC_REPO);
        compositeDisposable.add(authService.authenticate(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleAuthenticationResponse, throwable -> {
                            view.toggleView(AuthView.Screen.SIGN_IN);
                            view.showAlertForResponseCode(ErrorCode.NO_CONNECTION);
                        }
                ));
    }

    /**
     * API user account information
     **/
    void loadUserAccount() {
        compositeDisposable.add(authService.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleUserAccountResponse, throwable -> {
                    view.showAlertForResponseCode(ErrorCode.NO_CONNECTION);
                    view.toggleView(AuthView.Screen.SIGN_IN);
                }));
    }

    // Create a Basic authentication credential for the user
    String createBasicAuthCredentials(String username, String password) {
        String credentials = username + ":" + password;
        return String.format("Basic %s", Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP).replace("\n", ""));
    }

    private void handleAuthenticationResponse(Response<AuthToken> response) {
        AuthToken authToken = response.body();
        if (authToken != null) { // Store token and attempt to fetch user account info
            dataStorage.storeToken(authToken.getToken());
            loadUserAccount();
        } else if (response.code() == 401 &&
                response.headers().names().contains(Global.HEADER_TWO_FACTOR_AUTH)) {
            view.toggleView(AuthView.Screen.TWO_FACTOR_AUTH);
        } else {  // failed to acquire the authorization token
            view.toggleView(AuthView.Screen.SIGN_IN);
            view.showAlertForResponseCode(ErrorCode.parse(response.code()));
        }
    }

    private void handleUserAccountResponse(Response<User> response) {
        if (response.body() != null) { // Pass along the user account to the Main activity
            view.launchMainActivity(response.body());
        } else if (response.code() == 401 || response.code() == 403) { // Revoked or expired Token
            dataStorage.clearAccount();
            view.toggleView(AuthView.Screen.SIGN_IN);
        } else {
            view.showAlertForResponseCode(ErrorCode.UNKNOWN);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }
}
