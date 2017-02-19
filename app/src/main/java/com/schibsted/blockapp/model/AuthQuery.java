package com.schibsted.blockapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sam on 2/18/17.
 */
public class AuthQuery {

    List<String> scopes;

    @SerializedName("client_id")
    String clientId;

    @SerializedName("client_secret")
    String clientSecret;

    String note;

    public AuthQuery(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.note = "GitApp Auth";
        this.scopes = new ArrayList<>();
    }

    public List<String> getScopes() {
        return scopes;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getNote() {
        return note;
    }

    public void addScope(String scope){
        scopes.add(scope);
    }


}
