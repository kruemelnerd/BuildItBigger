package com.udacity.gradle.builditbigger;


import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class EnpointsAsyncTaskTest extends AndroidTestCase {

    @Mock
    private EndpointsAsyncTask.OnJokeCallback mCallback;


    @Before
    public void init(){
        mCallback = mock(EndpointsAsyncTask.OnJokeCallback.class);
        doNothing().when(mCallback).onJokeRecived(anyString());
    }

    @Test
    public void testAsyncTask() throws Exception {

        final CountDownLatch signal = new CountDownLatch(1);

        EndpointsAsyncTask task = new EndpointsAsyncTask(new EndpointsAsyncTask.OnJokeCallback() {
            @Override
            public void onJokeRecived(String joke) {
                mCallback.onJokeRecived(joke);
                signal.countDown();// notify the count down latch
            }
        });

        task.execute();
        signal.await();// wait for callback
    }
}
