package com.schibsted.blockapp;

import com.schibsted.blockapp.activity.MainActivity;
import com.schibsted.blockapp.model.ApiResponse;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Before
    public void setup(){
        GitApplication.setupRetrofit();
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isParseCorrect() throws Exception{
        BoxOfficeMovieResponse response = BoxOfficeMovieResponse.parseJSON("{'movies':[{'id':0,'title':'merde','production':{'director':'dir'}}]}");
        assertNotNull(response);
        assertFalse(response.getMovies().size()==0);
        assertTrue(response.getMovies().get(0).getTitle().equals("merde"));
    }

    @Test
    public void isAPICorrect() throws Exception{
        ApiResponse response = MainActivity.fetchMovies();
        Thread.sleep(2500);
        assertTrue(response.isSuccess());
    }



}