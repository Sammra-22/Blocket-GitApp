package com.github.client.storage;


/**
 * Created by Sam on 2/18/17.
 */
public interface Storage {

    String fetchToken();

    String fetchBasicCredentials();

    String fetchTwoFactorAuth();

    void storeToken(String token);

    void storeBasicCredentials(String credentials);

    void store2faCode(String authCode);

    void clearAccount();
}
