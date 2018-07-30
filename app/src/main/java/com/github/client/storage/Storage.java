package com.github.client.storage;


/**
 * Created by Sam on 2/18/17.
 */
public interface Storage {

    String fetchToken();

    String fetchBasicCredentials();

    void storeToken(String token);

    void storeBasicCredentials(String credentials);

    void clearAccount();
}
