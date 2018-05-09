package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class FetchJokeTask extends AsyncTask<Void, Void, JokeResult> {
    private static MyApi myApiService = null;
    Listener listener;

    public interface Listener {
        void onSuccess(String joke);

        void onError(String error);
    }

    public FetchJokeTask(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected JokeResult doInBackground(Void... params) {
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8888/_ah/api/")
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
            return new JokeResult(null, myApiService.getJoke().execute().getData());
        } catch (IOException e) {
            return new JokeResult(e.getMessage(), null);
        }
    }

    @Override
    protected void onPostExecute(JokeResult jokeResult) {
        if (listener != null) {
            if (jokeResult.getException() != null)
                listener.onError(jokeResult.getException());
            else {
                listener.onSuccess(jokeResult.getResult());
            }
        }
    }
}