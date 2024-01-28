package com.sp.healthease;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        mediaPlayer = MediaPlayer.create(this,R.raw.music);

        if (mediaPlayer != null) {
            mediaPlayer.start();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                Intent intent = new Intent(SplashScreen.this, Registration.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }

}
