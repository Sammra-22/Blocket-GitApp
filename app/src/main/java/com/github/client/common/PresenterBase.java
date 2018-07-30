package com.github.client.common;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

/**
 * Created by Sam on 4/18/17.
 */
public abstract class PresenterBase<View extends ViewBase> {

    protected View view;

    public PresenterBase(View v) {
        view = v;
    }

    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
    }

    @CallSuper
    public void onStart() {
    }

    @CallSuper
    public void onStop() {
    }

    @CallSuper
    public void onDestroy() {
    }
}
