package com.schibsted.blockapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sam on 2/18/17.
 */
public class Repository {

    long id;

    String name;

    @SerializedName("full_name")
    String fullName;

    @SerializedName("private")
    boolean isPrivate;

    String description;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("updated_at")
    String updatedAt;

    @SerializedName("pushed_at")
    String pushedAt;

    @SerializedName("git_url")
    String gitUrl;

    @SerializedName("ssh_url")
    String sshUrl;

    @SerializedName("clone_url")
    String cloneUrl;

    String language;

    String size;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getPushedAt() {
        return pushedAt;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public String getLanguage() {
        return language;
    }

    public String getSize() {
        return size;
    }

    public List<ItemDetail> getDetails(){
        List<ItemDetail> details = new ArrayList<>();
        details.add(new ItemDetail("ID: ", String.valueOf(id)));
        details.add(new ItemDetail("Name: ",fullName));
        details.add(new ItemDetail("Language: ",language));
        details.add(new ItemDetail("Desc: ",description));
        details.add(new ItemDetail("Access: ", ((isPrivate)?"Private":"Public")));
        details.add(new ItemDetail("Created: ", createdAt.substring(0,10)));
        details.add(new ItemDetail("Updated: ",  updatedAt.substring(0,10)));
        details.add(new ItemDetail("Pushed: ", pushedAt.substring(0,10)));
        details.add(new ItemDetail("Git url: ", sshUrl));
        details.add(new ItemDetail("SSH url: ", sshUrl));
        details.add(new ItemDetail("Clone url: ", cloneUrl));
        return details;
    }
}
