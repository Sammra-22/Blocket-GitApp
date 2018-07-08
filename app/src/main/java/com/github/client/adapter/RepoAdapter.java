package com.github.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.client.R;
import com.github.client.listener.RepoClickListener;
import com.github.client.model.Repository;
import com.github.client.utils.Dialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sam on 2/18/17.
 * Adapter for displaying Repositories in a list
 */
public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    List<Repository> repoList;
    Context context;

    public RepoAdapter(Context context){
        this.context = context;
        repoList = new ArrayList<>();
    }

    public void setItems(List<Repository> repoList){
        this.repoList = repoList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Repository repo = repoList.get(position);
        holder.mRepoName.setText(repo.getName());
        holder.mRepoDesc.setText(repo.getDescription());
        holder.mRepoDate.setText(repo.getCreatedAt().substring(0,10));
        holder.mItem.setOnClickListener(new RepoClickListener(context, repo));
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mItem;
        public TextView mRepoName,mRepoDesc,mRepoDate;
        public ViewHolder(View v) {
            super(v);
            mItem = v;
            mRepoName = (TextView) v.findViewById(R.id.repo_name);
            mRepoDesc = (TextView) v.findViewById(R.id.repo_desc);
            mRepoDate = (TextView) v.findViewById(R.id.repo_date);
        }
    }
}
