package com.example.ezbooking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 3000; // Splash screen timeout in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delayed execution of the next activity (in this case, MainActivity)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start your app's main activity here
                Intent intent = new Intent(splash.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
