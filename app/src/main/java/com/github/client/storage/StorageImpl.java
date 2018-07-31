package com.github.client.storage;


import android.content.Context;

import static com.github.client.utils.Global.ACCOUNT_2FA;
import static com.github.client.utils.Global.ACCOUNT_BASIC_CREDENTIALS;
import static com.github.client.utils.Global.ACCOUNT_TOKEN;

/**
 * Created by Sam on 2/18/17.
 */
public class StorageImpl extends StoreBase implements Storage {

    private Context context;

    public StorageImpl(Context context) {
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
    public String fetchTwoFactorAuth() {
        return get(context, ACCOUNT_2FA);
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
    public void store2faCode(String authCode) {
        set(context, ACCOUNT_2FA, authCode);
    }

    @Override
    public void clearAccount() {
        clearAll(context);
    }
}
