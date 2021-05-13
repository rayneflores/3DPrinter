package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ryfsystems.a3dprinter.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }, 3000);
    }
}