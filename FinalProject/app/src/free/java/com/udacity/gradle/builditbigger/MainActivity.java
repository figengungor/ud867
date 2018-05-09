package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.figengungor.jokedisplayer.JokeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {

    ProgressBar loadingPb;
    private InterstitialAd mInterstitialAd;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingPb = findViewById(R.id.loadingPb);
        getIdlingResource();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                openJokeActivity();
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(false);
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            openJokeActivity();
        }
    }

    public void setListener(FetchJokeTask.Listener listener) {
        this.listener = listener;
    }

    FetchJokeTask.Listener listener = new FetchJokeTask.Listener(){

        @Override
        public void onSuccess(String joke) {
            loadingPb.setVisibility(View.GONE);
            startActivity(new Intent(MainActivity.this, JokeActivity.class)
                    .putExtra(JokeActivity.EXTRA_JOKE, joke));
        }
        //https://stackoverflow.com/questions/3875184/cant-create-handler-inside-thread-that-has-not-called-looper-prepare
        @Override
        public void onError(final String error) {
            loadingPb.setVisibility(View.GONE);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });

        }
    };

    private void openJokeActivity(){
        loadingPb.setVisibility(View.VISIBLE);
        new FetchJokeTask(listener).execute();
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}




