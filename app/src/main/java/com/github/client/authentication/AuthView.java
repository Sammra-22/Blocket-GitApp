package com.github.client.authentication;

import com.github.client.api.network.ErrorCode;
import com.github.client.common.ViewBase;
import com.github.client.api.model.User;

/**
 * Created by Sam on 4/18/17.
 */
interface AuthView extends ViewBase {

    enum Screen{ PROGRESS, SIGN_IN, TWO_FACTOR_AUTH}

    void toggleView(Screen screen);

    void showAlertForResponseCode(ErrorCode code);

    void launchMainActivity(User user);
}
