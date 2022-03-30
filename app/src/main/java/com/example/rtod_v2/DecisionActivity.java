package com.example.rtod_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rtod_v2.Auth.AuthMainActivity;

public class DecisionActivity extends AppCompatActivity {

    Button toLogin, toSignUp, toSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        toLogin = findViewById(R.id.tologin);
        toSignUp = findViewById(R.id.tosignup);
        toSkip = findViewById(R.id.toskip);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DecisionActivity.this,com.example.rtod_v2.Auth.Login.class));
            }
        });

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DecisionActivity.this,com.example.rtod_v2.Auth.Register.class));
            }
        });

        toSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DecisionActivity.this, MainActivity.class));
            }
        });
    }
}