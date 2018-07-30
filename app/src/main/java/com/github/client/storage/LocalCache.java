package com.github.client.storage;

import android.content.Context;

import static com.github.client.utils.Global.PREF_ACCOUNT;

/**
 * Created by Sam on 2/18/17.
 * Local storage to cache user/settings
 */
abstract class LocalCache {

    String get(Context context, String key) {
        return context.getSharedPreferences(PREF_ACCOUNT, 0).getString(key, null);
    }

    void set(Context context, String key, String value) {
        context.getSharedPreferences(PREF_ACCOUNT, 0).edit().putString(key, value).apply();
    }

    void clearAll(Context context) {
        context.getSharedPreferences(PREF_ACCOUNT, 0).edit().clear().apply();
    }
}
