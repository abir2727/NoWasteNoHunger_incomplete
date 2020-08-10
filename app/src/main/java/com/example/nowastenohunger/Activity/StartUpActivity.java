package com.example.nowastenohunger.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.nowastenohunger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartUpActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Intent newIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        nextActivity();
    }

    private void nextActivity() {

        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (user != null)
                    newIntent = new Intent(StartUpActivity.this, BottomNavigationMenuActivity.class);
                else
                   newIntent = new Intent(StartUpActivity.this, MainActivity.class);

                startActivity(newIntent);
                finish();
            }

        }.start();
    }
}