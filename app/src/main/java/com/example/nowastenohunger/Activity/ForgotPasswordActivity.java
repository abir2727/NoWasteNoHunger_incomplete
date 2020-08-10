package com.example.nowastenohunger.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nowastenohunger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Button forgotPasswordButton;
    EditText forgotPasswordEmailEditText;
    ProgressBar forgotPasswordProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        forgotPasswordEmailEditText = findViewById(R.id.forgotPasswordEmailEditText);
        forgotPasswordProgressBar = findViewById(R.id.forgotPasswordProgressBar);

        forgotPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.forgotPasswordButton)
        {
            sendResetLink();
        }
    }

    private void sendResetLink()
    {
        forgotPasswordProgressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(forgotPasswordEmailEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                forgotPasswordProgressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    finish();
                    Intent i = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset link is sent to your email!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}