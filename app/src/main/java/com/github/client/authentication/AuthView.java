package com.github.client.authentication;

import com.github.client.api.network.ErrorCode;
import com.github.client.common.ViewBase;
import com.github.client.api.model.User;

/**
 * Created by Sam on 4/18/17.
 */
interface AuthView extends ViewBase {

    void toggleSignInForm(boolean isVisible);

    void showAlertForResponseCode(ErrorCode code);

    void launchMainActivity(User user);
}
