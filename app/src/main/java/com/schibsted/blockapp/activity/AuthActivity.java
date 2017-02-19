package com.schibsted.blockapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.schibsted.blockapp.GitApplication;
import com.schibsted.blockapp.R;
import com.schibsted.blockapp.model.AuthQuery;
import com.schibsted.blockapp.model.AuthToken;
import com.schibsted.blockapp.model.User;
import com.schibsted.blockapp.service.GitApiService;
import com.schibsted.blockapp.storage.LocalCache;
import com.schibsted.blockapp.utils.Dialog;
import com.schibsted.blockapp.utils.Global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.schibsted.blockapp.utils.Global.INTENT_EXTRA_USER;

/**
 * Created by Sam on 2/18/17.
 */
public class AuthActivity extends AppCompatActivity implements Callback<AuthToken>{

    static final String TAG = AuthActivity.class.getName();
    InputMethodManager imm;
    EditText mUsername, mPassword;
    CheckBox mPrivateAccess;
    View form, progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mUsername = (EditText)findViewById(R.id.username);
        mPassword = (EditText)findViewById(R.id.password);
        mPrivateAccess = (CheckBox)findViewById(R.id.access_private);
        form = findViewById(R.id.login_form);
        progress = findViewById(R.id.login_progress);
        String token = LocalCache.getInstance().getToken(this);
        if(TextUtils.isEmpty(token))
            showAuthForm(true);
        else
            loadUserAccount();
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**  Show/hide login display **/
    private void showAuthForm(boolean isVisible){
        if(isVisible) {
            form.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }else{
            form.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }
    }

    /**  Sign in button action **/
    public void signIn(View v){
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Dialog.showAlert(this, getString(R.string.error_missing_credentials));
        }else{
            showAuthForm(false);
            // Create a Basic authentication credential for the user
            String credentials = username+":"+password;
            String credBase64 = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
            authenticateUser(credBase64);
        }
    }

    /**  API user authentication and acquire the associated authorization token **/
    private void authenticateUser(String basicAuth){
        GitApiService service = GitApplication.setupRetrofit(basicAuth).create(GitApiService.class);
        AuthQuery query = new AuthQuery(Global.APP_KEY,Global.APP_SECRET);
        if(mPrivateAccess.isChecked())
            query.addScope(Global.SCOPE_ALL_REPO);
        else
            query.addScope(Global.SCOPE_PUBLIC_REPO);
        Call<AuthToken> call = service.authenticate(query);
        call.enqueue(this);
    }

    /**  API user account information  **/
    private void loadUserAccount(){
        GitApiService service = GitApplication.getRetrofit().create(GitApiService.class);
        Call<User> call = service.getUser();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body()!=null) { // Pass along the user account to the Main activity
                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    intent.putExtra(INTENT_EXTRA_USER, new Gson().toJson(response.body()));
                    startActivity(intent);
                    finish();
                }else
                    Dialog.showAlert(AuthActivity.this, getString(R.string.error_unknown));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Dialog.showAlert(AuthActivity.this, getString(R.string.error_connection));
                GitApplication.logout(getBaseContext());
                showAuthForm(true);
            }
        });
    }


    @Override
    public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
        Log.i(TAG, "SUCCESS: "+ response.code());
        AuthToken authToken = response.body();
        if(authToken!=null) { // Store token and attempt to fetch user account info
            LocalCache.getInstance().setToken(this, authToken.getToken());
            GitApplication.setupRetrofit("token "+authToken.getToken());
            loadUserAccount();
        }else {  // failed to acquire the authorization token
            showAuthForm(true);
            switch (response.code()){
                case 401:
                    Dialog.showAlert(AuthActivity.this, getString(R.string.error_invalid_auth_title), getString(R.string.error_invalid_auth_body_1));break;
                case 403:
                    Dialog.showAlert(AuthActivity.this, getString(R.string.error_invalid_auth_title), getString(R.string.error_invalid_auth_body_2));break;
                default:
                    Dialog.showAlert(AuthActivity.this, getString(R.string.error_unknown));break;
            }
        }
    }

    @Override
    public void onFailure(Call<AuthToken> call, Throwable t) {
        Log.w(TAG, "FAILURE: "+t); // Probably connection issues
        Dialog.showAlert(AuthActivity.this, getString(R.string.error_connection));
        showAuthForm(true);
    }
}
