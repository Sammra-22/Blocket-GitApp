package com.schibsted.blockapp.storage;

import android.content.Context;
import android.content.SharedPreferences;

import static com.schibsted.blockapp.utils.Global.ACCOUNT_TOKEN;
import static com.schibsted.blockapp.utils.Global.PREF_ACCOUNT;

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

    public String getToken(Context context){
        return context.getSharedPreferences(PREF_ACCOUNT, 0).getString(ACCOUNT_TOKEN, null);
    }

    public void setToken(Context context, String token){
        context.getSharedPreferences(PREF_ACCOUNT, 0).edit().putString(ACCOUNT_TOKEN, token).commit();
    }

    public void clearAccount(Context context){
        context.getSharedPreferences(PREF_ACCOUNT, 0).edit().clear().commit();
    }

}
