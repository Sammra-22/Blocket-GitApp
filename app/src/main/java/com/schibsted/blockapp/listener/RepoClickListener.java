package com.schibsted.blockapp.listener;

import android.content.Context;
import android.view.View;

import com.schibsted.blockapp.GitApplication;
import com.schibsted.blockapp.R;
import com.schibsted.blockapp.model.Repository;
import com.schibsted.blockapp.service.GitApiService;
import com.schibsted.blockapp.utils.Dialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sam on 2/19/17.
 * Click listener for a given repository
 */
public class RepoClickListener implements View.OnClickListener, Callback<Repository> {


    Context context;
    Repository repository;

    public RepoClickListener(Context context, Repository repository){
        this.context = context;
        this.repository = repository;
    }

    @Override
    public void onClick(View view) {
        Dialog.showProgress(context, context.getString(R.string.progress_details));
        GitApiService service = GitApplication.getRetrofit().create(GitApiService.class);
        String[] nameParts = repository.getFullName().split("/");
        String owner = nameParts[0];
        String name = nameParts[1];
        // API fetch repository details
        Call<Repository> call = service.getRepoDetails(owner, name);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Repository> call, Response<Repository> response) {
        Dialog.dismissProgress();
        if(response.body()!=null)
            Dialog.showListDialog(context, response.body().getName(), response.body().getDetails());
        else{
            if(response.code()==401 || response.code()==403)
                GitApplication.logout(context); // Access problem - Logout
            Dialog.showAlert(context, context.getString(R.string.error_unknown));
        }
    }

    @Override
    public void onFailure(Call<Repository> call, Throwable t) {
        Dialog.dismissProgress();
        Dialog.showAlert(context, context.getString(R.string.error_connection));
    }
}
