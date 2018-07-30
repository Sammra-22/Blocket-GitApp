package com.github.client.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.github.client.R;
import com.github.client.account.AccountActivity;
import com.github.client.api.model.User;
import com.github.client.api.network.ErrorCode;
import com.github.client.common.BaseActivity;
import com.github.client.utils.DialogUtils;
import com.google.gson.Gson;

import static com.github.client.utils.Global.INTENT_EXTRA_USER;

/**
 * Created by Sam on 2/18/17.
 */
public class AuthActivity extends BaseActivity<AuthPresenter> implements AuthView {

    InputMethodManager imm;
    EditText mUsername, mPassword;
    CheckBox mPrivateAccess;
    View form, progress;

    @Override
    protected AuthPresenter createPresenter() {
        return new AuthPresenter(this, apiManager.getAccountService(), storage);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportActionBar().hide();
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mPrivateAccess = (CheckBox) findViewById(R.id.access_private);
        form = findViewById(R.id.login_form);
        progress = findViewById(R.id.login_progress);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        checkHasToken();
    }

    private void checkHasToken() {
        String token = storage.fetchToken();
        if (!TextUtils.isEmpty(token)) {
            presenter.loadUserAccount();
        } else {
            toggleSignInForm(true);
        }
    }

    /**
     * Show/hide login display
     **/
    @Override
    public void toggleSignInForm(boolean isVisible) {
        if (isVisible) {
            form.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        } else {
            form.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sign in button action
     **/
    public void signIn(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            DialogUtils.showAlert(this, getString(R.string.error_missing_credentials));
        } else {
            toggleSignInForm(false);
            presenter.authenticateUser(username, password, mPrivateAccess.isChecked());
        }
    }

    @Override
    public void showAlertForResponseCode(ErrorCode code) {
        switch (code) {
            case UNAUTHORIZED:
                DialogUtils.showAlert(this, getString(R.string.error_invalid_auth_title), getString(R.string.error_invalid_auth_body_1));
                break;
            case FORBIDDEN:
                DialogUtils.showAlert(this, getString(R.string.error_invalid_auth_title), getString(R.string.error_invalid_auth_body_2));
                break;
            case NO_CONNECTION:
                DialogUtils.showAlert(AuthActivity.this, getString(R.string.error_connection));
                break;
            default:
                DialogUtils.showAlert(this, getString(R.string.error_unknown));
                break;
        }
    }

    @Override
    public void launchMainActivity(User user) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(INTENT_EXTRA_USER, new Gson().toJson(user));
        startActivity(intent);
        finish();
    }
}
