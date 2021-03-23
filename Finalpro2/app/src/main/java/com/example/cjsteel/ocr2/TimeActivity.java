package com.example.cjsteel.ocr2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class TimeActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        final MediaPlayer intro = MediaPlayer.create(this, R.raw.intro);

        intro.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcome = new Intent(TimeActivity.this, MainActivity.class);
                startActivity(welcome);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
