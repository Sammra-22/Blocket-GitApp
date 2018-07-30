package com.github.client.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import com.TestUtils;
import com.github.client.api.AuthService;
import com.github.client.api.model.AuthQuery;
import com.github.client.api.model.AuthToken;
import com.github.client.api.model.User;
import com.github.client.api.network.ErrorCode;
import com.github.client.storage.Storage;
import com.github.client.utils.Global;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import retrofit2.Response;
import rx.Single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AuthPresenterTest {

    @Mock
    private
    Context mockContext;
    @Mock
    private
    SharedPreferences mockSharedPrefs;
    @Mock
    private
    AuthService mockAuthService;
    @Mock
    private
    Storage mockStorage;
    @Mock
    private
    AuthView mockView;

    private AuthPresenter presenter;

    @Before
    public void setup() {
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPrefs);
        presenter = spy(new AuthPresenter(mockView, mockAuthService, mockStorage));
    }

    @Test
    public void testAuthenticateUserFail() {
        String mockBasicAuth = "mockedBasicAuth";
        doReturn(mockBasicAuth).when(presenter)
                .createBasicAuthCredentials(anyString(), anyString());
        when(mockAuthService.authenticate(any(AuthQuery.class))).
                thenReturn(Single.just(TestUtils.mockServerError()));

        presenter.authenticateUser("username", "password", true);

        verify(mockStorage).storeBasicCredentials(mockBasicAuth);
        verify(mockView).toggleSignInForm(true);
        verify(mockView).showAlertForResponseCode(any(ErrorCode.class));
    }

    @Test
    public void testAuthenticateUserSuccess() {
        String mockToken = "mockedToken";
        String mockBasicAuth = "mockedBasicAuth";
        doNothing().when(presenter).loadUserAccount();
        doReturn(mockBasicAuth).when(presenter)
                .createBasicAuthCredentials(anyString(), anyString());

        when(mockAuthService.authenticate(any(AuthQuery.class))).
                thenReturn(Single.just(Response.success(new AuthToken(mockToken))));

        presenter.authenticateUser("username", "password", true);

        verify(mockStorage).storeBasicCredentials(mockBasicAuth);
        verify(mockStorage).storeToken(mockToken);
    }

    @Test
    public void testGetUserAccountFail() {
        when(mockAuthService.getUser()).
                thenReturn(Single.just(TestUtils.mockServerError()));

        presenter.loadUserAccount();

        verify(mockView).showAlertForResponseCode(any(ErrorCode.class));
    }

    @Test
    public void testGetUserAccountUnauthorized() {
        when(mockAuthService.getUser()).
                thenReturn(Single.just(TestUtils.mockUnauthorizedError()));

        presenter.loadUserAccount();

        verify(mockStorage).clearAccount();
        verify(mockView).toggleSignInForm(true);
    }

    @Test
    public void testGetUserAccountSuccess() {
        doNothing().when(presenter).loadUserAccount();
        User mockUser = new User("mockName", "mock@mail");
        when(mockAuthService.getUser()).
                thenReturn(Single.just(Response.success(mockUser)));

        presenter.loadUserAccount();

        verify(mockView).launchMainActivity(eq(mockUser));
    }


    @Test
    public void testAppCredentials() {
        assertNotNull(Global.APP_KEY);
        assertNotNull(Global.APP_SECRET);
        assertEquals(20, Global.APP_KEY.length());
        assertEquals(40, Global.APP_SECRET.length());
    }

}