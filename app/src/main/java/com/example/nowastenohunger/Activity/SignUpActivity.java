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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button signUpButton;
    EditText signUpEmailEditText, signUpPasswordEditText;
    ProgressBar signUpProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        signUpButton = findViewById(R.id.signUpButton);
        signUpEmailEditText = findViewById(R.id.signUpEmailEditText);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);

        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.signUpButton)
        {
            userRegistration();
        }
    }

    private void userRegistration()
    {
        String email = signUpEmailEditText.getText().toString().trim();
        String password = signUpPasswordEditText.getText().toString().trim();

        if(email.isEmpty())
        {
            signUpEmailEditText.setError("An Email is nedded!");
            signUpEmailEditText.requestFocus();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signUpEmailEditText.setError("Email is invalid!");
            signUpEmailEditText.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            signUpPasswordEditText.setError("Enter a password!");
            signUpPasswordEditText.requestFocus();
            return;
        }
        if(password.length()<8)
        {
            signUpPasswordEditText.setError("Password must have at least 8 characters!");
            signUpPasswordEditText.requestFocus();
            return;
        }

        signUpProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signUpProgressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                finish();
                                Intent i = new Intent(SignUpActivity.this,  MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                Toast.makeText(SignUpActivity.this, "Verfication Link sent to your email!\nYou need to verify before signing in.", Toast.LENGTH_LONG).show();
                                signUpPasswordEditText.setText("");
                                signUpEmailEditText.setText("");
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(SignUpActivity.this, "An account using this email already exists!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}