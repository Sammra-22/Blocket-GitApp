package com.github.client.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sam on 2/18/17.
 */
public class AuthToken {

    private long id;
    private String url;
    private String token;
    private String note;
    @SerializedName("created_at")
    String created;
    @SerializedName("updated_at")
    String updated;

    public AuthToken(String token){
        this.token = token;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNote(String note) {
        this.note = note;
    }

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
