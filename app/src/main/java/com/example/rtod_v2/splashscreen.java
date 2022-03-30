package com.example.rtod_v2;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {
    private static final long Splash_length = 2000;

    ImageView splashLogo;
    TextView logo_text;
//    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        splashLogo = findViewById(R.id.splashLogo);
        logo_text = findViewById(R.id.logo_text);
        splashLogo.animate().translationY(600).setDuration(1000).setStartDelay(1000);
        logo_text.animate().translationY(620).setDuration(1000).setStartDelay(1000);

//        Intent intent = new Intent(this, loginActivity.class);
//        splashscreen.this.startActivity(intent);
//        splashscreen.this.finish();
//        timer = new Timer();

//        ActionBar mActionbar = getSupportActionBar();
//        mActionbar.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splashscreen.this,DecisionActivity.class));
                splashscreen.this.finish();
            }
        },Splash_length);



    }
}