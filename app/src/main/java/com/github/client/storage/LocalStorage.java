package com.github.client.storage;


import android.content.Context;

import static com.github.client.utils.Global.ACCOUNT_BASIC_CREDENTIALS;
import static com.github.client.utils.Global.ACCOUNT_TOKEN;

/**
 * Created by Sam on 2/18/17.
 */
public class LocalStorage extends LocalCache implements Storage {

    private Context context;

    public LocalStorage(Context context) {
        this.context = context;
    }

    @Override
    public String fetchToken() {
        return get(context, ACCOUNT_TOKEN);
    }

    @Override
    public String fetchBasicCredentials() {
        return get(context, ACCOUNT_BASIC_CREDENTIALS);
    }

    @Override
    public void storeToken(String token) {
        set(context, ACCOUNT_TOKEN, token);
    }

    @Override
    public void storeBasicCredentials(String credentials) {
        set(context, ACCOUNT_BASIC_CREDENTIALS, credentials);
    }

    @Override
    public void clearAccount() {
        clearAll(context);
    }
}
