package com.example.nowastenohunger.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nowastenohunger.Fragment.SearchDonationsFragment;
import com.example.nowastenohunger.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SmsActivity extends AppCompatActivity implements View.OnClickListener {

    String phoneNumberOfWhoPosted, messageToBeSent, UID;
    Button confirmOrder, showRoute;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        Bundle bu =  getIntent().getExtras();
        phoneNumberOfWhoPosted = bu.getString("p");
        messageToBeSent = bu.getString("m");
        UID = bu.getString("UID");
        //btn_send();


        confirmOrder = findViewById(R.id.confirm_order);
        confirmOrder.setOnClickListener(this);
        //btn_send();
        databaseReference =  FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    public void onBackPressed() {
        //Intent newIntent = new Intent(Main2Activity.this, Main3Activity.class);
        //startActivity(newIntent);
        finishAffinity();
        Intent newIntent = new Intent(SmsActivity.this, BottomNavigationMenuActivity.class);
        startActivity(newIntent);
        Toast.makeText(this, "Back!!!", Toast.LENGTH_LONG).show();
    }

    public void btn_send()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            MyMessage();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }

    private void MyMessage()
    {
        //String phoneNumberOfWhoPosted = "+8801726228132", messageToBeSent = "Hi, from your Grameenphone sim!";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumberOfWhoPosted, null, messageToBeSent, null, null);
        Toast.makeText(this, "Message sent!!!", Toast.LENGTH_LONG).show();
        databaseReference.child(UID).child("post").removeValue();
        confirmOrder.setEnabled(false);
        confirmOrder.setBackground(getDrawable(R.drawable.disabled_button));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 0:
                if(grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    MyMessage();
                }
                else
                {
                    Toast.makeText(this, "You don't have required permission!!!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        btn_send();
    }
}