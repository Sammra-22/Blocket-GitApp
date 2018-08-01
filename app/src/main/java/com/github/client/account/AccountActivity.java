package com.github.client.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.github.client.R;
import com.github.client.api.model.Repository;
import com.github.client.api.model.User;
import com.github.client.authentication.AuthActivity;
import com.github.client.common.BaseActivity;
import com.github.client.utils.DialogUtils;
import com.google.gson.Gson;

import java.util.List;

import static com.github.client.utils.Global.INTENT_EXTRA_USER;


/**
 * Created by Sam on 2/18/17.
 */
public class AccountActivity extends BaseActivity<AccountPresenter> implements AccountView, RepositoryListAdapter.RepositoryActionListener {

    View progressView, noRepoView;
    RepositoryListAdapter mAdapter;

    @Override
    protected AccountPresenter createPresenter() {
        return new AccountPresenter(this, apiManager.getAccountService(), storage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.git_icon);

        progressView = findViewById(R.id.repos_progress);
        noRepoView = findViewById(R.id.no_repos);

        if (getIntent().getExtras() != null) {
            String userExtra = getIntent().getExtras().getString(INTENT_EXTRA_USER);
            if (userExtra != null) {
                User user = new Gson().fromJson(userExtra, User.class);
                TextView name = findViewById(R.id.name);
                TextView mail = findViewById(R.id.mail);
                name.setText(user.getName());
                mail.setText(user.getEmail());
            }
        } else {
            finish();
        }

        setupActionButtons();
        setupRepositoryList();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.fetchRepositories();
    }

    private void setupRepositoryList() {
        RecyclerView listView = findViewById(R.id.repo_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listView.getContext(), layoutManager.getOrientation());
        listView.setLayoutManager(layoutManager);
        listView.addItemDecoration(dividerItemDecoration);
        mAdapter = new RepositoryListAdapter(this);
        listView.setAdapter(mAdapter);
    }

    private void setupActionButtons() {
        Button logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(v -> presenter.logoutUser());

        FloatingActionButton sync = findViewById(R.id.sync);
        sync.setOnClickListener(view -> {
            Snackbar.make(view, getString(R.string.action_sync_repos), Snackbar.LENGTH_LONG).show();
            view.animate().rotationBy(720f)
                    .setDuration(3000)
                    .setInterpolator(new AccelerateDecelerateInterpolator());
            presenter.fetchRepositories();
        });
    }

    @Override
    public void updateRepositories(List<Repository> repositories) {
        mAdapter.setItems(repositories);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void toggleRepositoriesView() {
        progressView.setVisibility(View.GONE);
        if (mAdapter.getItemCount() == 0) { // No repositories found
            noRepoView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUserLogout() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    public void onSelectRepository(Repository repository) {
        DialogUtils.showProgress(this, R.string.progress_details);
        presenter.fetchRepositoryDetails(repository);
    }

    @Override
    public void showRepositoryDetails(Repository repository) {
        DialogUtils.dismissProgress();
        DialogUtils.showListDialog(this, repository.getName(), repository.getDetails());
    }

    @Override
    public void displayError() {
        DialogUtils.dismissProgress();
        DialogUtils.showAlert(this, getString(R.string.error_unknown));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
