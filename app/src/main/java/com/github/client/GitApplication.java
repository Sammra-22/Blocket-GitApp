package com.github.client;

import android.app.Application;
import android.content.Context;

/**
 * Created by Sam on 2/18/17.
 */
public class GitApplication extends Application {

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
}
