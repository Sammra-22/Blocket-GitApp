package com.github.client.utils;


/**
 * Created by Sam on 2/18/17.
 * Globally accessed constants
 */
public class Global {

    public static final String API_BASE_URL = "https://api.github.com";

    /**
     * OAuth app client key/secret for which to create the token.
     **/
    public static final String APP_KEY = "4dd090fbdfe7d850a001";
    public static final String APP_SECRET = "5014c932c9f5be38817af9965c149c8159394cf8";

    public static final String SCOPE_ALL_REPO = "repo";
    public static final String SCOPE_PUBLIC_REPO = "public_repo";

    public static final String HEADER_AUTH = "Authorization";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_TWO_FACTOR_AUTH = "X-GitHub-OTP";

    /**
     * Storage
     **/
    public static final String PREF_ACCOUNT = "Account";
    public static final String ACCOUNT_TOKEN = "Token";

    /**
     * Intents
     **/
    public static final String INTENT_EXTRA_USER = "extra_user";

}
