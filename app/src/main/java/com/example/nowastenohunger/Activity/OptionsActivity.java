package com.example.nowastenohunger.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nowastenohunger.R;
import com.google.firebase.auth.FirebaseAuth;

public class OptionsActivity extends AppCompatActivity {

    private TextView helpLineTextView, signOutTextView, creditsTextView, aboutTextView;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        helpLineTextView = findViewById(R.id.helpLineTextView);
        signOutTextView = findViewById(R.id.signOutTextView);
        creditsTextView = findViewById(R.id.creditsTextView);
        aboutTextView = findViewById(R.id.aboutTextView);

        Toolbar optionsToolBar = findViewById(R.id.optionsToolBar);
        setSupportActionBar(optionsToolBar);
        getSupportActionBar().setTitle("More");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        optionsToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OptionsActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
                Toast.makeText(OptionsActivity.this, "Successfully logged out!", Toast.LENGTH_LONG).show();
            }
        });
        helpLineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void makePhoneCall() {
        String helpLineNumber = "01726228132";
        if (ContextCompat.checkSelfPermission(OptionsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(OptionsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else
        {
            String callingOperation = "tel:" + helpLineNumber;
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse(callingOperation));
            startActivity(i);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                makePhoneCall();
            }
            else
            {
                Toast.makeText(this, "You don't have required permission!!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}