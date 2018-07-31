package com.github.client.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.client.api.ApiManager;
import com.github.client.storage.Storage;
import com.github.client.storage.StorageImpl;


/**
 * Created by Sam on 4/18/17.
 */
public abstract class BaseActivity<Presenter extends PresenterBase> extends AppCompatActivity {

    protected Presenter presenter;
    protected ApiManager apiManager;
    protected Storage storage;

    protected abstract Presenter createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new StorageImpl(this);
        apiManager = ApiManager.getInstance(storage);
        presenter = createPresenter();
        presenter.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
