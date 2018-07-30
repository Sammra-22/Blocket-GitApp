package com.github.client.api.model;


/**
 * Created by Sam on 2/18/17.
 */
public class User {

    private long id;
    private String name;
    private String company;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }
}
