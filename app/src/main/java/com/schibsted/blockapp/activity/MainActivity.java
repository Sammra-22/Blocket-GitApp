package com.schibsted.blockapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.schibsted.blockapp.GitApplication;
import com.schibsted.blockapp.R;
import com.schibsted.blockapp.adapter.RepoAdapter;
import com.schibsted.blockapp.model.Repository;
import com.schibsted.blockapp.model.User;
import com.schibsted.blockapp.service.GitApiService;
import com.schibsted.blockapp.storage.LocalCache;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.schibsted.blockapp.utils.Global.INTENT_EXTRA_USER;


/**
 * Created by Sam on 2/18/17.
 */
public class MainActivity extends AppCompatActivity implements Callback<List<Repository>> {

    final static String TAG = MainActivity.class.getName();
    View progressView, noRepoView;
    RepoAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressView = findViewById(R.id.repos_progress);
        noRepoView = findViewById(R.id.no_repos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.git_icon);

        TextView name = (TextView)findViewById(R.id.name);
        TextView mail = (TextView)findViewById(R.id.mail);

        String userExtra = getIntent().getExtras().getString(INTENT_EXTRA_USER);
        User user = new Gson().fromJson(userExtra, User.class);
        if(!TextUtils.isEmpty(user.getName()))
            name.setText(user.getName());
        if(!TextUtils.isEmpty(user.getEmail()))
            mail.setText(user.getEmail());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.action_sync_repos), Snackbar.LENGTH_LONG).show();
                fetchRepositories();
            }
        });

        // Set List view's layout manager and Adapter
        RecyclerView listView = (RecyclerView) findViewById(R.id.repo_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listView.getContext(), layoutManager.getOrientation());
        listView.setLayoutManager(layoutManager);
        listView.addItemDecoration(dividerItemDecoration);
        mAdapter = new RepoAdapter(this);
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        fetchRepositories();
    }

    /**  API fetch all repositories for the user**/
    public void fetchRepositories(){
        GitApiService service = GitApplication.getRetrofit().create(GitApiService.class);
        Call<List<Repository>> call = service.getRepositories();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
        progressView.setVisibility(View.GONE);
        if(response.body()!=null) {
            mAdapter.setItems(response.body());
            mAdapter.notifyDataSetChanged();
        }else if(response.code()==401 || response.code()==403){
            logoutUser(null); // Authentication issue (access revoked) -- automatic logout
        }
        if(mAdapter.getItemCount()==0) // No repositories found
            noRepoView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onFailure(Call<List<Repository>> call, Throwable t) {
        progressView.setVisibility(View.GONE);
        // connection problem, keep displaying the cached repositories
    }

    /** Logout the user and redirect to the login screen**/
    public void logoutUser(View v){
        GitApplication.logout(this);
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
