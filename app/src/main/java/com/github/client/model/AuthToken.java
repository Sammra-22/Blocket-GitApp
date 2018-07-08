package com.github.client.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sam on 2/18/17.
 */
public class AuthToken {

    long id;
    String url;
    String token;
    String note;
    @SerializedName("created_at")
    String created;
    @SerializedName("updated_at")
    String updated;


    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

    public String getNote() {
        return note;
    }
}
