package com.schibsted.blockapp;

import android.content.Context;
import android.content.SharedPreferences;
import com.schibsted.blockapp.utils.Global;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;


@RunWith(MockitoJUnitRunner.class)
public class GitUnitTest {

    Context mMockContext;
    SharedPreferences sharedPrefs;
    GitApplication app;

    @Before
    public void setup(){
        this.sharedPrefs = Mockito.mock(SharedPreferences.class);
        this.mMockContext = Mockito.mock(Context.class);
        Mockito.when(mMockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
        app = new GitApplication(mMockContext);
    }

    @Test
    public void headers() throws Exception {
        app.setHeaders("test");
        Map<String,String> headers = app.getHeaders();
        assertTrue(headers.containsKey(Global.HEADER_USER_AGENT));
        assertTrue(headers.containsKey(Global.HEADER_AUTH));
        assertEquals("test",headers.get(Global.HEADER_AUTH));
    }

    @Test
    public void token() throws Exception {
        Mockito.when(sharedPrefs.getString(anyString(), anyString())).thenReturn("test");
        assertEquals("test",app.getToken());
    }

    @Test
    public void app_credentials() throws Exception {
        assertNotNull(Global.APP_KEY);
        assertNotNull(Global.APP_SECRET);
        assertEquals(20, Global.APP_KEY.length());
        assertEquals(40, Global.APP_SECRET.length());
    }





}