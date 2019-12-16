package com.unse.gestiondepolideportivo.Actividades;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.unse.gestiondepolideportivo.R;


public class SplashScreenActivity extends AppCompatActivity {

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setViewsConfig();

        loadViews();

        loadData();

        loadListener();

    }

    private void loadListener() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 4000);
    }

    private void loadData() {
        //Glide.with(this).load(R.color.colorPrimary)
          //      .into(linearFondo);

    }

    private void loadViews() {
       // mProgress = findViewById(R.id.splash_screen_progress_bar);
        //linearFondo = findViewById(R.id.splashbackgr);

    }

    private void setViewsConfig() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


}
