package com.github.client.storage;

import android.content.Context;
import android.content.SharedPreferences;

import static com.github.client.utils.Global.ACCOUNT_TOKEN;
import static com.github.client.utils.Global.PREF_ACCOUNT;

/**
 * Created by Sam on 2/18/17.
 * Local storage to cache user/settings
 */
public class LocalCache {

    static LocalCache mInstance;

    public static LocalCache getInstance() {
        if(mInstance==null)
            mInstance = new LocalCache();
        return mInstance;
    }

    public String get(Context context, String key){
        return context.getSharedPreferences(PREF_ACCOUNT, 0).getString(key, null);
    }

    public void set(Context context, String key, String value){
        context.getSharedPreferences(PREF_ACCOUNT, 0).edit().putString(key, value).commit();
    }

    public void clearAccount(Context context){
        context.getSharedPreferences(PREF_ACCOUNT, 0).edit().clear().commit();
    }

}
