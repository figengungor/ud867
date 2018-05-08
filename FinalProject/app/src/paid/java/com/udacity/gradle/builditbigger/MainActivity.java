package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.figengungor.jokedisplayer.JokeActivity;



public class MainActivity extends AppCompatActivity {

    ProgressBar loadingPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingPb = findViewById(R.id.loadingPb);
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
        loadingPb.setVisibility(View.VISIBLE);
        new FetchJokeTask(listener).execute();
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


}




