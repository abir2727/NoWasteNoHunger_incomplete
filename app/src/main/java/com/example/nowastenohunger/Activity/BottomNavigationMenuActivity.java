package com.example.nowastenohunger.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.nowastenohunger.Fragment.MailFragment;
import com.example.nowastenohunger.Fragment.MakeDonationsFragment;
import com.example.nowastenohunger.Fragment.ProfileFragment;
import com.example.nowastenohunger.Fragment.SearchDonationsFragment;
import com.example.nowastenohunger.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationMenuActivity extends AppCompatActivity {

    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_menu);

        progressBar = (ProgressBar) findViewById(R.id.fragmentsProgressBar);

        BottomNavigationView b = findViewById(R.id.bottom_navigation_menu);
        b.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchDonationsFragment()).commit();


        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                progressBar.setVisibility(View.GONE);

            }

        }.start();

    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId())
            {
                case R.id.userProfile:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.makeDonations:
                    selectedFragment = new MakeDonationsFragment();
                    break;
                case R.id.searchDonations:
                    selectedFragment = new SearchDonationsFragment();
                    break;
                case R.id.contactus:
                    selectedFragment = new MailFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        }
}