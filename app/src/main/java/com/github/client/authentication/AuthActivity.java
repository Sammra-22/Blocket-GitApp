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
    EditText usernameEditText, passwordEditText, codeEditText;
    CheckBox privateAccessBox;
    View signInForm, progress, codeForm;

    @Override
    protected AuthPresenter createPresenter() {
        return new AuthPresenter(this, apiManager.getAuthService(), storage);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportActionBar().hide();
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        privateAccessBox = findViewById(R.id.access_private);
        codeEditText = findViewById(R.id.auth_code);
        signInForm = findViewById(R.id.login_form);
        codeForm = findViewById(R.id.two_factor_auth_form);
        progress = findViewById(R.id.login_progress);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        checkHasToken();
    }

    private void checkHasToken() {
        String token = storage.fetchToken();
        if (!TextUtils.isEmpty(token)) {
            presenter.loadUserAccount();
        } else {
            toggleView(Screen.SIGN_IN);
        }
    }

    /**
     * Show/hide login display
     **/
    @Override
    public void toggleView(Screen screen) {
        signInForm.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        codeForm.setVisibility(View.GONE);
        switch (screen) {
            case PROGRESS:
                progress.setVisibility(View.VISIBLE);
                break;
            case SIGN_IN:
                signInForm.setVisibility(View.VISIBLE);
                break;
            case TWO_FACTOR_AUTH:
                codeForm.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Sign in button action
     **/
    public void signIn(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            DialogUtils.showAlert(this, getString(R.string.error_missing_credentials));
        } else {
            toggleView(Screen.PROGRESS);
            presenter.authenticateUser(username, password, privateAccessBox.isChecked());
        }
    }

    /**
     * Submit button action
     **/
    public void submit(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        String code = codeEditText.getText().toString();
        if (TextUtils.isEmpty(code)) {
            DialogUtils.showAlert(this, getString(R.string.error_missing_code));
        } else {
            toggleView(Screen.PROGRESS);
            presenter.authenticateUser(code, privateAccessBox.isChecked());
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
