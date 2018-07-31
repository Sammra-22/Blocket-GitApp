package com.github.client.account;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.client.R;
import com.github.client.api.model.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 2/18/17.
 * Adapter for displaying Repositories in a list
 */
public class RepositoryListAdapter extends RecyclerView.Adapter<RepositoryListAdapter.ViewHolder> {

    private List<Repository> repositoryList;
    private RepositoryActionListener repositoryListener;

    RepositoryListAdapter(RepositoryActionListener listener) {
        repositoryListener = listener;
        repositoryList = new ArrayList<>();
    }

    public void setItems(List<Repository> repoList) {
        this.repositoryList = repoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Repository repo = repositoryList.get(position);
        holder.repoName.setText(repo.getName());
        holder.repoDesc.setText(repo.getDescription());
        holder.repoDate.setText(repo.getCreatedAt().substring(0, 10));
        holder.itemView.setOnClickListener(v -> repositoryListener.onSelectRepository(repo));
    }

    @Override
    public int getItemCount() {
        return repositoryList.size();
    }

    interface RepositoryActionListener {
        void onSelectRepository(Repository repository);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView repoName, repoDesc, repoDate;

        ViewHolder(View v) {
            super(v);
            repoName = v.findViewById(R.id.repo_name);
            repoDesc = v.findViewById(R.id.repo_desc);
            repoDate = v.findViewById(R.id.repo_date);
        }
    }

    class RepositoryClickListener implements View.OnClickListener {

        private final Repository repository;

        RepositoryClickListener(Repository repository) {
            this.repository = repository;
        }

        @Override
        public void onClick(View v) {
            repositoryListener.onSelectRepository(repository);
        }
    }
}
