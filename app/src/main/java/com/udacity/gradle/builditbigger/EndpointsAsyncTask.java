package com.udacity.gradle.builditbigger;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {
    private static MyApi myApiService = null;
    private ProgressDialog dialog;
    public boolean jokeLoaded = false;
    private OnJokeCallback mCallback;


    public EndpointsAsyncTask(OnJokeCallback callback) {
        mCallback = callback;
    }

    public EndpointsAsyncTask(Activity activity) {
        dialog = new ProgressDialog(activity);
        if(activity instanceof OnJokeCallback){
            mCallback = (OnJokeCallback) activity;
        }
    }



    @Override
    protected void onPreExecute() {
        if(dialog != null){
            dialog.setMessage("Loading your joke, please wait.");
            dialog.show();
        }
    }

    @Override
    protected String doInBackground(Context... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            return myApiService.tellJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        jokeLoaded = true;

        mCallback.onJokeRecived(result);

//        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    public interface OnJokeCallback {
        public void onJokeRecived(String joke);
    }
}