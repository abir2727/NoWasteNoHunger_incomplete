package com.example.nowastenohunger.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.nowastenohunger.R;

public class SmsActivity extends AppCompatActivity {

    String phoneNumberOfWhoPosted, messageToBeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        Bundle bu =  getIntent().getExtras();
        phoneNumberOfWhoPosted = bu.getString("p");
        messageToBeSent = bu.getString("m");
        btn_send();

        //btn_send();
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
}